package com.turkcell.etradedemoai.business.mappers;

import com.turkcell.etradedemoai.business.dtos.requests.product.CreateProductRequest;
import com.turkcell.etradedemoai.business.dtos.responses.product.CreateProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.UpdateProductResponse;
import com.turkcell.etradedemoai.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * MapStruct mapper for Product entity and DTOs.
 * Automatically generates implementation at compile-time.
 * 
 * Features:
 * - Spring component integration (@Mapper(componentModel = "spring"))
 * - Type-safe compile-time code generation
 * - Custom mapping methods for category fields
 * - Null-safe operations
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Maps Product entity to CreateProductResponse DTO.
     * Extracts categoryId and categoryName from nested Category object.
     */
    @Mapping(target = "categoryId", source = "product", qualifiedByName = "extractCategoryId")
    @Mapping(target = "categoryName", source = "product", qualifiedByName = "extractCategoryName")
    CreateProductResponse toCreateResponse(Product product);

    /**
     * Maps Product entity to GetProductResponse DTO.
     * Extracts categoryId and categoryName from nested Category object.
     */
    @Mapping(target = "categoryId", source = "product", qualifiedByName = "extractCategoryId")
    @Mapping(target = "categoryName", source = "product", qualifiedByName = "extractCategoryName")
    GetProductResponse toGetResponse(Product product);

    /**
     * Maps CreateProductRequest DTO to Product entity.
     * Ignores fields that should not be set from request (id, category, timestamps).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "deletedDate", ignore = true)
    Product toEntity(CreateProductRequest request);

    /**
     * Maps list of Product entities to list of GetProductResponse DTOs.
     * Useful for getAll() operations.
     */
    List<GetProductResponse> toGetResponseList(List<Product> products);

    /**
     * Maps Product entity to UpdateProductResponse DTO.
     */
    UpdateProductResponse toUpdateResponse(Product product);

    /**
     * Custom mapping method to safely extract category ID from Product.
     * Returns null if category is null.
     */
    @Named("extractCategoryId")
    default Long extractCategoryId(Product product) {
        return product != null && product.getCategory() != null 
            ? product.getCategory().getId() 
            : null;
    }

    /**
     * Custom mapping method to safely extract category name from Product.
     * Returns null if category is null.
     */
    @Named("extractCategoryName")
    default String extractCategoryName(Product product) {
        return product != null && product.getCategory() != null 
            ? product.getCategory().getName() 
            : null;
    }
}
