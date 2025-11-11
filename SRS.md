# Software Requirements Specification (SRS)
## E-Trade Demo AI Application

**Version:** 1.0  
**Date:** November 11, 2025  
**Project:** etradedemoai  
**Organization:** Turkcell

---

## 1. Introduction

### 1.1 Purpose
This document specifies the software requirements for the E-Trade Demo AI application, a RESTful API-based e-commerce product catalog management system. The system provides CRUD operations for products and categories with comprehensive business rule validation.

### 1.2 Scope
The E-Trade Demo AI application is a backend REST API service that manages:
- Product catalog with pricing and inventory
- Product categories and their hierarchical relationships
- Business rule enforcement for data integrity
- Global error handling with RFC 7807 compliance

### 1.3 Definitions, Acronyms, and Abbreviations
- **API**: Application Programming Interface
- **REST**: Representational State Transfer
- **CRUD**: Create, Read, Update, Delete
- **DTO**: Data Transfer Object
- **JPA**: Java Persistence API
- **RFC 7807**: Problem Details for HTTP APIs
- **CORS**: Cross-Origin Resource Sharing
- **SOLID**: Single Responsibility, Open-Closed, Liskov Substitution, Interface Segregation, Dependency Inversion

### 1.4 References
- Spring Boot 3.5.7 Documentation
- OpenAPI/Swagger Specification 3.0
- RFC 7807 - Problem Details for HTTP APIs
- Hibernate 6.6.33 Documentation

---

## 2. Overall Description

### 2.1 Product Perspective
The system is a standalone Spring Boot application that provides:
- RESTful API endpoints for product and category management
- H2 embedded database for data persistence
- OpenAPI/Swagger documentation interface
- CORS-enabled architecture for cross-origin requests

### 2.2 Product Functions
The system provides the following major functions:

#### 2.2.1 Product Management
- Create new products with pricing and inventory details
- Retrieve product information by ID or list all products
- Update product details including category assignment
- Delete products from the catalog
- Enforce business rules for product data integrity

#### 2.2.2 Category Management
- Create product categories
- Retrieve category information by ID or list all categories
- Update category information
- Delete categories (with constraint validation)
- Enforce business rules for category data integrity

### 2.3 User Characteristics
Expected users include:
- Frontend developers integrating with the API
- E-commerce administrators managing product catalogs
- System integrators connecting external applications
- QA engineers testing API functionality

### 2.4 Constraints
- Must use Java 17
- Must use Spring Boot 3.5.7
- Must use H2 database for persistence
- Must follow REST API best practices
- Must implement SOLID principles in business logic
- Must provide RFC 7807 compliant error responses

### 2.5 Assumptions and Dependencies
- JDK 17 is available in the runtime environment
- Database file storage is available at ./data/etradedemo
- Network connectivity for API access
- JSON format for request/response payloads

---

## 3. Functional Requirements

### 3.1 Product Management Requirements

#### 3.1.1 Create Product (FR-P-01)
**Description:** System shall allow creation of new products with validation

**Input:**
- Product name (required, string)
- Unit price (required, BigDecimal)
- Units in stock (required, Integer)
- Description (optional, string, max 2000 chars)
- Category ID (optional, Long)

**Business Rules:**
- BR-P-01: Product name must be unique (case-insensitive)
- BR-P-02: Unit price must be greater than zero
- BR-P-03: Units in stock must not be negative
- BR-P-04: Category must exist if category ID is provided

**Output:**
- Created product with ID, timestamps, and category details
- HTTP 201 Created status
- Location header with resource URI

**Exception Cases:**
- Duplicate name → BusinessException (PRODUCT_NAME_EXISTS)
- Invalid price → BusinessException (INVALID_PRODUCT_PRICE)
- Negative stock → BusinessException (INVALID_PRODUCT_STOCK)

#### 3.1.2 Get Product by ID (FR-P-02)
**Description:** System shall retrieve product details by unique identifier

**Input:**
- Product ID (required, Long)

