package com.turkcell.etradedemoai.business.abstracts;

import com.turkcell.etradedemoai.business.dtos.requests.product.CreateProductRequest;
import com.turkcell.etradedemoai.business.dtos.requests.product.UpdateProductRequest;
import com.turkcell.etradedemoai.business.dtos.responses.product.CreateProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.DeleteProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetAllProductsResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.UpdateProductResponse;
import java.util.Optional;

public interface ProductService {

    CreateProductResponse create(CreateProductRequest request);

    Optional<GetProductResponse> getById(Long id);

    GetAllProductsResponse getAll();

    UpdateProductResponse update(Long id, UpdateProductRequest request);

    DeleteProductResponse deleteById(Long id);
}
