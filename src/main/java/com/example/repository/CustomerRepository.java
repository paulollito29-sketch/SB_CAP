package com.example.repository;

import com.example.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    // list
    List<CustomerEntity> findAllByEnabledIsTrueOrderByIdDesc();

    // create
    boolean existsByEnabledIsTrueAndEmailIgnoreCase(String email);

    // find by Id
    Optional<CustomerEntity> findByEnabledIsTrueAndId(Long id);

    // update
    boolean existsByEnabledIsTrueAndEmailIgnoreCaseAndIdNot(String email, Long id);

}
