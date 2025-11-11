# Yazılım Geliştirme Kuralları — Genel Şablon

Bu dosya, farklı projelerde temel girdi olarak kullanılabilecek, dil/çerçeve özelinden büyük ölçüde arındırılmış bir kurallar şablonudur. Amaç: tutarlı, sürdürülebilir, test edilebilir ve güvenli geliştirme alışkanlıkları sunmak.

## Kısa Sözleşme (Contract)
- Girdi: Herhangi bir yazılım projesi (tercihen sürümlenmiş araçlar; örneğin JVM projeleri için Java 17+ önerilir).
- Çıktı: Derlenebilir/çalıştırılabilir kaynak, birim testleri, açıkça tanımlanmış hata modelleri, tutarlı kod stili.
- Hata Modu: CI derleme/test başarısızsa değişiklikler ana dala/ürün sürümüne merge edilmez.

## Uygulanabilirlik / Adaptasyon
- Bu rehber geneldir: proje dili, build aracı veya dağıtım ortamına bağlı olarak bazı bölümler (ör: MapStruct, mvnw) adapte edilmelidir.
- Proje sahipleri, bu dosyayı temel alarak proje-özel gereksinim ve araç listesini eklemelidir.

## Temel Prensipler
- Kısa, test edilebilir, tek sorumluluklu bileşenler oluşturun (SRP).
- Bağımlılıkları soyutlamalar üzerinden enjekte edin (DIP).
- Arayüzleri küçük ve spesifik tutun (ISP).
- Mevcut davranışı bozmadan genişleyebilen tasarımlar tercih edin (OCP).
- Alt sınıflar üst sınıfların yerine sorunsuz geçebilmeli (LSP).

## Araçlar ve Versiyon Önerileri (Genel)
- JVM projeleri için öneri: Java 17+ (JDK). CI ortamlarında JDK kurulu ve `JAVA_HOME` ayarlı olmalı.
- Build araçları: Maven veya Gradle (proje için uygun olan tercih edilmeli).
- Kod formatı: proje ekibi ile kararlaştırılmış formatlayıcı (Google Java Format, ktlint, Prettier, vs.).
- Mapper seçimleri: Java projeleri için MapStruct (compile-time), diğer diller için uygun benzeri tercih edin.

## Proje Yapısı (örnek, dil/çerçeveye göre uyarlanır)
- Layered architecture önerilir: API / Application / Domain / Infrastructure.
- DTO/Request/Response ile domain entity'lerini ayırın.
- Mapper'lar sadece dönüşüm yapmalı; iş mantığını içermemeli.

## SOLID Prensipleri — Business & Rules Yazımı
Bu şablon özellikle iş kuralları ve servis katmanları için uygulanabilecek pratik kuralları içerir:

- Single Responsibility (SRP): Her sınıf/sınıf grubu tek bir sorumluluk taşımalı. Validation ve iş kuralları ayrı bileşenlerde tutulmalı.
- Open/Closed (OCP): Yeni davranış eklemek için mevcut sınıfları değiştirmek yerine yeni modüller/implementasyonlar ekleyin.
- Liskov Substitution (LSP): Arayüzleri implement eden sınıflar beklenen davranışı bozmayacak şekilde tasarlanmalı.
- Interface Segregation (ISP): Büyük arayüzler yerine küçük, özel arayüzler kullanın.
- Dependency Inversion (DIP): Üst seviye bileşenler somut sınıflara değil arayüzlere/abstraction'lara bağımlı olmalı; constructor-injection tercih edilir.

Pratik öneriler:
- İş kurallarını `rules` veya `domain.rules` gibi bir pakette toplayın; her rule tek bir koşulu doğrulasın.
- Kurallar saf (stateless) olabildiğince side-effect üretmesin. DB/IO gereksinimi varsa, bu açıkça belirtilsin ve unit testlerde mocklanabilir olsun.
- Her kural için hızlı unit testler yazın (happy-path + hata-case).

## Hata Yönetimi (Genel Yaklaşım)
- API/servisler için tutarlı bir hata modeli kullanın (ör: RFC7807 Problem Details veya dilinize uygun benzeri).
- Domain hataları ile teknik hataları ayırın; kullanıcıya dönen hata mesajları ham stacktrace içermemeli.

## Mapping (Dönüşümler)
- Java için MapStruct gibi compile-time çözümler önerilir; diğer diller için performans/maintainability dengesi göz önünde bulundurulmalı.
- Mapper'lar null/optional sözleşmesi konusunda açık olmalı; dönüşümlerin sınır koşulları test edilmeli.

## Testler
- Her mantıksal birim için birim testleri (unit) yazılmalı; kritik iş akışları için entegrasyon testleri eklenmeli.
- Testler izolasyonlu olmalı: dış bağımlılıklar mock'lanmalı veya test konteynerleri kullanılmalı.
- Minimum tavsiye: temel modüller için %70-80 coverage; kritik domain kuralları için daha yüksek hedef.

## CI / Build Önerileri
- CI pipeline genel aşamaları (proje türüne göre uyarlanır):
  1. Checkout
  2. Setup environment (JDK/SDK, toolchain)
  3. Lint/format check
  4. Build (derleme)
  5. Unit tests + coverage
  6. Security scan (dependency checks)
  7. Publish artifacts (on approval)

## Commit ve PR Kuralları
- Commit mesajı standardı belirleyin (Conventional Commits tavsiye edilir).
- PR checklist (genel):
  - Build/CI başarılı
  - Unit testler çalışıyor
  - Kod formatı uygulanmış
  - Değişiklikleri açıklayan açıklama ve gerekiyorsa dokümantasyon
  - En az bir reviewer

## Güvenlik ve Secrets
- Secrets/anahtarlar kaynak koduna commit edilmez; ortam değişkenleri/secret manager tercih edilir.
- Dependabot/Snyk gibi araçlarla bağımlılık güvenlik taraması yapın.

## Logging, Tracing ve Monitoring
- Yapılandırılmış logging (en azından açık format) ve correlation-id kullanımını teşvik edin.
- Kritik uç noktalar için tracing (OpenTelemetry) ve metrik toplanması önerilir.

## Enforcement / Checklist (Hızlı Kontrol)
- CI'de zorlanabilecek kontroller:
  - Linter/format doğrulama
  - Build ve test geçişleri
  - Coverage gate (proje ihtiyacına göre)
  - JDK/SDK enforcer (proje türüne göre)

## Uygulama Şablonları ve Örnekler
- `BusinessRule` pattern (pseudocode):

```
interface BusinessRule { void check(); }

// örnek implementasyon
class ProductMustExistRule implements BusinessRule {
  // repository veya service injected
  public void check() { /* throw business exception if not satisfied */ }
}
```

## Nasıl Başlanır (Adaptasyon Rehberi)
- Yeni bir proje başlatırken bu dosyayı kopyalayın ve aşağıdakileri proje-özel şekilde doldurun:
  - Hangi dil ve sürümlerin desteklendiği
  - Build ve CI komutları (mvnw/gradlew/npm vs.)
  - Gerekli araçlar ve setup talimatları

## Kapanış
Bu dosya genel bir şablondur; proje ihtiyaçlarına göre uyarlanmalıdır. Şablonda değişiklik öneriniz varsa PR ile gönderin; kritik değişiklikler için en az iki onay önerilir.

