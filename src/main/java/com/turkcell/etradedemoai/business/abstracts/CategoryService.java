package com.turkcell.etradedemoai.business.abstracts;

import com.turkcell.etradedemoai.business.dtos.requests.category.CreateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.requests.category.UpdateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.responses.category.CreateCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.DeleteCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetAllCategoriesResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.UpdateCategoryResponse;

import java.util.Optional;

public interface CategoryService {

    CreateCategoryResponse create(CreateCategoryRequest request);

    Optional<GetCategoryResponse> getById(Long id);

    GetAllCategoriesResponse getAll();

    UpdateCategoryResponse update(Long id, UpdateCategoryRequest request);

    DeleteCategoryResponse deleteById(Long id);
}
