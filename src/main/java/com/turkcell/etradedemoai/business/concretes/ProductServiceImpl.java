package com.turkcell.etradedemoai.business.concretes;

import com.turkcell.etradedemoai.business.abstracts.ProductService;
import com.turkcell.etradedemoai.business.dtos.requests.product.CreateProductRequest;
import com.turkcell.etradedemoai.business.dtos.requests.product.UpdateProductRequest;
import com.turkcell.etradedemoai.business.dtos.responses.product.CreateProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.DeleteProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetAllProductsResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.UpdateProductResponse;
import com.turkcell.etradedemoai.business.rules.ProductBusinessRules;
import com.turkcell.etradedemoai.dataAccess.ProductRepository;
import com.turkcell.etradedemoai.dataAccess.CategoryRepository;
import com.turkcell.etradedemoai.entities.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductBusinessRules productBusinessRules;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductBusinessRules productBusinessRules) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productBusinessRules = productBusinessRules;
    }

    @Override
    @Transactional
    public CreateProductResponse create(CreateProductRequest request) {
        // Business rules validation
        productBusinessRules.checkIfProductNameExists(request.getName());
        productBusinessRules.checkIfProductPriceIsValid(request.getUnitPrice());
        productBusinessRules.checkIfProductStockIsValid(request.getUnitsInStock());
        
        Product entity = toEntity(request);
        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(entity::setCategory);
        }
        Product saved = productRepository.save(entity);
        CreateProductResponse resp = new CreateProductResponse();
        resp.setId(saved.getId());
        resp.setName(saved.getName());
        resp.setUnitPrice(saved.getUnitPrice());
        resp.setUnitsInStock(saved.getUnitsInStock());
        resp.setDescription(saved.getDescription());
        resp.setCreatedDate(saved.getCreatedDate());
        if (saved.getCategory() != null) {
            resp.setCategoryId(saved.getCategory().getId());
            resp.setCategoryName(saved.getCategory().getName());
        }
        return resp;
    }

    @Override
    public Optional<GetProductResponse> getById(Long id) {
        return productRepository.findById(id).map(this::toGetResponse);
    }

    @Override
    public GetAllProductsResponse getAll() {
        List<GetProductResponse> items = productRepository.findAll().stream()
            .map(this::toGetResponse)
            .collect(Collectors.toList());
        return new GetAllProductsResponse(items);
    }

    @Override
    @Transactional
    public UpdateProductResponse update(Long id, UpdateProductRequest request) {
        // Business rules validation
        Product existing = productBusinessRules.checkIfProductExists(id);
        productBusinessRules.checkIfProductNameExistsForUpdate(id, request.getName());
        productBusinessRules.checkIfProductPriceIsValid(request.getUnitPrice());
        productBusinessRules.checkIfProductStockIsValid(request.getUnitsInStock());
        
        existing.setName(request.getName());
        existing.setUnitPrice(request.getUnitPrice());
        existing.setUnitsInStock(request.getUnitsInStock());
        existing.setDescription(request.getDescription());
        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(existing::setCategory);
        }
        Product saved = productRepository.save(existing);
        return new UpdateProductResponse(
            saved.getId(),
            saved.getName(),
            saved.getUnitPrice(),
            saved.getUnitsInStock(),
            saved.getDescription(),
            saved.getUpdatedDate()
        );
    }

    @Override
    @Transactional
    public DeleteProductResponse deleteById(Long id) {
        // Business rule validation
        productBusinessRules.checkIfProductExists(id);
        
        productRepository.deleteById(id);
        return new DeleteProductResponse(true, "Deleted");
    }

    private Product toEntity(CreateProductRequest request) {
        Product p = new Product();
        p.setName(request.getName());
        p.setUnitPrice(request.getUnitPrice());
        p.setUnitsInStock(request.getUnitsInStock());
        p.setDescription(request.getDescription());
        return p;
    }

    private GetProductResponse toGetResponse(Product p) {
        Long catId = null;
        String catName = null;
        if (p.getCategory() != null) {
            catId = p.getCategory().getId();
            catName = p.getCategory().getName();
        }
        return new GetProductResponse(
            p.getId(),
            p.getName(),
            p.getUnitPrice(),
            p.getUnitsInStock(),
            p.getDescription(),
            p.getCreatedDate(),
            p.getUpdatedDate(),
            p.getDeletedDate(),
            catId,
            catName
        );
    }
}
