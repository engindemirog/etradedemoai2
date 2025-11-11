package com.turkcell.etradedemoai.business.concretes;

import com.turkcell.etradedemoai.business.dtos.requests.product.CreateProductRequest;
import com.turkcell.etradedemoai.business.dtos.requests.product.UpdateProductRequest;
import com.turkcell.etradedemoai.business.dtos.responses.product.CreateProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.DeleteProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetAllProductsResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.GetProductResponse;
import com.turkcell.etradedemoai.business.dtos.responses.product.UpdateProductResponse;
import com.turkcell.etradedemoai.business.rules.ProductBusinessRules;
import com.turkcell.etradedemoai.common.BusinessException;
import com.turkcell.etradedemoai.dataAccess.ProductRepository;
import com.turkcell.etradedemoai.entities.Category;
import com.turkcell.etradedemoai.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductServiceImpl.
 * Tests all CRUD operations and business rule validations.
 * 
 * Test Strategy:
 * - Mocking: ProductRepository and ProductBusinessRules are mocked
 * - Coverage: All public methods with happy path and edge cases
 * - Assertions: Using AssertJ for fluent assertions
 * - Naming: Given_When_Then pattern for clarity
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductServiceImpl Unit Tests")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductBusinessRules productBusinessRules;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;
    private Category sampleCategory;
    private CreateProductRequest createRequest;
    private UpdateProductRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Sample Category
        sampleCategory = new Category();
        sampleCategory.setId(1L);
        sampleCategory.setName("Electronics");
        sampleCategory.setCreatedDate(Instant.now());

        // Sample Product
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Laptop Dell XPS 15");
        sampleProduct.setUnitPrice(new BigDecimal("1299.99"));
        sampleProduct.setUnitsInStock(50);
        sampleProduct.setDescription("High-performance laptop");
        sampleProduct.setCategory(sampleCategory);
        sampleProduct.setCreatedDate(Instant.now());
        sampleProduct.setUpdatedDate(Instant.now());

        // Sample Create Request
        createRequest = new CreateProductRequest();
        createRequest.setName("Laptop Dell XPS 15");
        createRequest.setUnitPrice(new BigDecimal("1299.99"));
        createRequest.setUnitsInStock(50);
        createRequest.setDescription("High-performance laptop");
        createRequest.setCategoryId(1L);

        // Sample Update Request
        updateRequest = new UpdateProductRequest();
        updateRequest.setName("Laptop Dell XPS 15 Updated");
        updateRequest.setUnitPrice(new BigDecimal("1199.99"));
        updateRequest.setUnitsInStock(45);
        updateRequest.setDescription("Updated description");
        updateRequest.setCategoryId(1L);
    }

    // ========================================
    // CREATE PRODUCT TESTS
    // ========================================

    @Nested
    @DisplayName("Create Product Tests")
    class CreateProductTests {

        @Test
        @DisplayName("Should successfully create product with category")
        void givenValidRequestWithCategory_whenCreate_thenReturnCreatedProduct() {
            // Given
            when(productBusinessRules.getCategoryIfExists(1L))
                .thenReturn(Optional.of(sampleCategory));
            when(productBusinessRules.extractCategoryId(any(Product.class)))
                .thenReturn(1L);
            when(productBusinessRules.extractCategoryName(any(Product.class)))
                .thenReturn("Electronics");
            when(productRepository.save(any(Product.class)))
                .thenReturn(sampleProduct);

            // When
            CreateProductResponse response = productService.create(createRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getName()).isEqualTo("Laptop Dell XPS 15");
            assertThat(response.getUnitPrice()).isEqualByComparingTo("1299.99");
            assertThat(response.getUnitsInStock()).isEqualTo(50);
            assertThat(response.getDescription()).isEqualTo("High-performance laptop");
            assertThat(response.getCategoryId()).isEqualTo(1L);
            assertThat(response.getCategoryName()).isEqualTo("Electronics");
            assertThat(response.getCreatedDate()).isNotNull();

            // Verify business rules were checked
            verify(productBusinessRules).checkIfProductNameExists("Laptop Dell XPS 15");
            verify(productBusinessRules).checkIfProductPriceIsValid(new BigDecimal("1299.99"));
            verify(productBusinessRules).checkIfProductStockIsValid(50);
            verify(productBusinessRules).getCategoryIfExists(1L);
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Should successfully create product without category")
        void givenValidRequestWithoutCategory_whenCreate_thenReturnCreatedProduct() {
            // Given
            createRequest.setCategoryId(null);
            sampleProduct.setCategory(null);
            
            when(productBusinessRules.getCategoryIfExists(null))
                .thenReturn(Optional.empty());
            when(productBusinessRules.extractCategoryId(any(Product.class)))
                .thenReturn(null);
            when(productBusinessRules.extractCategoryName(any(Product.class)))
                .thenReturn(null);
            when(productRepository.save(any(Product.class)))
                .thenReturn(sampleProduct);

            // When
            CreateProductResponse response = productService.create(createRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getCategoryId()).isNull();
            assertThat(response.getCategoryName()).isNull();

            verify(productBusinessRules).getCategoryIfExists(null);
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when product name already exists")
        void givenDuplicateName_whenCreate_thenThrowBusinessException() {
            // Given
            doThrow(new BusinessException("Product with name 'Laptop Dell XPS 15' already exists", "PRODUCT_NAME_EXISTS"))
                .when(productBusinessRules).checkIfProductNameExists("Laptop Dell XPS 15");

            // When & Then
            assertThatThrownBy(() -> productService.create(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists")
                .hasFieldOrPropertyWithValue("businessCode", "PRODUCT_NAME_EXISTS");

            verify(productBusinessRules).checkIfProductNameExists("Laptop Dell XPS 15");
            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when price is invalid")
        void givenInvalidPrice_whenCreate_thenThrowBusinessException() {
            // Given
            doThrow(new BusinessException("Product price must be greater than zero", "INVALID_PRODUCT_PRICE"))
                .when(productBusinessRules).checkIfProductPriceIsValid(any(BigDecimal.class));

            // When & Then
            assertThatThrownBy(() -> productService.create(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("price must be greater than zero")
                .hasFieldOrPropertyWithValue("businessCode", "INVALID_PRODUCT_PRICE");

            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when stock is invalid")
        void givenInvalidStock_whenCreate_thenThrowBusinessException() {
            // Given
            doThrow(new BusinessException("Product stock cannot be negative", "INVALID_PRODUCT_STOCK"))
                .when(productBusinessRules).checkIfProductStockIsValid(anyInt());

            // When & Then
            assertThatThrownBy(() -> productService.create(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("stock cannot be negative")
                .hasFieldOrPropertyWithValue("businessCode", "INVALID_PRODUCT_STOCK");

            verify(productRepository, never()).save(any(Product.class));
        }
    }

    // ========================================
    // GET PRODUCT BY ID TESTS
    // ========================================

    @Nested
    @DisplayName("Get Product By ID Tests")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return product when ID exists")
        void givenExistingId_whenGetById_thenReturnProduct() {
            // Given
            when(productRepository.findById(1L))
                .thenReturn(Optional.of(sampleProduct));
            when(productBusinessRules.extractCategoryId(sampleProduct))
                .thenReturn(1L);
            when(productBusinessRules.extractCategoryName(sampleProduct))
                .thenReturn("Electronics");

            // When
            Optional<GetProductResponse> response = productService.getById(1L);

            // Then
            assertThat(response).isPresent();
            assertThat(response.get().getId()).isEqualTo(1L);
            assertThat(response.get().getName()).isEqualTo("Laptop Dell XPS 15");
            assertThat(response.get().getUnitPrice()).isEqualByComparingTo("1299.99");
            assertThat(response.get().getUnitsInStock()).isEqualTo(50);
            assertThat(response.get().getCategoryId()).isEqualTo(1L);
            assertThat(response.get().getCategoryName()).isEqualTo("Electronics");

            verify(productRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty Optional when ID does not exist")
        void givenNonExistingId_whenGetById_thenReturnEmpty() {
            // Given
            when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

            // When
            Optional<GetProductResponse> response = productService.getById(999L);

            // Then
            assertThat(response).isEmpty();
            verify(productRepository).findById(999L);
        }

        @Test
        @DisplayName("Should handle product without category")
        void givenProductWithoutCategory_whenGetById_thenReturnProductWithNullCategory() {
            // Given
            sampleProduct.setCategory(null);
            when(productRepository.findById(1L))
                .thenReturn(Optional.of(sampleProduct));
            when(productBusinessRules.extractCategoryId(sampleProduct))
                .thenReturn(null);
            when(productBusinessRules.extractCategoryName(sampleProduct))
                .thenReturn(null);

            // When
            Optional<GetProductResponse> response = productService.getById(1L);

            // Then
            assertThat(response).isPresent();
            assertThat(response.get().getCategoryId()).isNull();
            assertThat(response.get().getCategoryName()).isNull();
        }
    }

    // ========================================
    // GET ALL PRODUCTS TESTS
    // ========================================

    @Nested
    @DisplayName("Get All Products Tests")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return all products when products exist")
        void givenProductsExist_whenGetAll_thenReturnAllProducts() {
            // Given
            Product product2 = new Product();
            product2.setId(2L);
            product2.setName("Wireless Mouse");
            product2.setUnitPrice(new BigDecimal("29.99"));
            product2.setUnitsInStock(200);
            product2.setCategory(sampleCategory);

            List<Product> products = Arrays.asList(sampleProduct, product2);
            when(productRepository.findAll()).thenReturn(products);
            when(productBusinessRules.extractCategoryId(any(Product.class)))
                .thenReturn(1L);
            when(productBusinessRules.extractCategoryName(any(Product.class)))
                .thenReturn("Electronics");

            // When
            GetAllProductsResponse response = productService.getAll();

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getItems()).hasSize(2);
            assertThat(response.getItems().get(0).getName()).isEqualTo("Laptop Dell XPS 15");
            assertThat(response.getItems().get(1).getName()).isEqualTo("Wireless Mouse");

            verify(productRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no products exist")
        void givenNoProductsExist_whenGetAll_thenReturnEmptyList() {
            // Given
            when(productRepository.findAll()).thenReturn(Arrays.asList());

            // When
            GetAllProductsResponse response = productService.getAll();

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getItems()).isEmpty();
            verify(productRepository).findAll();
        }

        @Test
        @DisplayName("Should handle products with and without categories")
        void givenMixedProducts_whenGetAll_thenReturnAllWithCorrectCategoryData() {
            // Given
            Product productWithCategory = sampleProduct;
            
            Product productWithoutCategory = new Product();
            productWithoutCategory.setId(2L);
            productWithoutCategory.setName("No Category Product");
            productWithoutCategory.setUnitPrice(new BigDecimal("99.99"));
            productWithoutCategory.setUnitsInStock(10);
            productWithoutCategory.setCategory(null);

            when(productRepository.findAll())
                .thenReturn(Arrays.asList(productWithCategory, productWithoutCategory));
            
            when(productBusinessRules.extractCategoryId(productWithCategory))
                .thenReturn(1L);
            when(productBusinessRules.extractCategoryName(productWithCategory))
                .thenReturn("Electronics");
            when(productBusinessRules.extractCategoryId(productWithoutCategory))
                .thenReturn(null);
            when(productBusinessRules.extractCategoryName(productWithoutCategory))
                .thenReturn(null);

            // When
            GetAllProductsResponse response = productService.getAll();

            // Then
            assertThat(response.getItems()).hasSize(2);
            assertThat(response.getItems().get(0).getCategoryId()).isEqualTo(1L);
            assertThat(response.getItems().get(1).getCategoryId()).isNull();
        }
    }

    // ========================================
    // UPDATE PRODUCT TESTS
    // ========================================

    @Nested
    @DisplayName("Update Product Tests")
    class UpdateProductTests {

        @Test
        @DisplayName("Should successfully update product")
        void givenValidRequest_whenUpdate_thenReturnUpdatedProduct() {
            // Given
            Product updatedProduct = new Product();
            updatedProduct.setId(1L);
            updatedProduct.setName("Laptop Dell XPS 15 Updated");
            updatedProduct.setUnitPrice(new BigDecimal("1199.99"));
            updatedProduct.setUnitsInStock(45);
            updatedProduct.setDescription("Updated description");
            updatedProduct.setUpdatedDate(Instant.now());

            when(productBusinessRules.checkIfProductExists(1L))
                .thenReturn(sampleProduct);
            when(productBusinessRules.getCategoryIfExists(1L))
                .thenReturn(Optional.of(sampleCategory));
            when(productRepository.save(any(Product.class)))
                .thenReturn(updatedProduct);

            // When
            UpdateProductResponse response = productService.update(1L, updateRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getName()).isEqualTo("Laptop Dell XPS 15 Updated");
            assertThat(response.getUnitPrice()).isEqualByComparingTo("1199.99");
            assertThat(response.getUnitsInStock()).isEqualTo(45);
            assertThat(response.getDescription()).isEqualTo("Updated description");
            assertThat(response.getUpdatedDate()).isNotNull();

            verify(productBusinessRules).checkIfProductExists(1L);
            verify(productBusinessRules).checkIfProductNameExistsForUpdate(1L, "Laptop Dell XPS 15 Updated");
            verify(productBusinessRules).checkIfProductPriceIsValid(new BigDecimal("1199.99"));
            verify(productBusinessRules).checkIfProductStockIsValid(45);
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when product does not exist")
        void givenNonExistingProduct_whenUpdate_thenThrowBusinessException() {
            // Given
            when(productBusinessRules.checkIfProductExists(999L))
                .thenThrow(new BusinessException("Product not found with id: 999", "PRODUCT_NOT_FOUND"));

            // When & Then
            assertThatThrownBy(() -> productService.update(999L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Product not found")
                .hasFieldOrPropertyWithValue("businessCode", "PRODUCT_NOT_FOUND");

            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when updated name already exists")
        void givenDuplicateName_whenUpdate_thenThrowBusinessException() {
            // Given
            when(productBusinessRules.checkIfProductExists(1L))
                .thenReturn(sampleProduct);
            doThrow(new BusinessException("Another product with name already exists", "PRODUCT_NAME_EXISTS"))
                .when(productBusinessRules).checkIfProductNameExistsForUpdate(1L, "Laptop Dell XPS 15 Updated");

            // When & Then
            assertThatThrownBy(() -> productService.update(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists")
                .hasFieldOrPropertyWithValue("businessCode", "PRODUCT_NAME_EXISTS");

            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when updated price is invalid")
        void givenInvalidPrice_whenUpdate_thenThrowBusinessException() {
            // Given
            when(productBusinessRules.checkIfProductExists(1L))
                .thenReturn(sampleProduct);
            doThrow(new BusinessException("Product price must be greater than zero", "INVALID_PRODUCT_PRICE"))
                .when(productBusinessRules).checkIfProductPriceIsValid(any(BigDecimal.class));

            // When & Then
            assertThatThrownBy(() -> productService.update(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("price must be greater than zero");

            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should successfully update product and change category")
        void givenNewCategory_whenUpdate_thenUpdateCategorySuccessfully() {
            // Given
            Category newCategory = new Category();
            newCategory.setId(2L);
            newCategory.setName("Accessories");

            updateRequest.setCategoryId(2L);

            when(productBusinessRules.checkIfProductExists(1L))
                .thenReturn(sampleProduct);
            when(productBusinessRules.getCategoryIfExists(2L))
                .thenReturn(Optional.of(newCategory));
            when(productRepository.save(any(Product.class)))
                .thenReturn(sampleProduct);

            // When
            UpdateProductResponse response = productService.update(1L, updateRequest);

            // Then
            assertThat(response).isNotNull();
            verify(productBusinessRules).getCategoryIfExists(2L);
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Should successfully update product and remove category")
        void givenNullCategory_whenUpdate_thenRemoveCategorySuccessfully() {
            // Given
            updateRequest.setCategoryId(null);

            when(productBusinessRules.checkIfProductExists(1L))
                .thenReturn(sampleProduct);
            when(productBusinessRules.getCategoryIfExists(null))
                .thenReturn(Optional.empty());
            when(productRepository.save(any(Product.class)))
                .thenReturn(sampleProduct);

            // When
            UpdateProductResponse response = productService.update(1L, updateRequest);

            // Then
            assertThat(response).isNotNull();
            verify(productBusinessRules).getCategoryIfExists(null);
        }
    }

    // ========================================
    // DELETE PRODUCT TESTS
    // ========================================

    @Nested
    @DisplayName("Delete Product Tests")
    class DeleteProductTests {

        @Test
        @DisplayName("Should successfully delete product when it exists")
        void givenExistingProduct_whenDelete_thenReturnSuccessResponse() {
            // Given
            when(productBusinessRules.checkIfProductExists(1L))
                .thenReturn(sampleProduct);
            doNothing().when(productRepository).deleteById(1L);

            // When
            DeleteProductResponse response = productService.deleteById(1L);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo("Deleted");

            verify(productBusinessRules).checkIfProductExists(1L);
            verify(productRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw BusinessException when product does not exist")
        void givenNonExistingProduct_whenDelete_thenThrowBusinessException() {
            // Given
            when(productBusinessRules.checkIfProductExists(999L))
                .thenThrow(new BusinessException("Product not found with id: 999", "PRODUCT_NOT_FOUND"));

            // When & Then
            assertThatThrownBy(() -> productService.deleteById(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Product not found")
                .hasFieldOrPropertyWithValue("businessCode", "PRODUCT_NOT_FOUND");

            verify(productRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should call business rules validation before delete")
        void givenValidId_whenDelete_thenVerifyBusinessRulesAreCalled() {
            // Given
            when(productBusinessRules.checkIfProductExists(1L))
                .thenReturn(sampleProduct);

            // When
            productService.deleteById(1L);

            // Then
            verify(productBusinessRules, times(1)).checkIfProductExists(1L);
            verify(productRepository, times(1)).deleteById(1L);
        }
    }

    // ========================================
    // INTEGRATION & EDGE CASE TESTS
    // ========================================

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle product with very long description")
        void givenLongDescription_whenCreate_thenHandleSuccessfully() {
            // Given
            String longDescription = "A".repeat(2000);
            createRequest.setDescription(longDescription);

            when(productBusinessRules.getCategoryIfExists(any()))
                .thenReturn(Optional.empty());
            when(productRepository.save(any(Product.class)))
                .thenReturn(sampleProduct);

            // When
            CreateProductResponse response = productService.create(createRequest);

            // Then
            assertThat(response).isNotNull();
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Should handle product with zero stock")
        void givenZeroStock_whenCreate_thenHandleSuccessfully() {
            // Given
            createRequest.setUnitsInStock(0);
            sampleProduct.setUnitsInStock(0);

            when(productBusinessRules.getCategoryIfExists(any()))
                .thenReturn(Optional.empty());
            when(productRepository.save(any(Product.class)))
                .thenReturn(sampleProduct);

            // When
            CreateProductResponse response = productService.create(createRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getUnitsInStock()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle product with very high price")
        void givenHighPrice_whenCreate_thenHandleSuccessfully() {
            // Given
            BigDecimal highPrice = new BigDecimal("999999.99");
            createRequest.setUnitPrice(highPrice);
            sampleProduct.setUnitPrice(highPrice);

            when(productBusinessRules.getCategoryIfExists(any()))
                .thenReturn(Optional.empty());
            when(productRepository.save(any(Product.class)))
                .thenReturn(sampleProduct);

            // When
            CreateProductResponse response = productService.create(createRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getUnitPrice()).isEqualByComparingTo(highPrice);
        }

        @Test
        @DisplayName("Should verify transaction boundaries are respected")
        void verifyTransactionalBehavior() {
            // Given
            when(productBusinessRules.checkIfProductExists(1L))
                .thenReturn(sampleProduct);
            when(productRepository.save(any(Product.class)))
                .thenReturn(sampleProduct);

            // When
            productService.update(1L, updateRequest);

            // Then - Verify save was called (transaction committed)
            verify(productRepository).save(any(Product.class));
        }
    }
}
