# Melikhan Asa Modu – Kurulum (Fabric, Minecraft 26.1.x)

> Not: Bu proje yazıldığı ortamda derlenemedi ve oyunda denenemedi. İlk derlemede küçük hatalar çıkabilir;
> çıkarsa hata mesajını kopyalayıp gönder, düzeltilir.

## 1. Gerekli programlar
1. **JDK 25** (26.1 Java 25 ister): https://adoptium.net → Temurin 25 → kur.
2. **IntelliJ IDEA 2025.2 veya daha yenisi** (Community yeterli): https://www.jetbrains.com/idea/download
3. Minecraft Java Edition 26.1.x + Fabric Loader + Fabric API (test için).

## 2. Gradle dosyalarını tamamla (pakette yok)
`gradle-wrapper.jar`, `gradlew`, `gradlew.bat` bu ortamda hazırlanamadı.
1. https://fabricmc.net/develop/template → Minecraft sürümünü seç (26.1) → "Generate" → zip indir.
2. Zip'teki şunları proje klasörüne kopyala (varsa üzerine yaz): `gradle` klasörü, `gradlew`, `gradlew.bat`.
3. Zip'teki `gradle.properties` içindeki şu 4 değeri benim `gradle.properties` dosyama yaz:
   `minecraft_version`, `loader_version`, `loom_version`, `fabric_api_version`.
   (Fabric sürüm numaraları sık değişir; en doğrusu şablondakilerdir.)

## 3. Projeyi aç ve çalıştır
1. IntelliJ → File → Open → proje klasörünü seç → "Trust Project".
2. File → Settings → Build Tools → Gradle → "Gradle JVM" = JDK 25 olsun.
3. Gradle yüklenmesini bekle (ilk seferde 5-10 dk).
4. Sağ üstteki "Minecraft Client" çalıştırma ayarını çalıştır.
5. Creative dünya aç → "Araçlar ve Faydalı Eşyalar" sekmesinde "Melikhan"ı al.

## 4. Test listesi
- Asa elde kanatsız görünür.
- Sağ tık → asaya biner (oturma animasyonu), kanatlar açık, baktığın yöne süzülür.
- 80° ve daha aşağı bakınca dalış: dalış kanatları, hız artar. ≈72° altına çıkınca normal süzülme.
- Tekrar sağ tık (veya Shift) → uçuş biter.
- Hata/çökme olursa `run/logs/latest.log` dosyasını gönder.

## 5. Oyuna kurmak için
Terminal: `gradlew build` → `build/libs/melikhan-asa-1.0.0.jar` → `.minecraft/mods` klasörüne (Fabric API ile).

## 6. Ayarlar: src/main/java/com/melikhan/wand/WandConfig.java
- Hızlar, dalış açısı (80), oturma yüksekliği.
- Asa ters/yan duruyorsa: `HEAD_POINTS_FORWARD`, `MODEL_ROLL_DEGREES` (0 veya 180).
- Asa oyuncudan kayık duruyorsa: `PIVOT_X/Y/Z` ve `SEAT_HEIGHT`.

## IntelliJ'de "import" hatası kırmızı çıkarsa
Kırmızı sınıf adının üstüne gel → Alt+Enter → "Import class". (26.1'de bazı paket adları değişmiş olabilir.)

## Dosya adı değişiklikleri (Minecraft büyük harf/Türkçe karakter kabul etmez)
ASA_BUTUN.png→asa_butun.png, DALIŞ_KADANI.png→dalis_kadani.png, SURATI.png→surati.png,
SURATI.mcmeta→surati.png.mcmeta. İçerikleri birebir aynı. Model geometrisi değiştirilmedi.
