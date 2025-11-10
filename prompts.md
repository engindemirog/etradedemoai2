# E-Ticaret Projesi Prompt Geçmişi

## 10 Kasım 2025

### Prompt 1: Proje Yapısı İnceleme
```
Spring Boot kullanarak bir eticaret projesi geliştireceğim. Sıfırdan bir spring boot projesi oluşturdum. Bu dosyada kullandığım teknolojileri ve versiyonlarını bulabilirsin. Bunu aklında tutacak şekilde öğren. Herhangi bir ekstra işlem yapma ve kod üretme.
```

### Prompt 2: Prompt Geçmişi Oluşturma
```
Önceden yazdığım ve sonradan yazacağım bütün promptları benim için bir dosyada sakla.
```

### Prompt 3: Temel Paket Yapısı Oluşturma
```
src\main\java\com\turkcell\etradedemoai altında bana gerekli olabilecek package yapısını oluştur. dataAccess, business, api, entities, common gibi. Her package içinde package-info.java dosyası oluştur. İçinde açıklama olsun.
```

### Prompt 4: Common Utilities Ekleme
```
common package'ının içine uygulama boyunca kullanabileceğimiz sabitler ve utility yapıları için bir CommonConstants sınıfı ekle.
```

### Prompt 5: BaseEntity Ekleme
```
entities paketi altına BaseEntity sınıfı ekle. Bu sınıf @MappedSuperclass olacak ve içinde id alanı olacak. Jakarta persistence kullan.
```

### Prompt 6: Product Entity Oluşturma
```
entities paketi altına Product entity'sini ekle. Bu entity BaseEntity'den türeyecek ve name, unitPrice, unitsInStock, description alanlarına sahip olacak.
```

### Prompt 7: Product Repository Oluşturma
```
dataAccess paketi altına ProductRepository interface'ini ekle. JpaRepository<Product,Long> extend edecek.
```

### Prompt 8: Business Katmanı Yapısı
```
business paketi altında abstracts ve concretes paketlerini oluştur ve her birinin içine package-info.java ekle.
```

### Prompt 9: ProductService Implementasyonu
```
ProductService interface ve ProductServiceImpl sınıfını oluştur. Temel CRUD operasyonları (create, getById, getAll, update, delete) olsun.
```

### Prompt 10: Product Controller Oluşturma
```
api.controllers paketi altına ProductController oluştur. RESTful CRUD endpoint'leri ekle.
```

### Prompt 11: DTO Yapısı Yeniden Düzenleme
```
business.dtos paketi altındaki request/response yapısını kaldır. Yerine business.dtos.requests.product ve business.dtos.responses.product paketlerini oluştur. Her operasyon için ayrı DTO'lar olsun (CreateProductRequest, CreateProductResponse, GetProductResponse, GetAllProductsResponse, UpdateProductRequest, UpdateProductResponse, DeleteProductResponse). Service ve controller'ları bu yeni yapıya göre güncelle.
```

### Prompt 12: H2 Veritabanı Konfigürasyonu
```
application.yaml dosyasında H2 veritabanını file-based olarak yapılandır. Kalıcı veri saklama için ./data/etradedemo path'ini kullan. H2 console'u aktif et.
```

### Prompt 13: Swagger/OpenAPI Desteği
```
Projeye Swagger/OpenAPI desteği ekle. pom.xml'e springdoc-openapi dependency'sini ekle ve gerekli konfigürasyonları yap.
```

### Prompt 14: Lombok Kaldırma
```
Projeden Lombok bağımlılığını tamamen kaldır. Tüm @Data, @Getter, @Setter, @Builder gibi annotation'ları standart Java koduna dönüştür. pom.xml'den Lombok dependency'sini ve plugin'ini kaldır.
```

### Prompt 15: Swagger Dokümantasyonu Ekleme
```
Endpointler için swagger dökümantasyon detayı ekle.
```

### Prompt 16: Category Entity ve İlişki Ekleme
```
Category nesnesini ekleyeceğiz. Product için yaptığımız tüm aşamaları Category (id,name) için de gerçekleştir. Category ile Product arasında bire çok ilişki olacak. Bu ilişkiyi de yapılandır. Gerekli düzenlemeleri yap.
```

### Prompt 17: Veritabanı Bağlantı Hatası Düzeltme (Dialect)
```
Error: Unable to determine Dialect without JDBC metadata
Çözüm: application.yaml'a hibernate.dialect: org.hibernate.dialect.H2Dialect eklendi.
```