**Business Rules:**
- BR-P-05: Product must exist with given ID

**Output:**
- Product details including category information
- HTTP 200 OK status

**Exception Cases:**
- Product not found → BusinessException (PRODUCT_NOT_FOUND)

#### 3.1.3 Get All Products (FR-P-03)
**Description:** System shall retrieve list of all products

**Input:** None

**Output:**
- List of all products with category details
- HTTP 200 OK status
- Empty list if no products exist

#### 3.1.4 Update Product (FR-P-04)
**Description:** System shall update existing product with validation

**Input:**
- Product ID (required, Long)
- Product name (required, string)
- Unit price (required, BigDecimal)
- Units in stock (required, Integer)
- Description (optional, string)
- Category ID (optional, Long)

**Business Rules:**
- BR-P-05: Product must exist
- BR-P-06: Name must be unique excluding current product
- BR-P-02: Unit price must be greater than zero
- BR-P-03: Units in stock must not be negative
- BR-P-04: Category must exist if provided

**Output:**
- Updated product information
- HTTP 200 OK status

**Exception Cases:**
- Product not found → BusinessException (PRODUCT_NOT_FOUND)
- Duplicate name → BusinessException (PRODUCT_NAME_EXISTS)
- Invalid price → BusinessException (INVALID_PRODUCT_PRICE)
- Negative stock → BusinessException (INVALID_PRODUCT_STOCK)

#### 3.1.5 Delete Product (FR-P-05)
**Description:** System shall delete product from catalog

**Input:**
- Product ID (required, Long)

**Business Rules:**
- BR-P-05: Product must exist

**Output:**
- Success confirmation
- HTTP 204 No Content status

**Exception Cases:**
- Product not found → BusinessException (PRODUCT_NOT_FOUND)

---

### 3.2 Category Management Requirements

#### 3.2.1 Create Category (FR-C-01)
**Description:** System shall allow creation of new categories with validation

**Input:**
- Category name (required, string)

**Business Rules:**
- BR-C-01: Category name must be unique (case-insensitive)
- BR-C-02: Category name cannot be empty or blank
- BR-C-03: Category name must be at least 2 characters long

**Output:**
- Created category with ID and timestamp
- HTTP 201 Created status
- Location header with resource URI

**Exception Cases:**
- Duplicate name → BusinessException (CATEGORY_NAME_EXISTS)
- Invalid name → BusinessException (INVALID_CATEGORY_NAME)

#### 3.2.2 Get Category by ID (FR-C-02)
**Description:** System shall retrieve category details by unique identifier

**Input:**
- Category ID (required, Long)

**Business Rules:**
- BR-C-04: Category must exist with given ID

**Output:**
- Category details
- HTTP 200 OK status

**Exception Cases:**
- Category not found → BusinessException (CATEGORY_NOT_FOUND)

#### 3.2.3 Get All Categories (FR-C-03)
**Description:** System shall retrieve list of all categories

**Input:** None

**Output:**
- List of all categories
- HTTP 200 OK status
- Empty list if no categories exist

#### 3.2.4 Update Category (FR-C-04)
**Description:** System shall update existing category with validation

**Input:**
- Category ID (required, Long)
- Category name (required, string)

**Business Rules:**
- BR-C-04: Category must exist
- BR-C-05: Name must be unique excluding current category
- BR-C-02: Category name cannot be empty or blank
- BR-C-03: Category name must be at least 2 characters long

**Output:**
- Updated category information
- HTTP 200 OK status

**Exception Cases:**
- Category not found → BusinessException (CATEGORY_NOT_FOUND)
- Duplicate name → BusinessException (CATEGORY_NAME_EXISTS)
- Invalid name → BusinessException (INVALID_CATEGORY_NAME)

#### 3.2.5 Delete Category (FR-C-05)
**Description:** System shall delete category with constraint validation

**Input:**
- Category ID (required, Long)

**Business Rules:**
- BR-C-04: Category must exist
- BR-C-06: Category cannot be deleted if it has associated products

