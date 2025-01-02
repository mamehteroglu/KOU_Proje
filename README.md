.NET 8 API ve Kotlin Compose Mobil Uygulama
Bu proje, .NET 8 backend API ve Kotlin Compose ile geliştirilmiş mobil bir uygulamadan oluşmaktadır. JWT tabanlı kimlik doğrulama sistemi kullanılmaktadır.
Gereksinimler
Backend (.NET 8 API)

Visual Studio 2022 veya daha yeni bir sürüm
.NET 8.0 SDK
SQL Server 2019 veya daha yeni bir sürüm
SQL Server Management Studio (SSMS)

Mobile (Kotlin)

Android Studio Hedgehog | 2023.1.1 veya daha yeni bir sürüm
JDK 17 veya daha yeni bir sürüm
Kotlin Plugin
Android SDK 24 veya daha yüksek

Backend Kurulum

SQL Server Kurulumu:

SQL Server'ı buradan indirin ve kurun
SQL Server Configuration Manager'da TCP/IP protokolünü etkinleştirin
SQL Server Browser servisini başlatın


Visual Studio Kurulumu:

"ASP.NET and web development" workload'ını yükleyin
"Data storage and processing" workload'ını yükleyin


Proje Kurulumu:
bashCopygit clone [proje-url]
cd [proje-klasörü]
dotnet restore

Veritabanı Yapılandırması:

appsettings.json dosyasında connection string'i düzenleyin:

jsonCopy{
  "ConnectionStrings": {
    "DefaultConnection": "Server=YourServerName;Database=Net8DemoDb;Trusted_Connection=True;TrustServerCertificate=True;"
  }
}

Migration'ları çalıştırın:
bashCopydotnet ef migrations add InitialCreate
dotnet ef database update

Projeyi çalıştırın:
bashCopydotnet run


Mobile Uygulama Kurulumu

Android Studio Kurulumu:

Android Studio'yu buradan indirin
Kotlin plugin'ini yükleyin
Android SDK'yı güncelleyin


Proje Kurulumu:
bashCopygit clone [proje-url]
cd [mobile-klasörü]

API URL Yapılandırması:

ApiService.kt dosyasında base URL'i düzenleyin:

kotlinCopyprivate val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:5031/")  // Emülatör için
    // veya
    .baseUrl("http://localhost:5031/")  // Gerçek cihaz için

Android Emülatör Kurulumu:

AVD Manager'dan yeni bir emülatör oluşturun
API Level 24 veya üzeri bir sistem imajı seçin




Build Oluşturma
Android Build

Build Variants'dan "release" seçin
Build > Generate Signed Bundle / APK seçin
Key store bilgilerini girin
Build işlemini başlatın





Sorun Giderme
Backend Sorunları

SQL Server Bağlantı Hatası:

SQL Server servisinin çalıştığından emin olun
Firewall ayarlarını kontrol edin
Connection string'i doğrulayın


Migration Hataları:

Migration'ları temizleyip yeniden oluşturun

bashCopydotnet ef database drop
dotnet ef migrations remove
dotnet ef migrations add InitialCreate
dotnet ef database update


Mobile Sorunları

API Bağlantı Hatası:

Base URL'in doğru olduğundan emin olun
Android Manifest'te internet izninin olduğunu kontrol edin
Emülatör için 10.0.2.2 adresini kullandığınızdan emin olun


Build Hataları:

Gradle sync yapın
Clean Project yapın
Invalidate Caches / Restart deneyin



Lisans
Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için LICENSE dosyasına bakın.
