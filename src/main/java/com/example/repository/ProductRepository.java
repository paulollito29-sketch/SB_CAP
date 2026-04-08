package com.example.repository;

import com.example.entity.CategoryEntity;
import com.example.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAllByEnabledIsTrueOrderByIdDesc();

    boolean existsByEnabledIsTrueAndNameIgnoreCase(String name);

    Optional<ProductEntity> findByEnabledIsTrueAndId(Long id);

    boolean existsByEnabledIsTrueAndNameIgnoreCaseAndIdNot(String name, Long id);

    // TODO: method for delete CategoryService valid :categoryId
    List<ProductEntity> findAllByEnabledIsTrueAndCategory_Id(Long categoryId);

}
