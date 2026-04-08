package com.example.mapper;

import com.example.dto.CategoryRequest;
import com.example.dto.CategoryResponse;
import com.example.entity.CategoryEntity;

import java.time.LocalDateTime;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryEntity toSave(CategoryRequest dto) {
        return CategoryEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CategoryResponse toResponse(CategoryEntity entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }

    public static CategoryEntity toUpdate(CategoryEntity entity, CategoryRequest dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public static  CategoryEntity toDelete(CategoryEntity entity) {
        entity.setEnabled(false);
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
