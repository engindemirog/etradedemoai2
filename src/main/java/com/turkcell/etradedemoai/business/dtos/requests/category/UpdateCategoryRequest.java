package com.turkcell.etradedemoai.business.dtos.requests.category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to update an existing category")
public class UpdateCategoryRequest {
    @Schema(description = "Name of the category", example = "Electronics")
    private String name;

    public UpdateCategoryRequest() {
    }

    public UpdateCategoryRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
