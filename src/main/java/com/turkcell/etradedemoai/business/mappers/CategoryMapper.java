package com.turkcell.etradedemoai.business.mappers;

import com.turkcell.etradedemoai.business.dtos.requests.category.CreateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.responses.category.CreateCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.UpdateCategoryResponse;
import com.turkcell.etradedemoai.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for Category entity and DTOs.
 * Automatically generates implementation at compile-time.
 * 
 * Features:
 * - Spring component integration (@Mapper(componentModel = "spring"))
 * - Type-safe compile-time code generation
 * - Null-safe operations
 * - Simplified mapping (no complex nested objects like Product)
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Maps Category entity to CreateCategoryResponse DTO.
     * Direct field mapping (id, name, createdDate).
     */
    CreateCategoryResponse toCreateResponse(Category category);

    /**
     * Maps Category entity to GetCategoryResponse DTO.
     * Direct field mapping (id, name, createdDate).
     */
    GetCategoryResponse toGetResponse(Category category);

    /**
     * Maps CreateCategoryRequest DTO to Category entity.
     * Ignores fields that should not be set from request (id, timestamps, products).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "deletedDate", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CreateCategoryRequest request);

    /**
     * Maps list of Category entities to list of GetCategoryResponse DTOs.
     * Useful for getAll() operations.
     */
    List<GetCategoryResponse> toGetResponseList(List<Category> categories);

    /**
     * Maps Category entity to UpdateCategoryResponse DTO.
     * Direct field mapping (id, name, updatedDate).
     */
    UpdateCategoryResponse toUpdateResponse(Category category);
}
