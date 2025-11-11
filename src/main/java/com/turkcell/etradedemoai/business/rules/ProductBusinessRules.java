package com.turkcell.etradedemoai.business.rules;

import com.turkcell.etradedemoai.common.BusinessException;
import com.turkcell.etradedemoai.dataAccess.ProductRepository;
import com.turkcell.etradedemoai.dataAccess.CategoryRepository;
import com.turkcell.etradedemoai.entities.Product;
import com.turkcell.etradedemoai.entities.Category;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Business rules for Product entity operations.
 * Encapsulates validation and business logic to maintain SOLID principles.
 */
@Service
public class ProductBusinessRules {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductBusinessRules(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Rule 1: Product name must be unique.
     * Prevents duplicate product names in the system.
     */
    public void checkIfProductNameExists(String name) {
        boolean exists = productRepository.existsByNameIgnoreCase(name);
        if (exists) {
            throw new BusinessException("Product with name '" + name + "' already exists", "PRODUCT_NAME_EXISTS");
        }
    }

    /**
     * Rule 2: Product name must be unique when updating (except current product).
     * Ensures no other product has the same name during updates.
     */
    public void checkIfProductNameExistsForUpdate(Long productId, String name) {
        boolean exists = productRepository.existsByNameIgnoreCaseAndIdNot(name, productId);
        if (exists) {
            throw new BusinessException("Another product with name '" + name + "' already exists", "PRODUCT_NAME_EXISTS");
        }
    }

    /**
     * Rule 3: Product price must be positive.
     * Ensures business integrity by preventing zero or negative prices.
     */
    public void checkIfProductPriceIsValid(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Product price must be greater than zero", "INVALID_PRODUCT_PRICE");
        }
    }

    /**
     * Rule 4: Product stock quantity must not be negative.
     * Ensures stock integrity.
     */
    public void checkIfProductStockIsValid(Integer unitsInStock) {
        if (unitsInStock == null || unitsInStock < 0) {
            throw new BusinessException("Product stock cannot be negative", "INVALID_PRODUCT_STOCK");
        }
    }

    /**
     * Rule 5: Product must exist before performing operations.
     * Validates product existence for update/delete operations.
     */
    public Product checkIfProductExists(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Product not found with id: " + id, "PRODUCT_NOT_FOUND"));
    }

    /**
     * Rule 6: Returns category if exists, otherwise returns empty Optional.
     * Used when assigning category to product.
     */
    public Optional<Category> getCategoryIfExists(Long categoryId) {
        if (categoryId == null) {
            return Optional.empty();
        }
        return categoryRepository.findById(categoryId);
    }

    /**
     * Helper: Extract category ID safely from product.
     */
    public Long extractCategoryId(Product product) {
        return product.getCategory() != null ? product.getCategory().getId() : null;
    }

    /**
     * Helper: Extract category name safely from product.
     */
    public String extractCategoryName(Product product) {
        return product.getCategory() != null ? product.getCategory().getName() : null;
    }
}