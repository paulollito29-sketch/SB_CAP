package com.example.repository;

import com.example.entity.BillDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillDetailRepository extends JpaRepository<BillDetailEntity, Long> {

    List<BillDetailEntity> findAllByEnabledIsTrueAndBill_Id(Long billId);

    Optional<BillDetailEntity> findByEnabledIsTrueAndId(Long id);

    List<BillDetailEntity> findAllByEnabledIsTrueAndProduct_Id(Long productId);

}
