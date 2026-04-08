package com.example.repository;

import com.example.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAllByEnabledIsTrueOrderByIdDesc();

    Optional<CategoryEntity> findByEnabledIsTrueAndId(Long id);

    boolean existsByEnabledIsTrueAndNameIgnoreCase(String name);

    boolean existsByEnabledIsTrueAndNameIgnoreCaseAndIdNot(String name, Long id);

}