### Prompt 18: H2 JDBC URL Düzeltme
```
Error: Feature not supported: "AUTO_SERVER=TRUE && DB_CLOSE_ON_EXIT=FALSE"
Çözüm: JDBC URL'yi basitleştirildi: jdbc:h2:file:./data/etradedemo
```

### Prompt 19: İş Kuralları (Business Rules) Ekleme
```
Şimdi iş kuralı kodlamaya geçebiliriz. İş kurallarımızı SOLID prensiplerine uygun yazacağız. İş kurallarımızı direkt Impl sınıflarında değil de, business altında oluşturacağımız (örneğin : ProductBusinessRules) sınıflar içinde yazacağız. Product nesnesi için özelliklerini dikkate alarak 5 adet iş kuralı olabilecek kurallar yaz. Bu nesne metotlarını, Impl sınıfı içinde, ilgili metotlarda kullan.
```

**Eklenen İş Kuralları:**

**ProductBusinessRules:**
1. checkIfProductNameExists - Ürün adının benzersiz olması
2. checkIfProductNameExistsForUpdate - Güncelleme sırasında ad benzersizliği
3. checkIfProductPriceIsValid - Fiyatın pozitif olması
4. checkIfProductStockIsValid - Stok miktarının negatif olmaması
5. checkIfProductExists - Ürün varlık kontrolü

**CategoryBusinessRules:**
1. checkIfCategoryNameExists - Kategori adının benzersiz olması
2. checkIfCategoryNameExistsForUpdate - Güncelleme sırasında ad benzersizliği
3. checkIfCategoryExists - Kategori varlık kontrolü
4. checkIfCategoryNameIsValid - Kategori adının geçerliliği (min 2 karakter)
5. checkIfCategoryHasProducts - Silme öncesi ilişkili ürün kontrolü

### Prompt 20: Prompt Geçmişini Güncelleme
```
tüm promptları güncelle
```

---

## Proje Özeti

### Kullanılan Teknolojiler
- Spring Boot 3.5.7
- Java 17
- Spring Data JPA / Hibernate
- H2 Database (File-based)
- OpenAPI/Swagger (springdoc-openapi)
- Maven

### Proje Yapısı
```
com.turkcell.etradedemoai/
├── api/
│   └── controllers/
│       ├── ProductController.java
│       └── CategoryController.java
├── business/
│   ├── abstracts/
│   │   ├── ProductService.java
│   │   └── CategoryService.java
│   ├── concretes/
│   │   ├── ProductServiceImpl.java
│   │   └── CategoryServiceImpl.java
│   ├── rules/
│   │   ├── ProductBusinessRules.java
│   │   └── CategoryBusinessRules.java
│   └── dtos/
│       ├── requests/
│       │   ├── product/ (Create, Update)
│       │   └── category/ (Create, Update)
│       └── responses/
│           ├── product/ (Create, Get, GetAll, Update, Delete)
│           └── category/ (Create, Get, GetAll, Update, Delete)
├── dataAccess/
│   ├── ProductRepository.java
│   └── CategoryRepository.java
├── entities/
│   ├── BaseEntity.java
│   ├── Product.java
│   └── Category.java
└── common/
    ├── BaseEntity.java
    └── CommonConstants.java
```

### Önemli Özellikler
- ✅ Katmanlı mimari (Controller → Service → Repository → Entity)
- ✅ DTO pattern (Request/Response ayrımı)
- ✅ SOLID prensipleri (Business Rules ayrımı)
- ✅ JPA entity ilişkileri (ManyToOne, OneToMany)
- ✅ OpenAPI/Swagger dokümantasyonu
- ✅ Kalıcı H2 veritabanı
- ✅ İş kuralları validasyonu
- ✅ Lombok kullanılmıyor (standart Java)

### API Endpoints

**Products:**
- POST   /api/products
- GET    /api/products
- GET    /api/products/{id}
- PUT    /api/products/{id}
- DELETE /api/products/{id}

**Categories:**
- POST   /api/categories
- GET    /api/categories
- GET    /api/categories/{id}
- PUT    /api/categories/{id}
- DELETE /api/categories/{id}

### Erişim URL'leri
- Uygulama: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:file:./data/etradedemo
  - Username: sa
  - Password: (boş)

