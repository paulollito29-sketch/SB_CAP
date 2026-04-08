package com.example.service;

import com.example.dto.ProductRequest;
import com.example.dto.ProductResponse;
import com.example.entity.ProductEntity;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // list
    public List<ProductResponse> findAll() {
        return productRepository.findAllByEnabledIsTrueOrderByIdDesc()
                .stream().map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory().getName() + " - " + product.getCategory().getDescription()
                )).toList();
    }

    // create
    public ProductResponse create(ProductRequest dto) {
        var category = categoryRepository.findByEnabledIsTrueAndId(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (productRepository.existsByEnabledIsTrueAndNameIgnoreCase(dto.name())) {
            throw new ResourceAlreadyExistsException("Product with the same name already exists");
        }
        var productToSave = ProductEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .category(category)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        var productSaved = productRepository.save(productToSave);
        return new ProductResponse(
                productSaved.getId(),
                productSaved.getName(),
                productSaved.getDescription(),
                productSaved.getPrice(),
                productSaved.getCategory().getName()
        );
    }

    // find one
    public ProductResponse findOne(Long id) {
        var product = productRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getName()
        );
    }

    // update
    public ProductResponse update(Long id, ProductRequest dto) {
        var product = productRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        var category = categoryRepository.findByEnabledIsTrueAndId(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (productRepository.existsByEnabledIsTrueAndNameIgnoreCaseAndIdNot(dto.name(), id)) {
            throw new ResourceAlreadyExistsException("Product with the same name already exists");
        }
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now());
        var productUpdated = productRepository.save(product);
        return new ProductResponse(
                productUpdated.getId(),
                productUpdated.getName(),
                productUpdated.getDescription(),
                productUpdated.getPrice(),
                productUpdated.getCategory().getName()
        );
    }

    // delete
    public void delete(Long id) {
        var product = productRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setEnabled(false);
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

}
