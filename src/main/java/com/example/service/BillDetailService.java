package com.example.service;

import com.example.dto.BillDetailRequest;
import com.example.dto.BillDetailResponse;
import com.example.entity.BillDetailEntity;
import com.example.entity.BillEntity;
import com.example.entity.BillStatus;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.BillDetailRepository;
import com.example.repository.BillRepository;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillDetailService {

    private final BillDetailRepository billDetailRepository;
    private final ProductRepository productRepository;
    private final BillRepository billRepository;

    public BillDetailService(BillDetailRepository billDetailRepository,
                             ProductRepository productRepository,
                             BillRepository billRepository) {
        this.billDetailRepository = billDetailRepository;
        this.productRepository = productRepository;
        this.billRepository = billRepository;
    }

    @Transactional(readOnly = true)
    public List<BillDetailEntity> findAllByBillId(Long billId) {
        return billDetailRepository.findAllByEnabledIsTrueAndBill_Id(billId);
    }

    @Transactional
    public BillDetailEntity saveDetail(BillEntity bill, Long productId, Integer quantity) {
        var product = productRepository.findByEnabledIsTrueAndId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + productId + " not found"));
        var detailToSave = BillDetailEntity.builder()
                .bill(bill)
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        return billDetailRepository.save(detailToSave);
    }

    public BillDetailResponse update(Long id, BillDetailRequest dto) {
        var detail = billDetailRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill detail with id: " + id + " not found"));
        if (!detail.getBill().getStatus().equals(BillStatus.PENDING)){
            throw new ResourceAlreadyExistsException("Only details of pending bills can be updated, status: " + detail.getBill().getStatus());
        }
        var product = productRepository.findByEnabledIsTrueAndId(dto.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + dto.productId() + " not found"));

        var bill = detail.getBill();
        detail.setProduct(product);
        detail.setQuantity(dto.quantity());
        detail.setUnitPrice(product.getPrice());
        detail.setUpdatedAt(LocalDateTime.now());
        var updatedDetail = billDetailRepository.save(detail);
        var details = billDetailRepository.findAllByEnabledIsTrueAndBill_Id(bill.getId());
        var grandTotal = BigDecimal.ZERO;
        for (var d : details) {
            var subTotal = d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity()));
            grandTotal = grandTotal.add(subTotal);
        }
        bill.setTotal(grandTotal);
        billRepository.save(bill);
        return new BillDetailResponse(
                updatedDetail.getId(),
                updatedDetail.getProduct().getName(),
                updatedDetail.getQuantity()
        );

    }

    public void delete(Long id) {
        var detail = billDetailRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill detail with id: " + id + " not found"));
        if (!detail.getBill().getStatus().equals(BillStatus.PENDING)){
            throw new ResourceAlreadyExistsException("Only details of pending bills can be deleted, status: " + detail.getBill().getStatus());
        }

        detail.setEnabled(false);
        detail.setDeletedAt(LocalDateTime.now());
        billDetailRepository.save(detail);
    }

}
