package com.turkcell.etradedemoai.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Product entity representing a product in the catalog.
 */
@Entity
@Table(name = "products")
public class Product extends com.turkcell.etradedemoai.common.BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "units_in_stock", nullable = false)
    private Integer unitsInStock;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    public Product(String name, BigDecimal unitPrice, Integer unitsInStock, String description) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.description = description;
    }

    public Product(String name, BigDecimal unitPrice, Integer unitsInStock, String description, Category category) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.description = description;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        Long id = getId();
        Long otherId = product.getId();
        if (id != null && otherId != null) {
            return Objects.equals(id, otherId);
        }
        return Objects.equals(name, product.name) && Objects.equals(unitPrice, product.unitPrice) && Objects.equals(unitsInStock, product.unitsInStock) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        Long id = getId();
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(name, unitPrice, unitsInStock, description);
    }
}
