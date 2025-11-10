package com.turkcell.etradedemoai.api.controllers;

import com.turkcell.etradedemoai.business.abstracts.CategoryService;
import com.turkcell.etradedemoai.business.dtos.requests.category.CreateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.requests.category.UpdateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.responses.category.CreateCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.DeleteCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetAllCategoriesResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.UpdateCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a category")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Category created")})
    @PostMapping
    public ResponseEntity<CreateCategoryResponse> create(@org.springframework.web.bind.annotation.RequestBody CreateCategoryRequest request) {
        CreateCategoryResponse saved = categoryService.create(request);
        return ResponseEntity.created(URI.create("/api/categories/" + saved.getId())).body(saved);
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<GetAllCategoriesResponse> getAll() {
        GetAllCategoriesResponse list = categoryService.getAll();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    public ResponseEntity<GetCategoryResponse> getById(@PathVariable Long id) {
        return categoryService.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a category")
    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryResponse> update(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody UpdateCategoryRequest request) {
        UpdateCategoryResponse updated = categoryService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteCategoryResponse> delete(@PathVariable Long id) {
        DeleteCategoryResponse resp = categoryService.deleteById(id);
        if (!resp.isSuccess()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
