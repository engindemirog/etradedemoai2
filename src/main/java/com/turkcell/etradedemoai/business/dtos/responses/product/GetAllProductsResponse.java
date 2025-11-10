package com.turkcell.etradedemoai.business.dtos.responses.product;

import java.util.List;
import java.util.Objects;

public class GetAllProductsResponse {
    private List<GetProductResponse> items;

    public GetAllProductsResponse() {
    }

    public GetAllProductsResponse(List<GetProductResponse> items) {
        this.items = items;
    }

    public List<GetProductResponse> getItems() {
        return items;
    }

    public void setItems(List<GetProductResponse> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetAllProductsResponse that = (GetAllProductsResponse) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
