package com.turkcell.etradedemoai.business.rules;

import com.turkcell.etradedemoai.dataAccess.ProductRepository;
import com.turkcell.etradedemoai.entities.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Business rules for Product entity operations.
 * Encapsulates validation and business logic to maintain SOLID principles.
 */
@Service
public class ProductBusinessRules {

    private final ProductRepository productRepository;

    public ProductBusinessRules(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Rule 1: Product name must be unique.
     * Prevents duplicate product names in the system.
     */
    public void checkIfProductNameExists(String name) {
        boolean exists = productRepository.existsByNameIgnoreCase(name);
        if (exists) {
            throw new IllegalArgumentException("Product with name '" + name + "' already exists");
        }
    }

    /**
     * Rule 2: Product name must be unique when updating (except current product).
     * Ensures no other product has the same name during updates.
     */
    public void checkIfProductNameExistsForUpdate(Long productId, String name) {
        boolean exists = productRepository.existsByNameIgnoreCaseAndIdNot(name, productId);
        if (exists) {
            throw new IllegalArgumentException("Another product with name '" + name + "' already exists");
        }
    }

    /**
     * Rule 3: Product price must be positive.
     * Ensures business integrity by preventing zero or negative prices.
     */
    public void checkIfProductPriceIsValid(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
    }

    /**
     * Rule 4: Product stock quantity must not be negative.
     * Ensures stock integrity.
     */
    public void checkIfProductStockIsValid(Integer unitsInStock) {
        if (unitsInStock == null || unitsInStock < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
    }

    /**
     * Rule 5: Product must exist before performing operations.
     * Validates product existence for update/delete operations.
     */
    public Product checkIfProductExists(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }
}
