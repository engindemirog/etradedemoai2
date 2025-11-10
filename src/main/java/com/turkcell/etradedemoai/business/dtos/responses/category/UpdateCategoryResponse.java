package com.turkcell.etradedemoai.business.dtos.responses.category;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Objects;

@Schema(description = "Response after updating a category")
public class UpdateCategoryResponse {
    @Schema(description = "Category id", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Electronics")
    private String name;

    @Schema(description = "Updated timestamp")
    private Instant updatedDate;

    public UpdateCategoryResponse() {
    }

    public UpdateCategoryResponse(Long id, String name, Instant updatedDate) {
        this.id = id;
        this.name = name;
        this.updatedDate = updatedDate;
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

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateCategoryResponse that = (UpdateCategoryResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, updatedDate);
    }
}
