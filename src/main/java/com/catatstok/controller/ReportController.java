package com.catatstok.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import com.catatstok.model.DataService;
import com.catatstok.model.Item;
import com.catatstok.model.StockTransaction;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

// Controller untuk halaman Laporan (Export CSV)
public class ReportController {

    // Export laporan stok barang ke CSV
    @FXML
    private void exportItemReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Laporan Barang");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("laporan_barang.csv");
        
        // Mendapatkan stage aktif untuk menampilkan dialog save
        Stage stage = (Stage) javafx.stage.Window.getWindows().stream().filter(javafx.stage.Window::isShowing).findFirst().orElse(null);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Header CSV
                writer.println("SKU;Nama Barang;Kategori;Satuan;Stok");
                // Iterasi semua item dan tulis ke file
                for (Item item : DataService.getInstance().getItems()) {
                    writer.printf("%s;%s;%s;%s;%d%n",
                        escapeSpecialCharacters(item.getSku()),
                        escapeSpecialCharacters(item.getName()),
                        escapeSpecialCharacters(item.getCategory()),
                        escapeSpecialCharacters(item.getUnit()),
                        item.getStock()
                    );
                }
                showAlert("Export Berhasil", "Laporan Stok Barang telah disimpan ke:\n" + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Export Gagal", "Terjadi kesalahan saat menyimpan file.");
            }
        }
    }

    // Export laporan transaksi ke CSV
    @FXML
    private void exportTransactionReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Laporan Transaksi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("laporan_transaksi.csv");

        Stage stage = (Stage) javafx.stage.Window.getWindows().stream().filter(javafx.stage.Window::isShowing).findFirst().orElse(null);
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("Tanggal;Tipe;SKU Barang;Jumlah;Petugas");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                for (StockTransaction t : DataService.getInstance().getTransactions()) {
                    writer.printf("%s;%s;%s;%d;%s%n",
                        t.getDate().format(formatter),
                        escapeSpecialCharacters(t.getType()),
                        escapeSpecialCharacters(t.getItemSku()),
                        t.getQuantity(),
                        escapeSpecialCharacters(t.getUser())
                    );
                }
                showAlert("Export Berhasil", "Laporan Histori Transaksi telah disimpan ke:\n" + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Export Gagal", "Terjadi kesalahan saat menyimpan file.");
            }
        }
    }

    // Helper untuk menangani karakter khusus di CSV (misal koma atau quote)
    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            return "";
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