**Output:**
- Success confirmation
- HTTP 204 No Content status

**Exception Cases:**
- Category not found → BusinessException (CATEGORY_NOT_FOUND)
- Has products → BusinessException (CATEGORY_HAS_PRODUCTS)

---

### 3.3 Data Relationship Requirements

#### 3.3.1 Product-Category Relationship (FR-R-01)
**Description:** System shall maintain one-to-many relationship between Category and Product

**Rules:**
- One category can have multiple products
- One product can belong to zero or one category
- Category reference in product is optional
- Deleting category requires no products associated

---

### 3.4 Error Handling Requirements

#### 3.4.1 Business Exception Handling (FR-E-01)
**Description:** System shall provide structured error responses following RFC 7807

**Exception Response Format:**
```json
{
  "type": "string (error type URI)",
  "title": "string (error title)",
  "status": "integer (HTTP status code)",
  "detail": "string (detailed error message)",
  "instance": "string (request URI)",
  "businessCode": "string (business error code)",
  "timestamp": "datetime (error occurrence time)"
}
```

**Business Error Codes:**
- PRODUCT_NAME_EXISTS
- INVALID_PRODUCT_PRICE
- INVALID_PRODUCT_STOCK
- PRODUCT_NOT_FOUND
- CATEGORY_NAME_EXISTS
- INVALID_CATEGORY_NAME
- CATEGORY_NOT_FOUND
- CATEGORY_HAS_PRODUCTS

---

### 3.5 Cross-Cutting Requirements

#### 3.5.1 CORS Support (FR-CC-01)
**Description:** System shall allow cross-origin requests from all origins

**Configuration:**
- Allow all origin patterns (*)
- Support all HTTP methods (GET, POST, PUT, DELETE, PATCH, OPTIONS)
- Allow all headers
- Enable credentials
- Preflight cache: 3600 seconds
- Apply to all endpoints (**)

#### 3.5.2 API Documentation (FR-CC-02)
**Description:** System shall provide interactive API documentation

**Requirements:**
- OpenAPI 3.0 specification
- Swagger UI interface at /swagger-ui.html
- API docs JSON at /v3/api-docs
- Schema definitions for all DTOs
- Example requests and responses

#### 3.5.3 Audit Trail (FR-CC-03)
**Description:** System shall maintain audit timestamps for all entities

**Fields:**
- createdDate (auto-set on creation)
- updatedDate (auto-updated on modification)
- deletedDate (for soft delete support)

---

## 4. Non-Functional Requirements

### 4.1 Performance Requirements
- **NFR-01:** API response time shall not exceed 500ms for single record operations under normal load
- **NFR-02:** API shall support minimum 100 concurrent requests
- **NFR-03:** Database queries shall use proper indexing for name-based lookups

### 4.2 Reliability Requirements
- **NFR-04:** System shall use transactional operations for data consistency
- **NFR-05:** System shall validate all input data before persistence
- **NFR-06:** System shall handle database connection failures gracefully

### 4.3 Availability Requirements
- **NFR-07:** System shall be available 99.9% during business hours
- **NFR-08:** System shall support graceful shutdown without data loss

### 4.4 Security Requirements
- **NFR-09:** System shall validate all input parameters to prevent injection attacks
- **NFR-10:** System shall use prepared statements for all database queries
- **NFR-11:** System shall not expose internal error details in production

### 4.5 Maintainability Requirements
- **NFR-12:** Code shall follow SOLID principles
- **NFR-13:** Business rules shall be isolated in dedicated classes
- **NFR-14:** All public methods shall have Javadoc documentation
- **NFR-15:** Code shall use DTO pattern for API layer separation

### 4.6 Portability Requirements
- **NFR-16:** System shall run on any OS supporting Java 17
- **NFR-17:** Database shall use file-based H2 for easy deployment
- **NFR-18:** System shall be containerizable for cloud deployment

