package com.turkcell.etradedemoai.business.dtos.responses.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for a newly created product")
public class CreateProductResponse {
    @Schema(description = "Unique identifier of the created product", example = "1")
    private Long id;
    
    @Schema(description = "Name of the product", example = "Laptop")
    private String name;
    
    @Schema(description = "Unit price of the product", example = "999.99")
    private BigDecimal unitPrice;
    
    @Schema(description = "Number of units in stock", example = "50")
    private Integer unitsInStock;
    
    @Schema(description = "Detailed description of the product", example = "High-performance laptop with 16GB RAM")
    private String description;
    
    @Schema(description = "Timestamp when the product was created", example = "2025-11-10T10:15:30Z")
    private Instant createdDate;

    @Schema(description = "Category id of the product", example = "1")
    private Long categoryId;

    @Schema(description = "Category name of the product", example = "Electronics")
    private String categoryName;

    public CreateProductResponse() {
    }

    public CreateProductResponse(Long id, String name, BigDecimal unitPrice, Integer unitsInStock, String description, Instant createdDate) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.description = description;
        this.createdDate = createdDate;
    }

    public CreateProductResponse(Long id, String name, BigDecimal unitPrice, Integer unitsInStock, String description, Instant createdDate, Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.description = description;
        this.createdDate = createdDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateProductResponse that = (CreateProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(unitsInStock, that.unitsInStock) && Objects.equals(description, that.description) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitPrice, unitsInStock, description, createdDate, categoryId, categoryName);
    }
}
