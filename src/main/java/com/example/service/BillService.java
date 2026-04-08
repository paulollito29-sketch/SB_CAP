package com.example.service;

import com.example.dto.BillFindAllResponse;
import com.example.dto.BillItemResponse;
import com.example.dto.BillRequest;
import com.example.dto.BillResponse;
import com.example.entity.BillDetailEntity;
import com.example.entity.BillEntity;
import com.example.entity.BillStatus;
import com.example.entity.PaymentMethod;
import com.example.exception.InputEnumException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.BillDetailRepository;
import com.example.repository.BillRepository;
import com.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;
    private final BillDetailRepository billDetailRepository;
    private final BillDetailService billDetailService;

    public BillService(BillRepository billRepository,
                       CustomerRepository customerRepository,
                       BillDetailRepository billDetailRepository,
                       BillDetailService billDetailService) {
        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
        this.billDetailRepository = billDetailRepository;
        this.billDetailService = billDetailService;
    }

    // list
    public List<BillFindAllResponse> findAll() {
        return billRepository.findAllByEnabledIsTrueOrderByIdDesc()
                .stream().map(bill -> new BillFindAllResponse(
                        bill.getId(),
                        bill.getIssueDate(),
                        bill.getStatus().toString(),
                        bill.getPaymentMethod().toString(),
                        bill.getTotal(),
                        bill.getCustomer().getFirstName() + " " + bill.getCustomer().getLastName(),
                        bill.getBillNumber()
                )).toList();
    }

    // create
    public BillResponse create(BillRequest dto) {
        var customer = customerRepository.findByEnabledIsTrueAndId(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.customerId()));
        var billToSave = BillEntity.builder()
                .issueDate(dto.issueDate())
                .status(parseStatus(dto.status()))
                .paymentMethod(parsePaymentMethod(dto.paymentMethod()))
                .billNumber(generateBillNumber())
                .customer(customer)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        var billSaved = billRepository.save(billToSave);

        var details = new ArrayList<BillDetailEntity>();
        var grandTotal = BigDecimal.ZERO;

        for (var itemRequest : dto.items()) {
            var detail = billDetailService.saveDetail(billSaved, itemRequest.productId(), itemRequest.quantity());
            details.add(detail);
            var subTotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
            grandTotal = grandTotal.add(subTotal);
        }
        billSaved.setTotal(grandTotal);
        // update
        billRepository.save(billSaved);
        return new BillResponse(
                billSaved.getId(),
                billSaved.getIssueDate(),
                billSaved.getStatus().toString(),
                billSaved.getPaymentMethod().toString(),
                billSaved.getTotal(),
                billSaved.getCustomer().getFirstName() + " " + billSaved.getCustomer().getLastName(),
                billSaved.getBillNumber(),
                details.stream().map(detail -> new BillItemResponse(
                        detail.getId(),
                        detail.getProduct().getId(),
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getUnitPrice(),
                        detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()))
                )).toList()
        );
    }

    // find one
    public BillResponse findOne(Long id) {
        var bill = billRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        var details = billDetailService.findAllByBillId(id);

        return new BillResponse(
                bill.getId(),
                bill.getIssueDate(),
                bill.getStatus().toString(),
                bill.getPaymentMethod().toString(),
                bill.getTotal(),
                bill.getCustomer().getFirstName() + " " + bill.getCustomer().getLastName(),
                bill.getBillNumber(),
                details.stream().map(detail -> new BillItemResponse(
                        detail.getId(),
                        detail.getProduct().getId(),
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getUnitPrice(),
                        detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()))
                )).toList()
        );
    }

    // update
    public BillResponse update(Long id, BillRequest dto) {
        var bill = billRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        // validacion de ESTADO de FACTURA
        if (!bill.getStatus().equals(BillStatus.PENDING)) {
            throw new InputEnumException("Cannot update bill. Only PENDING bills can be updated.");
        }
        var customer = customerRepository.findByEnabledIsTrueAndId(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.customerId()));
        bill.setIssueDate(dto.issueDate());
        bill.setStatus(parseStatus(dto.status()));
        bill.setPaymentMethod(parsePaymentMethod(dto.paymentMethod()));
        bill.setCustomer(customer);
        bill.setUpdatedAt(LocalDateTime.now()); // eye!
        var billUpdated = billRepository.save(bill);

        var details = billDetailRepository.findAllByEnabledIsTrueAndBill_Id(id);

        return new BillResponse(
                billUpdated.getId(),
                billUpdated.getIssueDate(),
                billUpdated.getStatus().toString(),
                billUpdated.getPaymentMethod().toString(),
                billUpdated.getTotal(),
                billUpdated.getCustomer().getFirstName() + " " + billUpdated.getCustomer().getLastName(),
                billUpdated.getBillNumber(),
                details.stream().map(detail -> new BillItemResponse(
                        detail.getId(),
                        detail.getProduct().getId(),
                        detail.getProduct().getName(),
                        detail.getQuantity(),
                        detail.getUnitPrice(),
                        detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()))
                )).toList()
        );
    }

    public void delete(Long id) {
        var bill = billRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        if (!billDetailRepository.findAllByEnabledIsTrueAndBill_Id(id).isEmpty()) {
            throw new InputEnumException("Cannot delete bill. Bill has details.");
        }
        // TODO: no delete status for PAID or CANCELLED
        bill.setEnabled(false);
        bill.setDeletedAt(LocalDateTime.now());
        billRepository.save(bill);
    }


    private BillStatus parseStatus(String status) {
        try {
            return BillStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InputEnumException("Invalid bill status: " + status);
        }
    }

    private PaymentMethod parsePaymentMethod(String paymentMethod) {
        try {
            return PaymentMethod.valueOf(paymentMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InputEnumException("Invalid payment method: " + paymentMethod);
        }
    }

    private String generateBillNumber() {
        var count = billRepository.count() + 1;
        return String.format("FAC-%08d", count);
    }
}