### 4.7 Usability Requirements
- **NFR-19:** API responses shall use clear, consistent JSON structure
- **NFR-20:** Error messages shall be descriptive and actionable
- **NFR-21:** API documentation shall include request/response examples

---

## 5. System Architecture

### 5.1 Layered Architecture
```
┌─────────────────────────────────────┐
│     API Layer (Controllers)         │
│  - ProductController                │
│  - CategoryController               │
├─────────────────────────────────────┤
│     Business Layer (Services)       │
│  - ProductServiceImpl               │
│  - CategoryServiceImpl              │
│  - ProductBusinessRules             │
│  - CategoryBusinessRules            │
├─────────────────────────────────────┤
│   Data Access Layer (Repositories)  │
│  - ProductRepository                │
│  - CategoryRepository               │
├─────────────────────────────────────┤
│     Entity Layer (Domain Model)     │
│  - Product                          │
│  - Category                         │
│  - BaseEntity                       │
├─────────────────────────────────────┤
│     Common Layer (Utilities)        │
│  - BusinessException                │
│  - CustomProblemDetail              │
│  - GlobalExceptionHandler           │
│  - CorsConfig                       │
└─────────────────────────────────────┘
```

### 5.2 Technology Stack
- **Framework:** Spring Boot 3.5.7
- **Language:** Java 17
- **Database:** H2 2.3.232 (file-based)
- **ORM:** Hibernate 6.6.33
- **Documentation:** springdoc-openapi 2.6.0
- **Build Tool:** Maven 3.x

---

## 6. Data Requirements

### 6.1 Entity: Product
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Unique identifier |
| name | String | NOT NULL, Unique (case-insensitive) | Product name |
| unitPrice | BigDecimal | NOT NULL, > 0 | Product price |
| unitsInStock | Integer | NOT NULL, >= 0 | Inventory quantity |
| description | String | Max 2000 chars | Product description |
| category_id | Long | FK to Category, Nullable | Associated category |
| createdDate | Instant | Auto-set | Creation timestamp |
| updatedDate | Instant | Auto-update | Last modification timestamp |
| deletedDate | Instant | Nullable | Soft delete timestamp |

### 6.2 Entity: Category
| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | Long | PK, Auto-increment | Unique identifier |
| name | String | NOT NULL, Unique (case-insensitive), Min 2 chars | Category name |
| createdDate | Instant | Auto-set | Creation timestamp |
| updatedDate | Instant | Auto-update | Last modification timestamp |
| deletedDate | Instant | Nullable | Soft delete timestamp |

### 6.3 Database Relationships
- **Product.category** → **Category** (Many-to-One, LAZY fetch)
- **Category.products** → **Product** (One-to-Many, mapped by "category", @JsonIgnore)

---

## 7. API Endpoints Specification

### 7.1 Product Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | /api/products | Create product | CreateProductRequest | CreateProductResponse (201) |
| GET | /api/products | Get all products | - | GetAllProductsResponse (200) |
| GET | /api/products/{id} | Get product by ID | - | GetProductResponse (200/404) |
| PUT | /api/products/{id} | Update product | UpdateProductRequest | UpdateProductResponse (200) |
| DELETE | /api/products/{id} | Delete product | - | DeleteProductResponse (204) |

### 7.2 Category Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | /api/categories | Create category | CreateCategoryRequest | CreateCategoryResponse (201) |
| GET | /api/categories | Get all categories | - | GetAllCategoriesResponse (200) |
| GET | /api/categories/{id} | Get category by ID | - | GetCategoryResponse (200/404) |
| PUT | /api/categories/{id} | Update category | UpdateCategoryRequest | UpdateCategoryResponse (200) |
| DELETE | /api/categories/{id} | Delete category | - | DeleteCategoryResponse (204) |

---

## 8. Business Rules Summary

