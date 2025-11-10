package com.turkcell.etradedemoai.business.concretes;

import com.turkcell.etradedemoai.business.abstracts.CategoryService;
import com.turkcell.etradedemoai.business.dtos.requests.category.CreateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.requests.category.UpdateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.responses.category.CreateCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.DeleteCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetAllCategoriesResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.UpdateCategoryResponse;
import com.turkcell.etradedemoai.business.rules.CategoryBusinessRules;
import com.turkcell.etradedemoai.dataAccess.CategoryRepository;
import com.turkcell.etradedemoai.entities.Category;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryBusinessRules categoryBusinessRules;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryBusinessRules categoryBusinessRules) {
        this.categoryRepository = categoryRepository;
        this.categoryBusinessRules = categoryBusinessRules;
    }

    @Override
    @Transactional
    public CreateCategoryResponse create(CreateCategoryRequest request) {
        // Business rules validation
        categoryBusinessRules.checkIfCategoryNameIsValid(request.getName());
        categoryBusinessRules.checkIfCategoryNameExists(request.getName());
        
        Category c = new Category();
        c.setName(request.getName());
        Category saved = categoryRepository.save(c);
        CreateCategoryResponse resp = new CreateCategoryResponse();
        resp.setId(saved.getId());
        resp.setName(saved.getName());
        resp.setCreatedDate(saved.getCreatedDate());
        return resp;
    }

    @Override
    public Optional<GetCategoryResponse> getById(Long id) {
        return categoryRepository.findById(id).map(this::toGetResponse);
    }

    @Override
    public GetAllCategoriesResponse getAll() {
        List<GetCategoryResponse> items = categoryRepository.findAll().stream()
            .map(this::toGetResponse)
            .collect(Collectors.toList());
        return new GetAllCategoriesResponse(items);
    }

    @Override
    @Transactional
    public UpdateCategoryResponse update(Long id, UpdateCategoryRequest request) {
        // Business rules validation
        Category existing = categoryBusinessRules.checkIfCategoryExists(id);
        categoryBusinessRules.checkIfCategoryNameIsValid(request.getName());
        categoryBusinessRules.checkIfCategoryNameExistsForUpdate(id, request.getName());
        
        existing.setName(request.getName());
        Category saved = categoryRepository.save(existing);
        return new UpdateCategoryResponse(saved.getId(), saved.getName(), saved.getUpdatedDate());
    }

    @Override
    @Transactional
    public DeleteCategoryResponse deleteById(Long id) {
        // Business rules validation
        categoryBusinessRules.checkIfCategoryExists(id);
        categoryBusinessRules.checkIfCategoryHasProducts(id);
        
        categoryRepository.deleteById(id);
        return new DeleteCategoryResponse(true, "Deleted");
    }

    private GetCategoryResponse toGetResponse(Category c) {
        return new GetCategoryResponse(c.getId(), c.getName(), c.getCreatedDate());
    }
}
