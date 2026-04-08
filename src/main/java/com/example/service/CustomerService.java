package com.example.service;

import com.example.dto.CustomerRequest;
import com.example.dto.CustomerResponse;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CustomerMapper;
import com.example.repository.BillRepository;
import com.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BillRepository  billRepository;

    public CustomerService(CustomerRepository customerRepository,
                           BillRepository billRepository) {
        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAllByEnabledIsTrueOrderByIdDesc()
                .stream().map(CustomerMapper::toResponse).toList();
    }

    public CustomerResponse create(CustomerRequest dto) {
        if (customerRepository.existsByEnabledIsTrueAndEmailIgnoreCase(dto.email())) {
            throw new ResourceAlreadyExistsException("Customer with email already exists");
        }
        var customerToSave = CustomerMapper.toEntity(dto);
        var customerSaved = customerRepository.save(customerToSave);
        return CustomerMapper.toResponse(customerSaved);
    }

    public CustomerResponse findOne(Long id) {
        return customerRepository.findByEnabledIsTrueAndId(id)
                .map(CustomerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
    }

    public CustomerResponse update(Long id, CustomerRequest dto) {
        var customer = customerRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
        if (customerRepository.existsByEnabledIsTrueAndEmailIgnoreCaseAndIdNot(dto.email(), id)) {
            throw new ResourceAlreadyExistsException("Customer with email already exists");
        }
        var customerToUpdate = CustomerMapper.toUpdate(customer, dto);
        var customerUpdated = customerRepository.save(customerToUpdate);
        return CustomerMapper.toResponse(customerUpdated);
    }

    public void delete(Long id) {
        var customer = customerRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
        if (!billRepository.findAllByEnabledIsTrueAndCustomer_Id(id).isEmpty()) {
            throw new ResourceAlreadyExistsException("Customer has bills. Don't delete it");
        }
        var customerToDelete = CustomerMapper.toDelete(customer);
        customerRepository.save(customerToDelete);
    }

}
