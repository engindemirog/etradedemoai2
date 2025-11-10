package com.turkcell.etradedemoai.api.controllers;

import com.turkcell.etradedemoai.business.abstracts.ProductService;
import com.turkcell.etradedemoai.business.dtos.requests.product.CreateProductRequest;
import com.turkcell.etradedemoai.business.dtos.requests.product.UpdateProductRequest;
import com.turkcell.etradedemoai.business.dtos.responses.product.CreateProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.DeleteProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetAllProductsResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.UpdateProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created",
            content = @Content(schema = @Schema(implementation = CreateProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CreateProductResponse> create(
        @RequestBody @Parameter(description = "Product details", required = true) CreateProductRequest request) {
        CreateProductResponse saved = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + saved.getId()))
            .body(saved);
    }

    @Operation(summary = "Get all products", description = "Returns a list of all available products")
    @ApiResponse(responseCode = "200", description = "List of products retrieved",
        content = @Content(schema = @Schema(implementation = GetAllProductsResponse.class)))
    @GetMapping
    public ResponseEntity<GetAllProductsResponse> getAll() {
        GetAllProductsResponse list = productService.getAll();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get a product by ID", description = "Returns a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(schema = @Schema(implementation = GetProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetProductResponse> getById(
        @Parameter(description = "ID of the product", required = true) @PathVariable Long id) {
        return productService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a product", description = "Updates an existing product with new details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated",
            content = @Content(schema = @Schema(implementation = UpdateProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UpdateProductResponse> update(
        @Parameter(description = "ID of the product to update", required = true) @PathVariable Long id,
        @Parameter(description = "Updated product details", required = true) @RequestBody UpdateProductRequest request) {
        UpdateProductResponse updated = productService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteProductResponse> delete(
        @Parameter(description = "ID of the product to delete", required = true) @PathVariable Long id) {
        DeleteProductResponse resp = productService.deleteById(id);
        if (!resp.isSuccess()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
