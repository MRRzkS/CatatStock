# CatatStok - Aplikasi Manajemen Inventaris Barang

CatatStok adalah aplikasi desktop berbasis JavaFX untuk membantu manajemen stok barang, transaksi masuk/keluar, dan pelaporan sederhana. Aplikasi ini dirancang dengan antarmuka yang modern dan mudah digunakan.

## Fitur Utama

*   **Dashboard**: Ringkasan total barang, total stok, transaksi hari ini, aktivitas terkini, dan peringatan stok menipis.
*   **Manajemen Barang**: Tambah, update, hapus, dan cari data barang (SKU, Nama, Kategori, Satuan, Stok).
*   **Manajemen Stok**: Catat transaksi stok masuk dan keluar dengan mudah.
*   **Manajemen Kategori**: Kelola kategori barang untuk pengelompokan yang lebih baik.
*   **Laporan**: Export laporan stok barang dan histori transaksi ke format CSV (mendukung Excel).
*   **Penyimpanan Data**: Data tersimpan secara lokal dalam format JSON, sehingga tidak hilang saat aplikasi ditutup.

## Teknologi yang Digunakan

*   **Java 17+**: Bahasa pemrograman utama.
*   **JavaFX**: Framework untuk membangun antarmuka pengguna (GUI).
*   **Maven**: Manajemen dependensi dan build tool.
*   **Jackson**: Library untuk serialisasi dan deserialisasi JSON (penyimpanan data).
*   **ControlsFX**: Komponen UI tambahan untuk JavaFX.
*   **Ikonli**: Library ikon untuk JavaFX.

## Prasyarat

*   JDK 17 atau lebih baru.
*   Maven 3.6.0 atau lebih baru.

## Cara Menjalankan Aplikasi

1.  **Clone Repository**
    ```bash
    git clone https://github.com/MRRzkS/CatatStock.git
    cd CatatStock
    ```

2.  **Build dan Run**
    Jalankan perintah berikut di terminal:
    ```bash
    mvn javafx:run
    ```

## Struktur Proyek

*   `src/main/java/com/catatstok`: Source code Java.
    *   `controller`: Mengatur logika interaksi UI.
    *   `model`: Definisi objek data (Item, Transaction, Category) dan layanan data.
    *   `view`: File FXML untuk tampilan antarmuka.
*   `src/main/resources`: Resource aplikasi (FXML, CSS, Ikon).

## Kontribusi

Kontribusi selalu diterima! Silakan buat *pull request* untuk perbaikan bug atau penambahan fitur.

## Lisensi

Proyek ini dilisensikan di bawah [MIT License](LICENSE).