### 8.1 Product Business Rules
| Rule ID | Description | Error Code |
|---------|-------------|------------|
| BR-P-01 | Product name must be unique | PRODUCT_NAME_EXISTS |
| BR-P-02 | Unit price must be greater than zero | INVALID_PRODUCT_PRICE |
| BR-P-03 | Units in stock must not be negative | INVALID_PRODUCT_STOCK |
| BR-P-04 | Category must exist if provided | (Returns empty Optional) |
| BR-P-05 | Product must exist for operations | PRODUCT_NOT_FOUND |
| BR-P-06 | Name must be unique when updating | PRODUCT_NAME_EXISTS |

### 8.2 Category Business Rules
| Rule ID | Description | Error Code |
|---------|-------------|------------|
| BR-C-01 | Category name must be unique | CATEGORY_NAME_EXISTS |
| BR-C-02 | Category name cannot be empty | INVALID_CATEGORY_NAME |
| BR-C-03 | Category name min 2 characters | INVALID_CATEGORY_NAME |
| BR-C-04 | Category must exist for operations | CATEGORY_NOT_FOUND |
| BR-C-05 | Name must be unique when updating | CATEGORY_NAME_EXISTS |
| BR-C-06 | Cannot delete if has products | CATEGORY_HAS_PRODUCTS |

---

## 9. Validation Rules

### 9.1 Product Validation
- **Name:** Required, non-empty, unique (case-insensitive)
- **Unit Price:** Required, must be > 0
- **Units in Stock:** Required, must be >= 0
- **Description:** Optional, max 2000 characters
- **Category ID:** Optional, must reference existing category if provided

### 9.2 Category Validation
- **Name:** Required, non-empty, min 2 characters, unique (case-insensitive)

---

## 10. Exception Handling Strategy

### 10.1 Business Exceptions
All business rule violations throw `BusinessException` with:
- Descriptive error message
- Business error code
- Handled by GlobalExceptionHandler (when enabled)
- Returns RFC 7807 compliant response

### 10.2 HTTP Status Codes
- **200 OK:** Successful GET/PUT operations
- **201 Created:** Successful POST operations
- **204 No Content:** Successful DELETE operations
- **400 Bad Request:** Business rule violations, validation errors
- **404 Not Found:** Resource not found
- **500 Internal Server Error:** Unexpected system errors

---

## 11. Deployment Requirements

### 11.1 Environment Requirements
- JDK 17 or higher
- Minimum 512MB RAM
- 100MB disk space for database
- Port 8080 available for HTTP

### 11.2 Configuration
- **Database URL:** jdbc:h2:file:./data/etradedemo
- **H2 Console:** Enabled at /h2-console
- **Hibernate DDL:** Auto-update mode
- **Server Port:** 8080
- **Logging Level:** DEBUG for development

### 11.3 Startup Sequence
1. Initialize Spring context
2. Configure data source and JPA
3. Create/update database schema
4. Load business rules and services
5. Initialize REST controllers
6. Start embedded Tomcat server
7. Enable Swagger UI

---

## 12. Testing Requirements

### 12.1 Unit Testing
- All business rules must have unit tests
- Service layer methods must be tested
- Repository custom queries must be tested

### 12.2 Integration Testing
- API endpoints must have integration tests
- Database operations must be tested
- Transaction rollback scenarios must be verified

### 12.3 Test Coverage
- Minimum 80% code coverage
- 100% coverage for business rules
- All exception paths must be tested

---

## 13. Documentation Requirements

### 13.1 Code Documentation
- All public classes must have Javadoc
- All public methods must have Javadoc
- Complex business logic must have inline comments

### 13.2 API Documentation
- OpenAPI 3.0 specification
- Interactive Swagger UI
- Request/response examples
- Error code documentation

---

## 14. Appendices

### 14.1 Glossary
- **Business Rule:** Validation logic enforcing business constraints
- **DTO:** Data Transfer Object for API communication
- **Entity:** JPA domain model representing database table
- **Repository:** Data access interface extending JpaRepository
- **Service:** Business logic implementation layer

### 14.2 Revision History
| Version | Date | Author | Description |
|---------|------|--------|-------------|
| 1.0 | 2025-11-11 | AI Assistant | Initial SRS document |

---

**Document End**
