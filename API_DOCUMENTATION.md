# E-Trade Demo AI - API Documentation
## RESTful API Endpoint Reference with Client Examples

**Base URL:** `http://localhost:8080`  
**API Version:** 1.0  
**Content-Type:** `application/json`  
**Date:** November 11, 2025

---

## Table of Contents
- [Product Endpoints](#product-endpoints)
  - [Create Product](#1-create-product)
  - [Get All Products](#2-get-all-products)
  - [Get Product by ID](#3-get-product-by-id)
  - [Update Product](#4-update-product)
  - [Delete Product](#5-delete-product)
- [Category Endpoints](#category-endpoints)
  - [Create Category](#6-create-category)
  - [Get All Categories](#7-get-all-categories)
  - [Get Category by ID](#8-get-category-by-id)
  - [Update Category](#9-update-category)
  - [Delete Category](#10-delete-category)
- [Error Responses](#error-responses)

---

# Product Endpoints

## 1. Create Product

Creates a new product in the catalog with validation.

**Endpoint:** `POST /api/products`  
**Authentication:** None (for demo)

### Request Body

```json
{
  "name": "Laptop Dell XPS 15",
  "unitPrice": 1299.99,
  "unitsInStock": 50,
  "description": "High-performance laptop with Intel i7 processor",
  "categoryId": 1
}
```

### Request Parameters

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| name | string | Yes | Unique, non-empty | Product name |
| unitPrice | number | Yes | > 0 | Product price |
| unitsInStock | integer | Yes | >= 0 | Inventory quantity |
| description | string | No | Max 2000 chars | Product description |
| categoryId | integer | No | Must exist | Category reference |

### Response (201 Created)

```json
{
  "id": 1,
  "name": "Laptop Dell XPS 15",
  "unitPrice": 1299.99,
  "unitsInStock": 50,
  "description": "High-performance laptop with Intel i7 processor",
  "createdDate": "2025-11-11T10:30:00Z",
  "categoryId": 1,
  "categoryName": "Electronics"
}
```

### Client Examples

#### Java (Spring RestTemplate)

```java
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ProductClient {
    private static final String BASE_URL = "http://localhost:8080/api/products";
    
    public void createProduct() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Request body
        String requestJson = """
            {
                "name": "Laptop Dell XPS 15",
                "unitPrice": 1299.99,
                "unitsInStock": 50,
                "description": "High-performance laptop with Intel i7 processor",
                "categoryId": 1
            }
            """;
        
        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Request
        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
        
        // Execute
        ResponseEntity<String> response = restTemplate.postForEntity(
            BASE_URL, 
            request, 
            String.class
        );
        
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());
    }
}
```

#### C# (.NET HttpClient)

```csharp
using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

public class ProductClient
{
    private static readonly string BaseUrl = "http://localhost:8080/api/products";
    
    public async Task CreateProduct()
    {
        using var client = new HttpClient();
        
        // Request body
        var product = new
        {
            name = "Laptop Dell XPS 15",
            unitPrice = 1299.99,
            unitsInStock = 50,
            description = "High-performance laptop with Intel i7 processor",
            categoryId = 1
        };
        
        var json = JsonSerializer.Serialize(product);
        var content = new StringContent(json, Encoding.UTF8, "application/json");
        
        // Execute
        var response = await client.PostAsync(BaseUrl, content);
        
        Console.WriteLine($"Status: {response.StatusCode}");
        var responseBody = await response.Content.ReadAsStringAsync();
        Console.WriteLine($"Body: {responseBody}");
    }
}
```

#### Python (requests)

```python
import requests
import json

BASE_URL = "http://localhost:8080/api/products"

def create_product():
    # Request body
    product = {
        "name": "Laptop Dell XPS 15",
        "unitPrice": 1299.99,
        "unitsInStock": 50,
        "description": "High-performance laptop with Intel i7 processor",
        "categoryId": 1
    }
    
    # Headers
    headers = {
        "Content-Type": "application/json"
    }
    
    # Execute
    response = requests.post(
        BASE_URL,
        json=product,
        headers=headers
    )
    
    print(f"Status: {response.status_code}")
    print(f"Body: {response.json()}")
    
    return response.json()

# Usage
if __name__ == "__main__":
    result = create_product()
```

#### JavaScript (Fetch API)

```javascript
const BASE_URL = 'http://localhost:8080/api/products';

async function createProduct() {
    const product = {
        name: "Laptop Dell XPS 15",
        unitPrice: 1299.99,
        unitsInStock: 50,
        description: "High-performance laptop with Intel i7 processor",
        categoryId: 1
    };
    
    try {
        const response = await fetch(BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(product)
        });
        
        console.log('Status:', response.status);
        const data = await response.json();
        console.log('Body:', data);
        
        return data;
    } catch (error) {
        console.error('Error:', error);
    }
}

// Usage
createProduct();
```

#### Ruby (Net::HTTP)

```ruby
require 'net/http'
require 'json'
require 'uri'

BASE_URL = 'http://localhost:8080/api/products'

def create_product
  uri = URI(BASE_URL)
  
  # Request body
  product = {
    name: "Laptop Dell XPS 15",
    unitPrice: 1299.99,
    unitsInStock: 50,
    description: "High-performance laptop with Intel i7 processor",
    categoryId: 1
  }
  
  # HTTP request
  http = Net::HTTP.new(uri.host, uri.port)
  request = Net::HTTP::Post.new(uri.path, {
    'Content-Type' => 'application/json'
  })
  request.body = product.to_json
  
  # Execute
  response = http.request(request)
  
  puts "Status: #{response.code}"
  puts "Body: #{response.body}"
  
  JSON.parse(response.body)
end

# Usage
create_product
```

---

## 2. Get All Products

Retrieves a list of all products in the catalog.

**Endpoint:** `GET /api/products`  
**Authentication:** None

### Request

No request body required.

### Response (200 OK)

```json
{
  "items": [
    {
      "id": 1,
      "name": "Laptop Dell XPS 15",
      "unitPrice": 1299.99,
      "unitsInStock": 50,
      "description": "High-performance laptop",
      "createdDate": "2025-11-11T10:30:00Z",
      "updatedDate": "2025-11-11T10:30:00Z",
      "deletedDate": null,
      "categoryId": 1,
      "categoryName": "Electronics"
    },
    {
      "id": 2,
      "name": "Wireless Mouse",
      "unitPrice": 29.99,
      "unitsInStock": 200,
      "description": "Ergonomic wireless mouse",
      "createdDate": "2025-11-11T11:00:00Z",
      "updatedDate": "2025-11-11T11:00:00Z",
      "deletedDate": null,
      "categoryId": 2,
      "categoryName": "Accessories"
    }
  ]
}
```

### Client Examples

#### Java

```java
public void getAllProducts() {
    RestTemplate restTemplate = new RestTemplate();
    
    ResponseEntity<String> response = restTemplate.getForEntity(
        BASE_URL,
        String.class
    );
    
    System.out.println("Products: " + response.getBody());
}
```

#### C#

```csharp
public async Task GetAllProducts()
{
    using var client = new HttpClient();
    
    var response = await client.GetAsync(BaseUrl);
    var content = await response.Content.ReadAsStringAsync();
    
    Console.WriteLine($"Products: {content}");
}
```

#### Python

```python
def get_all_products():
    response = requests.get(BASE_URL)
    print(f"Products: {response.json()}")
    return response.json()
```

#### JavaScript

```javascript
async function getAllProducts() {
    const response = await fetch(BASE_URL);
    const data = await response.json();
    console.log('Products:', data);
    return data;
}
```

#### Ruby

```ruby
def get_all_products
  uri = URI(BASE_URL)
  response = Net::HTTP.get_response(uri)
  
  puts "Products: #{response.body}"
  JSON.parse(response.body)
end
```

---

## 3. Get Product by ID

Retrieves a specific product by its unique identifier.

**Endpoint:** `GET /api/products/{id}`  
**Authentication:** None

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Product ID |

### Request Example

```
GET /api/products/1
```

### Response (200 OK)

```json
{
  "id": 1,
  "name": "Laptop Dell XPS 15",
  "unitPrice": 1299.99,
  "unitsInStock": 50,
  "description": "High-performance laptop",
  "createdDate": "2025-11-11T10:30:00Z",
  "updatedDate": "2025-11-11T10:30:00Z",
  "deletedDate": null,
  "categoryId": 1,
  "categoryName": "Electronics"
}
```

### Client Examples

#### Java

```java
public void getProductById(Long id) {
    RestTemplate restTemplate = new RestTemplate();
    
    String url = BASE_URL + "/" + id;
    ResponseEntity<String> response = restTemplate.getForEntity(
        url,
        String.class
    );
    
    System.out.println("Product: " + response.getBody());
}
```

#### C#

```csharp
public async Task GetProductById(int id)
{
    using var client = new HttpClient();
    
    var url = $"{BaseUrl}/{id}";
    var response = await client.GetAsync(url);
    var content = await response.Content.ReadAsStringAsync();
    
    Console.WriteLine($"Product: {content}");
}
```

#### Python

```python
def get_product_by_id(product_id):
    url = f"{BASE_URL}/{product_id}"
    response = requests.get(url)
    print(f"Product: {response.json()}")
    return response.json()
```

#### JavaScript

```javascript
async function getProductById(id) {
    const response = await fetch(`${BASE_URL}/${id}`);
    const data = await response.json();
    console.log('Product:', data);
    return data;
}
```

#### Ruby

```ruby
def get_product_by_id(id)
  uri = URI("#{BASE_URL}/#{id}")
  response = Net::HTTP.get_response(uri)
  
  puts "Product: #{response.body}"
  JSON.parse(response.body)
end
```

---

## 4. Update Product

Updates an existing product with new information.

**Endpoint:** `PUT /api/products/{id}`  
**Authentication:** None

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Product ID to update |

### Request Body

```json
{
  "name": "Laptop Dell XPS 15 (Updated)",
  "unitPrice": 1199.99,
  "unitsInStock": 45,
  "description": "Updated description",
  "categoryId": 1
}
```

### Response (200 OK)

```json
{
  "id": 1,
  "name": "Laptop Dell XPS 15 (Updated)",
  "unitPrice": 1199.99,
  "unitsInStock": 45,
  "description": "Updated description",
  "updatedDate": "2025-11-11T12:00:00Z"
}
```

### Client Examples

#### Java

```java
public void updateProduct(Long id) {
    RestTemplate restTemplate = new RestTemplate();
    
    String requestJson = """
        {
            "name": "Laptop Dell XPS 15 (Updated)",
            "unitPrice": 1199.99,
            "unitsInStock": 45,
            "description": "Updated description",
            "categoryId": 1
        }
        """;
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
    
    String url = BASE_URL + "/" + id;
    ResponseEntity<String> response = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        request,
        String.class
    );
    
    System.out.println("Updated: " + response.getBody());
}
```

#### C#

```csharp
public async Task UpdateProduct(int id)
{
    using var client = new HttpClient();
    
    var product = new
    {
        name = "Laptop Dell XPS 15 (Updated)",
        unitPrice = 1199.99,
        unitsInStock = 45,
        description = "Updated description",
        categoryId = 1
    };
    
    var json = JsonSerializer.Serialize(product);
    var content = new StringContent(json, Encoding.UTF8, "application/json");
    
    var url = $"{BaseUrl}/{id}";
    var response = await client.PutAsync(url, content);
    var responseBody = await response.Content.ReadAsStringAsync();
    
    Console.WriteLine($"Updated: {responseBody}");
}
```

#### Python

```python
def update_product(product_id):
    url = f"{BASE_URL}/{product_id}"
    
    product = {
        "name": "Laptop Dell XPS 15 (Updated)",
        "unitPrice": 1199.99,
        "unitsInStock": 45,
        "description": "Updated description",
        "categoryId": 1
    }
    
    response = requests.put(url, json=product)
    print(f"Updated: {response.json()}")
    return response.json()
```

#### JavaScript

```javascript
async function updateProduct(id) {
    const product = {
        name: "Laptop Dell XPS 15 (Updated)",
        unitPrice: 1199.99,
        unitsInStock: 45,
        description: "Updated description",
        categoryId: 1
    };
    
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(product)
    });
    
    const data = await response.json();
    console.log('Updated:', data);
    return data;
}
```

#### Ruby

```ruby
def update_product(id)
  uri = URI("#{BASE_URL}/#{id}")
  
  product = {
    name: "Laptop Dell XPS 15 (Updated)",
    unitPrice: 1199.99,
    unitsInStock: 45,
    description: "Updated description",
    categoryId: 1
  }
  
  http = Net::HTTP.new(uri.host, uri.port)
  request = Net::HTTP::Put.new(uri.path, {
    'Content-Type' => 'application/json'
  })
  request.body = product.to_json
  
  response = http.request(request)
  puts "Updated: #{response.body}"
  JSON.parse(response.body)
end
```

---

## 5. Delete Product

Deletes a product from the catalog.

**Endpoint:** `DELETE /api/products/{id}`  
**Authentication:** None

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | integer | Yes | Product ID to delete |

### Request Example

```
DELETE /api/products/1
```

### Response (204 No Content)

No response body. Check HTTP status code.

### Client Examples

#### Java

```java
public void deleteProduct(Long id) {
    RestTemplate restTemplate = new RestTemplate();
    
    String url = BASE_URL + "/" + id;
    restTemplate.delete(url);
    
    System.out.println("Product deleted successfully");
}
```

#### C#

```csharp
public async Task DeleteProduct(int id)
{
    using var client = new HttpClient();
    
    var url = $"{BaseUrl}/{id}";
    var response = await client.DeleteAsync(url);
    
    Console.WriteLine($"Status: {response.StatusCode}");
}
```

#### Python

```python
def delete_product(product_id):
    url = f"{BASE_URL}/{product_id}"
    response = requests.delete(url)
    print(f"Status: {response.status_code}")
    return response.status_code == 204
```

#### JavaScript

```javascript
async function deleteProduct(id) {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: 'DELETE'
    });
    
    console.log('Status:', response.status);
    return response.status === 204;
}
```

#### Ruby

```ruby
def delete_product(id)
  uri = URI("#{BASE_URL}/#{id}")
  
  http = Net::HTTP.new(uri.host, uri.port)
  request = Net::HTTP::Delete.new(uri.path)
  
  response = http.request(request)
  puts "Status: #{response.code}"
  response.code == '204'
end
```

---

# Category Endpoints

## 6. Create Category

Creates a new product category.

**Endpoint:** `POST /api/categories`  
**Authentication:** None

### Request Body

```json
{
  "name": "Electronics"
}
```

### Request Parameters

| Field | Type | Required | Constraints | Description |
|-------|------|----------|-------------|-------------|
| name | string | Yes | Unique, min 2 chars | Category name |

### Response (201 Created)

```json
{
  "id": 1,
  "name": "Electronics",
  "createdDate": "2025-11-11T10:00:00Z"
}
```

### Client Examples

#### Java

```java
public void createCategory() {
    RestTemplate restTemplate = new RestTemplate();
    
    String requestJson = """
        {
            "name": "Electronics"
        }
        """;
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
    
    ResponseEntity<String> response = restTemplate.postForEntity(
        "http://localhost:8080/api/categories",
        request,
        String.class
    );
    
    System.out.println("Category created: " + response.getBody());
}
```

#### C#

```csharp
public async Task CreateCategory()
{
    using var client = new HttpClient();
    
    var category = new { name = "Electronics" };
    var json = JsonSerializer.Serialize(category);
    var content = new StringContent(json, Encoding.UTF8, "application/json");
    
    var response = await client.PostAsync(
        "http://localhost:8080/api/categories",
        content
    );
    
    var responseBody = await response.Content.ReadAsStringAsync();
    Console.WriteLine($"Category created: {responseBody}");
}
```

#### Python

```python
def create_category():
    url = "http://localhost:8080/api/categories"
    category = {"name": "Electronics"}
    
    response = requests.post(url, json=category)
    print(f"Category created: {response.json()}")
    return response.json()
```

#### JavaScript

```javascript
async function createCategory() {
    const response = await fetch('http://localhost:8080/api/categories', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: "Electronics" })
    });
    
    const data = await response.json();
    console.log('Category created:', data);
    return data;
}
```

#### Ruby

```ruby
def create_category
  uri = URI('http://localhost:8080/api/categories')
  
  category = { name: "Electronics" }
  
  http = Net::HTTP.new(uri.host, uri.port)
  request = Net::HTTP::Post.new(uri.path, {
    'Content-Type' => 'application/json'
  })
  request.body = category.to_json
  
  response = http.request(request)
  puts "Category created: #{response.body}"
  JSON.parse(response.body)
end
```

---

## 7. Get All Categories

Retrieves a list of all categories.

**Endpoint:** `GET /api/categories`  
**Authentication:** None

### Response (200 OK)

```json
{
  "items": [
    {
      "id": 1,
      "name": "Electronics",
      "createdDate": "2025-11-11T10:00:00Z"
    },
    {
      "id": 2,
      "name": "Accessories",
      "createdDate": "2025-11-11T10:15:00Z"
    }
  ]
}
```

### Client Examples

#### Java

```java
public void getAllCategories() {
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity(
        "http://localhost:8080/api/categories",
        String.class
    );
    System.out.println("Categories: " + response.getBody());
}
```

#### C#

```csharp
public async Task GetAllCategories()
{
    using var client = new HttpClient();
    var response = await client.GetAsync("http://localhost:8080/api/categories");
    var content = await response.Content.ReadAsStringAsync();
    Console.WriteLine($"Categories: {content}");
}
```

#### Python

```python
def get_all_categories():
    response = requests.get("http://localhost:8080/api/categories")
    print(f"Categories: {response.json()}")
    return response.json()
```

#### JavaScript

```javascript
async function getAllCategories() {
    const response = await fetch('http://localhost:8080/api/categories');
    const data = await response.json();
    console.log('Categories:', data);
    return data;
}
```

#### Ruby

```ruby
def get_all_categories
  uri = URI('http://localhost:8080/api/categories')
  response = Net::HTTP.get_response(uri)
  puts "Categories: #{response.body}"
  JSON.parse(response.body)
end
```

---

## 8. Get Category by ID

Retrieves a specific category by its ID.

**Endpoint:** `GET /api/categories/{id}`  
**Authentication:** None

### Response (200 OK)

```json
{
  "id": 1,
  "name": "Electronics",
  "createdDate": "2025-11-11T10:00:00Z"
}
```

### Client Examples

#### Java

```java
public void getCategoryById(Long id) {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:8080/api/categories/" + id;
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    System.out.println("Category: " + response.getBody());
}
```

#### C#

```csharp
public async Task GetCategoryById(int id)
{
    using var client = new HttpClient();
    var url = $"http://localhost:8080/api/categories/{id}";
    var response = await client.GetAsync(url);
    var content = await response.Content.ReadAsStringAsync();
    Console.WriteLine($"Category: {content}");
}
```

#### Python

```python
def get_category_by_id(category_id):
    url = f"http://localhost:8080/api/categories/{category_id}"
    response = requests.get(url)
    print(f"Category: {response.json()}")
    return response.json()
```

#### JavaScript

```javascript
async function getCategoryById(id) {
    const response = await fetch(`http://localhost:8080/api/categories/${id}`);
    const data = await response.json();
    console.log('Category:', data);
    return data;
}
```

#### Ruby

```ruby
def get_category_by_id(id)
  uri = URI("http://localhost:8080/api/categories/#{id}")
  response = Net::HTTP.get_response(uri)
  puts "Category: #{response.body}"
  JSON.parse(response.body)
end
```

---

## 9. Update Category

Updates an existing category.

**Endpoint:** `PUT /api/categories/{id}`  
**Authentication:** None

### Request Body

```json
{
  "name": "Electronics & Gadgets"
}
```

### Response (200 OK)

```json
{
  "id": 1,
  "name": "Electronics & Gadgets",
  "updatedDate": "2025-11-11T12:00:00Z"
}
```

### Client Examples

#### Java

```java
public void updateCategory(Long id) {
    RestTemplate restTemplate = new RestTemplate();
    
    String requestJson = """
        {
            "name": "Electronics & Gadgets"
        }
        """;
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
    
    String url = "http://localhost:8080/api/categories/" + id;
    ResponseEntity<String> response = restTemplate.exchange(
        url, HttpMethod.PUT, request, String.class
    );
    
    System.out.println("Updated: " + response.getBody());
}
```

#### C#

```csharp
public async Task UpdateCategory(int id)
{
    using var client = new HttpClient();
    
    var category = new { name = "Electronics & Gadgets" };
    var json = JsonSerializer.Serialize(category);
    var content = new StringContent(json, Encoding.UTF8, "application/json");
    
    var url = $"http://localhost:8080/api/categories/{id}";
    var response = await client.PutAsync(url, content);
    var responseBody = await response.Content.ReadAsStringAsync();
    
    Console.WriteLine($"Updated: {responseBody}");
}
```

#### Python

```python
def update_category(category_id):
    url = f"http://localhost:8080/api/categories/{category_id}"
    category = {"name": "Electronics & Gadgets"}
    
    response = requests.put(url, json=category)
    print(f"Updated: {response.json()}")
    return response.json()
```

#### JavaScript

```javascript
async function updateCategory(id) {
    const response = await fetch(`http://localhost:8080/api/categories/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: "Electronics & Gadgets" })
    });
    
    const data = await response.json();
    console.log('Updated:', data);
    return data;
}
```

#### Ruby

```ruby
def update_category(id)
  uri = URI("http://localhost:8080/api/categories/#{id}")
  
  category = { name: "Electronics & Gadgets" }
  
  http = Net::HTTP.new(uri.host, uri.port)
  request = Net::HTTP::Put.new(uri.path, {
    'Content-Type' => 'application/json'
  })
  request.body = category.to_json
  
  response = http.request(request)
  puts "Updated: #{response.body}"
  JSON.parse(response.body)
end
```

---

## 10. Delete Category

Deletes a category (only if it has no associated products).

**Endpoint:** `DELETE /api/categories/{id}`  
**Authentication:** None

### Response (204 No Content)

No response body.

### Client Examples

#### Java

```java
public void deleteCategory(Long id) {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:8080/api/categories/" + id;
    restTemplate.delete(url);
    System.out.println("Category deleted successfully");
}
```

#### C#

```csharp
public async Task DeleteCategory(int id)
{
    using var client = new HttpClient();
    var url = $"http://localhost:8080/api/categories/{id}";
    var response = await client.DeleteAsync(url);
    Console.WriteLine($"Status: {response.StatusCode}");
}
```

#### Python

```python
def delete_category(category_id):
    url = f"http://localhost:8080/api/categories/{category_id}"
    response = requests.delete(url)
    print(f"Status: {response.status_code}")
    return response.status_code == 204
```

#### JavaScript

```javascript
async function deleteCategory(id) {
    const response = await fetch(`http://localhost:8080/api/categories/${id}`, {
        method: 'DELETE'
    });
    console.log('Status:', response.status);
    return response.status === 204;
}
```

#### Ruby

```ruby
def delete_category(id)
  uri = URI("http://localhost:8080/api/categories/#{id}")
  
  http = Net::HTTP.new(uri.host, uri.port)
  request = Net::HTTP::Delete.new(uri.path)
  
  response = http.request(request)
  puts "Status: #{response.code}"
  response.code == '204'
end
```

---

# Error Responses

All error responses follow RFC 7807 Problem Details format.

## Business Rule Violation (400 Bad Request)

```json
{
  "type": "https://etradedemo.com/errors/business",
  "title": "Business Rule Violation",
  "status": 400,
  "detail": "Product with name 'Laptop Dell XPS 15' already exists",
  "instance": "/api/products",
  "businessCode": "PRODUCT_NAME_EXISTS",
  "timestamp": "2025-11-11T10:30:00Z"
}
```

## Resource Not Found (404 Not Found)

```json
{
  "type": "https://etradedemo.com/errors/business",
  "title": "Business Rule Violation",
  "status": 400,
  "detail": "Product not found with id: 999",
  "instance": "/api/products/999",
  "businessCode": "PRODUCT_NOT_FOUND",
  "timestamp": "2025-11-11T10:30:00Z"
}
```

## Error Code Reference

| Code | HTTP Status | Description |
|------|-------------|-------------|
| PRODUCT_NAME_EXISTS | 400 | Product name already exists |
| INVALID_PRODUCT_PRICE | 400 | Price must be greater than zero |
| INVALID_PRODUCT_STOCK | 400 | Stock cannot be negative |
| PRODUCT_NOT_FOUND | 400 | Product does not exist |
| CATEGORY_NAME_EXISTS | 400 | Category name already exists |
| INVALID_CATEGORY_NAME | 400 | Category name validation failed |
| CATEGORY_NOT_FOUND | 400 | Category does not exist |
| CATEGORY_HAS_PRODUCTS | 400 | Cannot delete category with products |

## Error Handling Examples

### Java

```java
try {
    createProduct();
} catch (HttpClientErrorException e) {
    System.err.println("Error: " + e.getStatusCode());
    System.err.println("Body: " + e.getResponseBodyAsString());
}
```

### C#

```csharp
try
{
    await CreateProduct();
}
catch (HttpRequestException e)
{
    Console.WriteLine($"Error: {e.Message}");
}
```

### Python

```python
try:
    create_product()
except requests.exceptions.HTTPError as e:
    print(f"Error: {e.response.status_code}")
    print(f"Details: {e.response.json()}")
```

### JavaScript

```javascript
try {
    await createProduct();
} catch (error) {
    if (error.response) {
        console.error('Error:', error.response.status);
        console.error('Details:', await error.response.json());
    }
}
```

### Ruby

```ruby
begin
  create_product
rescue => e
  puts "Error: #{e.message}"
end
```

---

## Additional Resources

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI Spec:** http://localhost:8080/v3/api-docs
- **H2 Console:** http://localhost:8080/h2-console

---

**Document Version:** 1.0  
**Last Updated:** November 11, 2025
