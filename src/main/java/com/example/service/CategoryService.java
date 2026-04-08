package com.example.service;

import com.example.dto.CategoryRequest;
import com.example.dto.CategoryResponse;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAllByEnabledIsTrueOrderByIdDesc()
                .stream()
                //.map(category -> CategoryMapper.toResponse(category))
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse create(CategoryRequest dto) {
        if (categoryRepository.existsByEnabledIsTrueAndNameIgnoreCase(dto.name())) {
            throw new ResourceAlreadyExistsException("Category with the same name already exists");
        }
        var categoryToSave = CategoryMapper.toSave(dto);
        var categorySaved = categoryRepository.save(categoryToSave);
        return CategoryMapper.toResponse(categorySaved);
    }

    public CategoryResponse findOne(Long id) {
        return categoryRepository.findByEnabledIsTrueAndId(id)
                .map(CategoryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public CategoryResponse update(Long id, CategoryRequest dto) {
        var category = categoryRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (categoryRepository.existsByEnabledIsTrueAndNameIgnoreCaseAndIdNot(dto.name(), id)) {
            throw new ResourceAlreadyExistsException("Category with the same name already exists");
        }
        var categoryToUpdate = CategoryMapper.toUpdate(category, dto);
        var categoryUpdated = categoryRepository.save(categoryToUpdate);
        return CategoryMapper.toResponse(categoryUpdated);
    }

    public void delete(Long id) {
        var category = categoryRepository.findByEnabledIsTrueAndId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (productRepository.findAllByEnabledIsTrueAndCategory_Id(id).size() > 0) {
            throw new ResourceAlreadyExistsException("Category has products, cannot be deleted");
        }
        var categoryToDelete = CategoryMapper.toDelete(category);
        categoryRepository.save(categoryToDelete);
    }

}
