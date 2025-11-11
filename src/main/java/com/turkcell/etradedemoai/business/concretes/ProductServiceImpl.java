package com.turkcell.etradedemoai.business.concretes;

import com.turkcell.etradedemoai.business.abstracts.ProductService;
import com.turkcell.etradedemoai.business.dtos.requests.product.CreateProductRequest;
import com.turkcell.etradedemoai.business.dtos.requests.product.UpdateProductRequest;
import com.turkcell.etradedemoai.business.dtos.responses.product.CreateProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.DeleteProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetAllProductsResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.UpdateProductResponse;
import com.turkcell.etradedemoai.business.mappers.ProductMapper;
import com.turkcell.etradedemoai.business.rules.ProductBusinessRules;
import com.turkcell.etradedemoai.dataAccess.ProductRepository;
import com.turkcell.etradedemoai.entities.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductBusinessRules productBusinessRules;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
            ProductRepository productRepository, 
            ProductBusinessRules productBusinessRules,
            ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productBusinessRules = productBusinessRules;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public CreateProductResponse create(CreateProductRequest request) {
        // Business rules validation
        productBusinessRules.checkIfProductNameExists(request.getName());
        productBusinessRules.checkIfProductPriceIsValid(request.getUnitPrice());
        productBusinessRules.checkIfProductStockIsValid(request.getUnitsInStock());
        
        Product entity = productMapper.toEntity(request);
        productBusinessRules.getCategoryIfExists(request.getCategoryId())
            .ifPresent(entity::setCategory);
        Product saved = productRepository.save(entity);
        return productMapper.toCreateResponse(saved);
    }

    @Override
    public Optional<GetProductResponse> getById(Long id) {
        return productRepository.findById(id).map(productMapper::toGetResponse);
    }

    @Override
    public GetAllProductsResponse getAll() {
        List<GetProductResponse> items = productMapper.toGetResponseList(productRepository.findAll());
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
        productBusinessRules.getCategoryIfExists(request.getCategoryId())
            .ifPresent(existing::setCategory);
        Product saved = productRepository.save(existing);
        return productMapper.toUpdateResponse(saved);
    }

    @Override
    @Transactional
    public DeleteProductResponse deleteById(Long id) {
        // Business rule validation
        productBusinessRules.checkIfProductExists(id);
        
        productRepository.deleteById(id);
        return new DeleteProductResponse(true, "Deleted");
    }
}
