package com.turkcell.etradedemoai.business.rules;

import com.turkcell.etradedemoai.dataAccess.CategoryRepository;
import com.turkcell.etradedemoai.dataAccess.ProductRepository;
import com.turkcell.etradedemoai.entities.Category;
import org.springframework.stereotype.Service;

/**
 * Business rules for Category entity operations.
 */
@Service
public class CategoryBusinessRules {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryBusinessRules(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Rule 1: Category name must be unique.
     */
    public void checkIfCategoryNameExists(String name) {
        boolean exists = categoryRepository.existsByNameIgnoreCase(name);
        if (exists) {
            throw new IllegalArgumentException("Category with name '" + name + "' already exists");
        }
    }

    /**
     * Rule 2: Category name must be unique when updating.
     */
    public void checkIfCategoryNameExistsForUpdate(Long categoryId, String name) {
        boolean exists = categoryRepository.existsByNameIgnoreCaseAndIdNot(name, categoryId);
        if (exists) {
            throw new IllegalArgumentException("Another category with name '" + name + "' already exists");
        }
    }

    /**
     * Rule 3: Category must exist before operations.
     */
    public Category checkIfCategoryExists(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }

    /**
     * Rule 4: Category name cannot be empty or blank.
     */
    public void checkIfCategoryNameIsValid(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (name.length() < 2) {
            throw new IllegalArgumentException("Category name must be at least 2 characters long");
        }
    }

    /**
     * Rule 5: Category cannot be deleted if it has associated products.
     */
    public void checkIfCategoryHasProducts(Long categoryId) {
        long productCount = productRepository.countByCategoryId(categoryId);
        if (productCount > 0) {
            throw new IllegalArgumentException("Category cannot be deleted because it has " + productCount + " associated product(s)");
        }
    }
}
