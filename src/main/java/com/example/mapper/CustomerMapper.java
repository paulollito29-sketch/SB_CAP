package com.example.mapper;

import com.example.dto.CustomerRequest;
import com.example.dto.CustomerResponse;
import com.example.entity.CustomerEntity;

import java.time.LocalDateTime;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponse toResponse(CustomerEntity entity) {
        return new CustomerResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail()
        );
    }

    public static CustomerEntity toEntity(CustomerRequest dto) {
        return CustomerEntity.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CustomerEntity toUpdate(CustomerEntity entity, CustomerRequest dto) {
        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setEmail(dto.email());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public static CustomerEntity toDelete(CustomerEntity entity) {
        entity.setEnabled(false);
        entity.setDeletedAt(LocalDateTime.now());
        return entity;
    }
}
