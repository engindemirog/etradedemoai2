package com.turkcell.etradedemoai.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.turkcell.etradedemoai.entities.Product;

/**
 * Spring Data repository for Product entities.
 * No custom operations are defined — use standard CRUD methods from JpaRepository.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    boolean existsByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    
    long countByCategoryId(Long categoryId);
}