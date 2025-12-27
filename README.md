# Smart Note App (Mobil Programlama Projesi)

![Platform](https://img.shields.io/badge/Platform-Android-green.svg) ![Language](https://img.shields.io/badge/Language-Kotlin-purple.svg) ![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue.svg) ![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)

> **Proje Özeti:** Kullanıcıların notlarını güvenle saklayabileceği, yapay zeka ile özetleyebileceği ve sensör etkileşimleri sunan kapsamlı bir Android not alma uygulamasıdır.

---

## İçindekiler
- [Proje Hakkında](#-proje-hakkında)
- [Özellikler](#-özellikler)
- [Teknik Altyapı ve Mimari](#-teknik-altyapı-ve-mimari)
- [Kurulum](#-kurulum)

---

## Proje Hakkında

Bu proje, Mobil Programlama dersi kapsamında geliştirilmiş native bir Android uygulamasıdır. Modern Android geliştirme standartlarına uygun olarak **Jetpack Compose** ile UI tasarlanmış ve veri yönetimi için **Clean Architecture** prensiplerine sadık kalınmıştır.

Uygulama sadece basit bir not defteri değil; internet bağlantı takibi, arka plan yedekleme işlemleri ve cihaz sensörlerinin kullanımı gibi gelişmiş özellikler barındırır.

---

## Özellikler

### Not Yönetimi (CRUD)
* Kullanıcılar yeni not oluşturabilir, düzenleyebilir ve silebilir.
* Notlar yerel veritabanında (**Room Database**) güvenle saklanır.
* **Smart Search:** Not başlıklarında arama yapabilme.

### Yapay Zeka Entegrasyonu (AI)
* Uzun notlar için **AI Özetleme** özelliği.
* Mock AI servisi ile not içeriğini analiz edip önemli kısımları çıkarır.

### Kimlik Doğrulama (Auth)
* Kullanıcı giriş ekranı.
* **Retrofit** kullanılarak uzaktaki bir API ile güvenli giriş (Login) işlemleri.

### Bağlantı ve Senkronizasyon
* **Connectivity Manager:** İnternet bağlantısını anlık takip eder. Bağlantı koptuğunda kullanıcıyı uyarır ("Offline moddasınız").
* **WorkManager:** Her 15 dakikada bir çalışan arka plan servisi (`BackupWorker`) ile veriler periyodik olarak yedeklenir.

### Sensörler ve Donanım
* **Shake Detector:** Telefon sallandığında tetiklenen özel aksiyonlar (İvmeölçer sensörü).
* **Konum Servisleri:** Kullanıcının son konumunu alma (`LocationHelper`).

---

## Teknik Altyapı ve Mimari

Proje, sürdürülebilirlik ve test edilebilirlik için **MVVM (Model-View-ViewModel)** mimarisi üzerine kurulmuştur.

| Teknoloji | Kullanım Amacı |
| :--- | :--- |
| **Kotlin** | Ana programlama dili. |
| **Jetpack Compose** | Modern, deklaratif UI tasarımı. |
| **Room Database** | SQLite tabanlı yerel veri saklama katmanı. |
| **Retrofit & Gson** | REST API haberleşmesi ve JSON ayrıştırma. |
| **WorkManager** | Periyodik arka plan işlemleri (Yedekleme). |
| **Coroutines & Flow** | Asenkron işlemler ve reaktif veri akışı. |
| **Navigation Compose** | Tek aktivite (Single Activity) üzerinde ekran geçişleri. |

---

## Kurulum

Projeyi yerel makinenizde çalıştırmak için:

1.  Repoyu klonlayın:
    ```bash
    git clone [https://github.com/OsmanGundogdu/mobil-programlama-proje.git](https://github.com/OsmanGundogdu/mobil-programlama-proje.git)
    ```
2.  Android Studio'da `File > Open` diyerek projeyi açın.
3.  Gradle senkronizasyonunun (Sync) tamamlanmasını bekleyin.
4.  `local.properties` dosyasına varsa API anahtarlarınızı ekleyin.
5.  Uygulamayı emülatör veya fiziksel cihazda çalıştırın.

---
