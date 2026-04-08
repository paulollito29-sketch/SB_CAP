package com.example.repository;

import com.example.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<BillEntity, Long> {

    List<BillEntity> findAllByEnabledIsTrueOrderByIdDesc();

    Optional<BillEntity> findByEnabledIsTrueAndId(Long id);

    List<BillEntity> findAllByEnabledIsTrueAndCustomer_Id(Long customerId);

}
