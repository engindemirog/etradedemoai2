package com.turkcell.etradedemoai.business.dtos.responses.category;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

@Schema(description = "Response containing list of categories")
public class GetAllCategoriesResponse {
    private List<GetCategoryResponse> items;

    public GetAllCategoriesResponse() {
    }

    public GetAllCategoriesResponse(List<GetCategoryResponse> items) {
        this.items = items;
    }

    public List<GetCategoryResponse> getItems() {
        return items;
    }

    public void setItems(List<GetCategoryResponse> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetAllCategoriesResponse that = (GetAllCategoriesResponse) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
