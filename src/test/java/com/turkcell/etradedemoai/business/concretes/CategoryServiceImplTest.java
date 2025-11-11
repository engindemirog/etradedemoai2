package com.turkcell.etradedemoai.business.concretes;

import com.turkcell.etradedemoai.business.dtos.requests.category.CreateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.requests.category.UpdateCategoryRequest;
import com.turkcell.etradedemoai.business.dtos.responses.category.CreateCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.DeleteCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetAllCategoriesResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.GetCategoryResponse;
import com.turkcell.etradedemoai.business.dtos.responses.category.UpdateCategoryResponse;
import com.turkcell.etradedemoai.business.rules.CategoryBusinessRules;
import com.turkcell.etradedemoai.common.BusinessException;
import com.turkcell.etradedemoai.dataAccess.CategoryRepository;
import com.turkcell.etradedemoai.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CategoryServiceImpl.
 * Tests all CRUD operations and business rule validations.
 * 
 * Test Strategy:
 * - Mocking: CategoryRepository and CategoryBusinessRules are mocked
 * - Coverage: All public methods with happy path and edge cases
 * - Assertions: Using AssertJ for fluent assertions
 * - Naming: Given_When_Then pattern for clarity
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryServiceImpl Unit Tests")
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryBusinessRules categoryBusinessRules;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category sampleCategory;
    private CreateCategoryRequest createRequest;
    private UpdateCategoryRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Sample Category
        sampleCategory = new Category();
        sampleCategory.setId(1L);
        sampleCategory.setName("Electronics");
        sampleCategory.setCreatedDate(Instant.now());
        sampleCategory.setUpdatedDate(Instant.now());

        // Sample Create Request
        createRequest = new CreateCategoryRequest();
        createRequest.setName("Electronics");

        // Sample Update Request
        updateRequest = new UpdateCategoryRequest();
        updateRequest.setName("Electronics & Gadgets");
    }

    // ========================================
    // CREATE CATEGORY TESTS
    // ========================================

    @Nested
    @DisplayName("Create Category Tests")
    class CreateCategoryTests {

        @Test
        @DisplayName("Should successfully create category with valid name")
        void givenValidRequest_whenCreate_thenReturnCreatedCategory() {
            // Given
            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            CreateCategoryResponse response = categoryService.create(createRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getName()).isEqualTo("Electronics");
            assertThat(response.getCreatedDate()).isNotNull();

            // Verify business rules were checked
            verify(categoryBusinessRules).checkIfCategoryNameIsValid("Electronics");
            verify(categoryBusinessRules).checkIfCategoryNameExists("Electronics");
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when category name is invalid")
        void givenInvalidName_whenCreate_thenThrowBusinessException() {
            // Given
            createRequest.setName("");
            doThrow(new BusinessException("Category name cannot be empty", "INVALID_CATEGORY_NAME"))
                .when(categoryBusinessRules).checkIfCategoryNameIsValid("");

            // When & Then
            assertThatThrownBy(() -> categoryService.create(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("cannot be empty")
                .hasFieldOrPropertyWithValue("businessCode", "INVALID_CATEGORY_NAME");

            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when category name already exists")
        void givenDuplicateName_whenCreate_thenThrowBusinessException() {
            // Given
            doThrow(new BusinessException("Category with name 'Electronics' already exists", "CATEGORY_NAME_EXISTS"))
                .when(categoryBusinessRules).checkIfCategoryNameExists("Electronics");

            // When & Then
            assertThatThrownBy(() -> categoryService.create(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists")
                .hasFieldOrPropertyWithValue("businessCode", "CATEGORY_NAME_EXISTS");

            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when category name is too short")
        void givenTooShortName_whenCreate_thenThrowBusinessException() {
            // Given
            createRequest.setName("A");
            doThrow(new BusinessException("Category name must be at least 2 characters long", "INVALID_CATEGORY_NAME"))
                .when(categoryBusinessRules).checkIfCategoryNameIsValid("A");

            // When & Then
            assertThatThrownBy(() -> categoryService.create(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("at least 2 characters")
                .hasFieldOrPropertyWithValue("businessCode", "INVALID_CATEGORY_NAME");

            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should call business rules in correct order")
        void givenValidRequest_whenCreate_thenCallBusinessRulesInOrder() {
            // Given
            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            categoryService.create(createRequest);

            // Then - Verify order of business rule calls
            var inOrder = inOrder(categoryBusinessRules, categoryRepository);
            inOrder.verify(categoryBusinessRules).checkIfCategoryNameIsValid("Electronics");
            inOrder.verify(categoryBusinessRules).checkIfCategoryNameExists("Electronics");
            inOrder.verify(categoryRepository).save(any(Category.class));
        }

        @Test
        @DisplayName("Should handle category with special characters in name")
        void givenNameWithSpecialChars_whenCreate_thenHandleSuccessfully() {
            // Given
            createRequest.setName("Electronics & Gadgets");
            sampleCategory.setName("Electronics & Gadgets");

            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            CreateCategoryResponse response = categoryService.create(createRequest);

            // Then
            assertThat(response.getName()).isEqualTo("Electronics & Gadgets");
            verify(categoryBusinessRules).checkIfCategoryNameIsValid("Electronics & Gadgets");
        }
    }

    // ========================================
    // GET CATEGORY BY ID TESTS
    // ========================================

    @Nested
    @DisplayName("Get Category By ID Tests")
    class GetCategoryByIdTests {

        @Test
        @DisplayName("Should return category when ID exists")
        void givenExistingId_whenGetById_thenReturnCategory() {
            // Given
            when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(sampleCategory));

            // When
            Optional<GetCategoryResponse> response = categoryService.getById(1L);

            // Then
            assertThat(response).isPresent();
            assertThat(response.get().getId()).isEqualTo(1L);
            assertThat(response.get().getName()).isEqualTo("Electronics");
            assertThat(response.get().getCreatedDate()).isNotNull();

            verify(categoryRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty Optional when ID does not exist")
        void givenNonExistingId_whenGetById_thenReturnEmpty() {
            // Given
            when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

            // When
            Optional<GetCategoryResponse> response = categoryService.getById(999L);

            // Then
            assertThat(response).isEmpty();
            verify(categoryRepository).findById(999L);
        }

        @Test
        @DisplayName("Should return category with all fields populated")
        void givenCategoryWithAllFields_whenGetById_thenReturnCompleteResponse() {
            // Given
            sampleCategory.setCreatedDate(Instant.parse("2025-11-11T10:00:00Z"));
            when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(sampleCategory));

            // When
            Optional<GetCategoryResponse> response = categoryService.getById(1L);

            // Then
            assertThat(response).isPresent();
            GetCategoryResponse categoryResponse = response.get();
            assertThat(categoryResponse.getId()).isEqualTo(1L);
            assertThat(categoryResponse.getName()).isEqualTo("Electronics");
            assertThat(categoryResponse.getCreatedDate()).isNotNull();
        }

        @Test
        @DisplayName("Should not call business rules for getById")
        void givenValidId_whenGetById_thenDoNotCallBusinessRules() {
            // Given
            when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(sampleCategory));

            // When
            categoryService.getById(1L);

            // Then
            verifyNoInteractions(categoryBusinessRules);
        }
    }

    // ========================================
    // GET ALL CATEGORIES TESTS
    // ========================================

    @Nested
    @DisplayName("Get All Categories Tests")
    class GetAllCategoriesTests {

        @Test
        @DisplayName("Should return all categories when categories exist")
        void givenCategoriesExist_whenGetAll_thenReturnAllCategories() {
            // Given
            Category category2 = new Category();
            category2.setId(2L);
            category2.setName("Books");
            category2.setCreatedDate(Instant.now());

            Category category3 = new Category();
            category3.setId(3L);
            category3.setName("Clothing");
            category3.setCreatedDate(Instant.now());

            List<Category> categories = Arrays.asList(sampleCategory, category2, category3);
            when(categoryRepository.findAll()).thenReturn(categories);

            // When
            GetAllCategoriesResponse response = categoryService.getAll();

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getItems()).hasSize(3);
            assertThat(response.getItems().get(0).getName()).isEqualTo("Electronics");
            assertThat(response.getItems().get(1).getName()).isEqualTo("Books");
            assertThat(response.getItems().get(2).getName()).isEqualTo("Clothing");

            verify(categoryRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no categories exist")
        void givenNoCategoriesExist_whenGetAll_thenReturnEmptyList() {
            // Given
            when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

            // When
            GetAllCategoriesResponse response = categoryService.getAll();

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getItems()).isEmpty();
            verify(categoryRepository).findAll();
        }

        @Test
        @DisplayName("Should return single category when only one exists")
        void givenOneCategoryExists_whenGetAll_thenReturnSingleCategory() {
            // Given
            when(categoryRepository.findAll()).thenReturn(Arrays.asList(sampleCategory));

            // When
            GetAllCategoriesResponse response = categoryService.getAll();

            // Then
            assertThat(response.getItems()).hasSize(1);
            assertThat(response.getItems().get(0).getName()).isEqualTo("Electronics");
        }

        @Test
        @DisplayName("Should map all category fields correctly")
        void givenMultipleCategories_whenGetAll_thenMapAllFieldsCorrectly() {
            // Given
            Category cat1 = new Category();
            cat1.setId(1L);
            cat1.setName("Category 1");
            cat1.setCreatedDate(Instant.parse("2025-11-11T10:00:00Z"));

            Category cat2 = new Category();
            cat2.setId(2L);
            cat2.setName("Category 2");
            cat2.setCreatedDate(Instant.parse("2025-11-11T11:00:00Z"));

            when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

            // When
            GetAllCategoriesResponse response = categoryService.getAll();

            // Then
            assertThat(response.getItems()).hasSize(2);
            assertThat(response.getItems().get(0).getId()).isEqualTo(1L);
            assertThat(response.getItems().get(0).getCreatedDate()).isNotNull();
            assertThat(response.getItems().get(1).getId()).isEqualTo(2L);
            assertThat(response.getItems().get(1).getCreatedDate()).isNotNull();
        }

        @Test
        @DisplayName("Should not call business rules for getAll")
        void whenGetAll_thenDoNotCallBusinessRules() {
            // Given
            when(categoryRepository.findAll()).thenReturn(Arrays.asList(sampleCategory));

            // When
            categoryService.getAll();

            // Then
            verifyNoInteractions(categoryBusinessRules);
        }
    }

    // ========================================
    // UPDATE CATEGORY TESTS
    // ========================================

    @Nested
    @DisplayName("Update Category Tests")
    class UpdateCategoryTests {

        @Test
        @DisplayName("Should successfully update category with valid data")
        void givenValidRequest_whenUpdate_thenReturnUpdatedCategory() {
            // Given
            Category updatedCategory = new Category();
            updatedCategory.setId(1L);
            updatedCategory.setName("Electronics & Gadgets");
            updatedCategory.setUpdatedDate(Instant.now());

            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            when(categoryRepository.save(any(Category.class)))
                .thenReturn(updatedCategory);

            // When
            UpdateCategoryResponse response = categoryService.update(1L, updateRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getName()).isEqualTo("Electronics & Gadgets");
            assertThat(response.getUpdatedDate()).isNotNull();

            verify(categoryBusinessRules).checkIfCategoryExists(1L);
            verify(categoryBusinessRules).checkIfCategoryNameIsValid("Electronics & Gadgets");
            verify(categoryBusinessRules).checkIfCategoryNameExistsForUpdate(1L, "Electronics & Gadgets");
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when category does not exist")
        void givenNonExistingCategory_whenUpdate_thenThrowBusinessException() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(999L))
                .thenThrow(new BusinessException("Category not found with id: 999", "CATEGORY_NOT_FOUND"));

            // When & Then
            assertThatThrownBy(() -> categoryService.update(999L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Category not found")
                .hasFieldOrPropertyWithValue("businessCode", "CATEGORY_NOT_FOUND");

            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when updated name is invalid")
        void givenInvalidName_whenUpdate_thenThrowBusinessException() {
            // Given
            updateRequest.setName("");
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            doThrow(new BusinessException("Category name cannot be empty", "INVALID_CATEGORY_NAME"))
                .when(categoryBusinessRules).checkIfCategoryNameIsValid("");

            // When & Then
            assertThatThrownBy(() -> categoryService.update(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("cannot be empty")
                .hasFieldOrPropertyWithValue("businessCode", "INVALID_CATEGORY_NAME");

            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when updated name already exists")
        void givenDuplicateName_whenUpdate_thenThrowBusinessException() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            doThrow(new BusinessException("Another category with name already exists", "CATEGORY_NAME_EXISTS"))
                .when(categoryBusinessRules).checkIfCategoryNameExistsForUpdate(1L, "Electronics & Gadgets");

            // When & Then
            assertThatThrownBy(() -> categoryService.update(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists")
                .hasFieldOrPropertyWithValue("businessCode", "CATEGORY_NAME_EXISTS");

            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should call business rules in correct order for update")
        void givenValidRequest_whenUpdate_thenCallBusinessRulesInOrder() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            categoryService.update(1L, updateRequest);

            // Then - Verify order
            var inOrder = inOrder(categoryBusinessRules, categoryRepository);
            inOrder.verify(categoryBusinessRules).checkIfCategoryExists(1L);
            inOrder.verify(categoryBusinessRules).checkIfCategoryNameIsValid("Electronics & Gadgets");
            inOrder.verify(categoryBusinessRules).checkIfCategoryNameExistsForUpdate(1L, "Electronics & Gadgets");
            inOrder.verify(categoryRepository).save(any(Category.class));
        }

        @Test
        @DisplayName("Should update category name properly")
        void givenNewName_whenUpdate_thenUpdateNameInEntity() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            categoryService.update(1L, updateRequest);

            // Then
            assertThat(sampleCategory.getName()).isEqualTo("Electronics & Gadgets");
        }

        @Test
        @DisplayName("Should handle update with same name")
        void givenSameName_whenUpdate_thenHandleSuccessfully() {
            // Given
            updateRequest.setName("Electronics"); // Same as original
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            UpdateCategoryResponse response = categoryService.update(1L, updateRequest);

            // Then
            assertThat(response).isNotNull();
            verify(categoryBusinessRules).checkIfCategoryNameExistsForUpdate(1L, "Electronics");
        }
    }

    // ========================================
    // DELETE CATEGORY TESTS
    // ========================================

    @Nested
    @DisplayName("Delete Category Tests")
    class DeleteCategoryTests {

        @Test
        @DisplayName("Should successfully delete category when it exists and has no products")
        void givenExistingCategoryWithNoProducts_whenDelete_thenReturnSuccessResponse() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            doNothing().when(categoryBusinessRules).checkIfCategoryHasProducts(1L);
            doNothing().when(categoryRepository).deleteById(1L);

            // When
            DeleteCategoryResponse response = categoryService.deleteById(1L);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo("Deleted");

            verify(categoryBusinessRules).checkIfCategoryExists(1L);
            verify(categoryBusinessRules).checkIfCategoryHasProducts(1L);
            verify(categoryRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw BusinessException when category does not exist")
        void givenNonExistingCategory_whenDelete_thenThrowBusinessException() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(999L))
                .thenThrow(new BusinessException("Category not found with id: 999", "CATEGORY_NOT_FOUND"));

            // When & Then
            assertThatThrownBy(() -> categoryService.deleteById(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Category not found")
                .hasFieldOrPropertyWithValue("businessCode", "CATEGORY_NOT_FOUND");

            verify(categoryRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw BusinessException when category has associated products")
        void givenCategoryWithProducts_whenDelete_thenThrowBusinessException() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            doThrow(new BusinessException("Category cannot be deleted because it has 5 associated product(s)", "CATEGORY_HAS_PRODUCTS"))
                .when(categoryBusinessRules).checkIfCategoryHasProducts(1L);

            // When & Then
            assertThatThrownBy(() -> categoryService.deleteById(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("has 5 associated product(s)")
                .hasFieldOrPropertyWithValue("businessCode", "CATEGORY_HAS_PRODUCTS");

            verify(categoryRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should call business rules in correct order for delete")
        void givenValidId_whenDelete_thenCallBusinessRulesInOrder() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            doNothing().when(categoryBusinessRules).checkIfCategoryHasProducts(1L);

            // When
            categoryService.deleteById(1L);

            // Then - Verify order
            var inOrder = inOrder(categoryBusinessRules, categoryRepository);
            inOrder.verify(categoryBusinessRules).checkIfCategoryExists(1L);
            inOrder.verify(categoryBusinessRules).checkIfCategoryHasProducts(1L);
            inOrder.verify(categoryRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should verify all business rules before deletion")
        void givenValidCategory_whenDelete_thenVerifyAllBusinessRules() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);

            // When
            categoryService.deleteById(1L);

            // Then
            verify(categoryBusinessRules, times(1)).checkIfCategoryExists(1L);
            verify(categoryBusinessRules, times(1)).checkIfCategoryHasProducts(1L);
            verify(categoryRepository, times(1)).deleteById(1L);
        }
    }

    // ========================================
    // INTEGRATION & EDGE CASE TESTS
    // ========================================

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle category with very long name")
        void givenLongName_whenCreate_thenHandleSuccessfully() {
            // Given
            String longName = "A".repeat(255);
            createRequest.setName(longName);
            sampleCategory.setName(longName);

            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            CreateCategoryResponse response = categoryService.create(createRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getName()).hasSize(255);
            verify(categoryBusinessRules).checkIfCategoryNameIsValid(longName);
        }

        @Test
        @DisplayName("Should handle category with unicode characters")
        void givenUnicodeName_whenCreate_thenHandleSuccessfully() {
            // Given
            createRequest.setName("Elektronik & Çözümler");
            sampleCategory.setName("Elektronik & Çözümler");

            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            CreateCategoryResponse response = categoryService.create(createRequest);

            // Then
            assertThat(response.getName()).isEqualTo("Elektronik & Çözümler");
        }

        @Test
        @DisplayName("Should handle category with minimum valid name length")
        void givenMinimumLengthName_whenCreate_thenHandleSuccessfully() {
            // Given
            createRequest.setName("AB"); // Minimum 2 characters
            sampleCategory.setName("AB");

            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            CreateCategoryResponse response = categoryService.create(createRequest);

            // Then
            assertThat(response.getName()).isEqualTo("AB");
            verify(categoryBusinessRules).checkIfCategoryNameIsValid("AB");
        }

        @Test
        @DisplayName("Should handle category with numbers in name")
        void givenNameWithNumbers_whenCreate_thenHandleSuccessfully() {
            // Given
            createRequest.setName("Electronics 2025");
            sampleCategory.setName("Electronics 2025");

            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            CreateCategoryResponse response = categoryService.create(createRequest);

            // Then
            assertThat(response.getName()).isEqualTo("Electronics 2025");
        }

        @Test
        @DisplayName("Should verify transaction boundaries are respected")
        void verifyTransactionalBehavior() {
            // Given
            when(categoryBusinessRules.checkIfCategoryExists(1L))
                .thenReturn(sampleCategory);
            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            categoryService.update(1L, updateRequest);

            // Then - Verify save was called (transaction committed)
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        @DisplayName("Should handle multiple rapid create operations")
        void givenMultipleCreateRequests_whenCreate_thenHandleAllSuccessfully() {
            // Given
            CreateCategoryRequest req1 = new CreateCategoryRequest();
            req1.setName("Category 1");

            CreateCategoryRequest req2 = new CreateCategoryRequest();
            req2.setName("Category 2");

            Category cat1 = new Category();
            cat1.setId(1L);
            cat1.setName("Category 1");
            cat1.setCreatedDate(Instant.now());

            Category cat2 = new Category();
            cat2.setId(2L);
            cat2.setName("Category 2");
            cat2.setCreatedDate(Instant.now());

            when(categoryRepository.save(any(Category.class)))
                .thenReturn(cat1)
                .thenReturn(cat2);

            // When
            CreateCategoryResponse resp1 = categoryService.create(req1);
            CreateCategoryResponse resp2 = categoryService.create(req2);

            // Then
            assertThat(resp1.getName()).isEqualTo("Category 1");
            assertThat(resp2.getName()).isEqualTo("Category 2");
            verify(categoryRepository, times(2)).save(any(Category.class));
        }

        @Test
        @DisplayName("Should handle category name with leading/trailing spaces in business rules")
        void givenNameWithSpaces_whenCreate_thenBusinessRulesHandleIt() {
            // Given
            createRequest.setName("  Electronics  ");

            when(categoryRepository.save(any(Category.class)))
                .thenReturn(sampleCategory);

            // When
            categoryService.create(createRequest);

            // Then
            verify(categoryBusinessRules).checkIfCategoryNameIsValid("  Electronics  ");
        }
    }
}
