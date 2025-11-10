package com.turkcell.etradedemoai.business.dtos.requests.product;

import java.math.BigDecimal;

public class UpdateProductRequest {
    private String name;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private String description;
    private Long categoryId;

    public UpdateProductRequest() {
    }

    public UpdateProductRequest(String name, BigDecimal unitPrice, Integer unitsInStock, String description) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.description = description;
    }

    public UpdateProductRequest(String name, BigDecimal unitPrice, Integer unitsInStock, String description, Long categoryId) {
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
