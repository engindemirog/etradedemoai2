# Proje Kuralları ve Best Practices

Bu dosya, bu ve sonraki projelerde tutarlı, sürdürülebilir ve güvenli geliştirme için izlememizi istediğim kuralları içerir. Kısa, uygulanabilir ve CI ile denetlenebilir olacak şekilde hazırlandı.

## Kısa Sözleşme (Contract)
- Girdi: Java 17 tabanlı Spring Boot uygulaması, Maven wrapper (mvnw) ile build.
- Çıktı: Derlenebilir kaynak, birim testleri, OpenAPI dokümantasyonu, tutarlı kod stili.
- Hata Modu: CI derleme/test başarısızsa PR merge edilmez; kod review isteğe bağlı hata düzeltmeleri gerektirir.

## Temel Araçlar ve Versiyonlar
- Java: 17 (JDK, NOT JRE) — tüm geliştiricilerde ve CI'de JDK bulunmalı.
- Build: Maven (proje içindeki `mvnw` kullanılmalı).
- Spring Boot LTS serisi (proje için belirtilen versiyon korunmalı).
- MapStruct: compile-time mapper (örnek: 1.6.x veya proje uyumluluğu gösteren versiyon).
- Test: JUnit5, Mockito, AssertJ.

## Proje Yapısı / Paketleme
- Katmanlı yapı: `controller` (web), `business` (servis), `data`/`repository`, `entities`/`models`, `dto`/`requests`/`responses`, `mappers`.
- DTO'lar servis sınırında kullanılmalı; entity'ler controller'a dönülmemeli.
- Mapper: MapStruct kullan — `@Mapper(componentModel = "spring")`.

## Kod Stili ve Format
- Kod formatı otomatik: Google Java Format veya Spotless + formatter config önerilir.
- `editorconfig` ekle ve IDE ayarlarını paylaş (tab/space, encodig).
- Kısa, tek sorumluluklu metotlar (SRP). Fonksiyon/servis başına sorumluluk net olmalı.

## Bağımlılıklar ve Yönetim
- Direkt versiyon sabitle: pom.xml içinde properties kullan.
- Yeni bağımlılık eklerken: a) gerçekten gerekli mi, b) aktif bakım durumu, c) güvenlik/size etkisi.
- Maven Enforcer plugin ile JDK ve dependency rule'ları CI'de zorlanmalı.

## Mapping Kuralları (MapStruct)
- MapStruct tercih edilir (compile-time, hızlı). `componentModel = "spring"` kullan.
- Mapper interface'leri `business.mappers` paketinde toplanır.
- Çok karmaşık dönüşümler için helper metodları mapper içinde `@Named` olarak ekle.
- Null vs Optional kararını projede tutarlı kullan (DTO'larda Optional önerilmez).

## Hata Yönetimi
- RFC7807 / ProblemDetail tarzı global hata yanıtı kullan (ör: `BusinessProblemDetail`).
- Domain/business hataları için özel `BusinessException` ve handler kullan.
- Global exception handler uygulandıktan sonra OpenAPI ile çatışma varsa, hata dönüşleri için `@Schema` açıklamaları eklenmeli.

## Güvenlik
- Saklanmaması gerekenler: API anahtarları, tokenler, parolalar kaynak koduna commit edilmez.
- Dependabot veya benzeri araçlar ile bağımlılık güvenlik taraması yapılsın.

## CORS ve Konfigürasyon
- CORS konfigürasyonu açık yapılacaksa (geliştirme), prod için kısıtlanmış policy kullanılmalı.

## Testler
- Her servis/metodun happy-path birim testi olmalı; kritik iş kuralları için ek sınır/olumsuz durum testleri ekle.
- Mockito kullanarak repository/DB bağımlılıklarını mockla.
- Minimum gereksinim: servis katmanında %80 coverage; kritik iş mantıklarında %100 hedeflenebilir.
- Test isimlendirme: methodUnderTest_condition_expectedResult (ör: createProduct_whenValidRequest_returnsCreateResponse).

## CI / Build Kuralları
- CI pipeline isteğe bağlı ama önerilen aşamalar:
  1. Setup JDK 17
  2. `./mvnw -B -DskipTests=false clean verify` (testleri çalıştır)
  3. Kod format kontrolü (Spotless/formatter)
  4. Unit test coverage gate (jacoco) — eşiği proje yönetimi belirler.
  5. Güvenlik taraması (dependency-check / Snyk).

## Commit ve PR Kuralları
- Commit mesajları: Conventional Commits önerilir (feat/, fix/, docs/, chore/).
- PR checklist (zorunlu):
  - Derleme geçiriyor (CI green)
  - Unit testler yeşil
  - Kod formatı uygun
  - En az 1 reviewer onayı (kısıtlamalara göre 2)
  - Gerekliyse migration/dökümantasyon güncellendi

## Logging ve Monitoring
- Yapılandırılmış log (JSON veya SLF4J + MDC), önemli olaylara correlation-id ekle.

## Dokümantasyon
- API: springdoc-openapi / OpenAPI @Operation açıklamalarıyla belgelendir.
- SRS ve yüksek seviye tasarım değişiklikleri için `docs/` veya `SRS.md` oluştur.

## Hata & Çözümler (Common Gotchas)
- Lokal hata: "No compiler is provided" → ortamda JDK yerine JRE var. Çözüm: JDK kurup `JAVA_HOME` ayarla.
- MapStruct impl'leri yoksa `mvnw clean compile` çalıştır (JDK gerekli).

## Enforcement / Önerilen Ek Adımlar
- CI pipeline'a aşağıdakileri ekle:
  - `mvnw -DskipTests=false clean verify`
  - Spotless/formatter check
  - jacoco coverage check
  - maven-enforcer (JDK check)
- Opsiyonel: `pre-commit` git hook ile `mvnw -q -DskipTests=true spotless:check` çalıştır.

## Nasıl Başlanır / Şablon
1. Repo kopyalandıktan sonra: `export JAVA_HOME=C:\path\to\jdk17` (Windows: sistem ayarlarından ayarla).
2. `./mvnw -v` ile JDK gösterildiğini doğrula.
3. `./mvnw clean compile` MapStruct impl'lerini üretir.

## Kapanış ve İrtibat
Bu kurallar zamanla evrilebilir. Yeni kural önerileri için PR açın ve en az iki kişiden onay alın. İsterseniz ben bu dosyayı CI kurallarıyla (maven-enforcer, Spotless) otomatik olarak entegre edebilirim.

---
Bu dosya, projelerin hızlı ve tutarlı şekilde ilerlemesi için hazırlanmıştır. Gerektiğinde projeye özel ayarlamalar yapılabilir.
