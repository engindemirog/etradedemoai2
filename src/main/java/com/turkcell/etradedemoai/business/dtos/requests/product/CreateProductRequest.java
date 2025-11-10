package com.turkcell.etradedemoai.business.dtos.requests.product;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating a new product")
public class CreateProductRequest {
    @Schema(description = "Name of the product", example = "Laptop")
    private String name;
    
    @Schema(description = "Unit price of the product", example = "999.99")
    private BigDecimal unitPrice;
    
    @Schema(description = "Number of units in stock", example = "50")
    private Integer unitsInStock;
    
    @Schema(description = "Detailed description of the product", example = "High-performance laptop with 16GB RAM")
    private String description;

    @Schema(description = "Category id the product belongs to", example = "1")
    private Long categoryId;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String name, BigDecimal unitPrice, Integer unitsInStock, String description) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.description = description;
    }

    public CreateProductRequest(String name, BigDecimal unitPrice, Integer unitsInStock, String description, Long categoryId) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.description = description;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(Integer unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
