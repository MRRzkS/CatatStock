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

public class ReportController {

    @FXML
    private void exportItemReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Laporan Barang");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("laporan_barang.csv");
        
        // Get the stage from any control, e.g., we can't easily get it here without a node reference.
        // But we can assume the button that triggered this is part of the scene.
        // For simplicity, let's just use a new Stage or try to get the current window if possible.
        // A better way is to bind the controller to the main stage or pass the stage.
        // However, fileChooser.showSaveDialog(null) works but might not be modal to the app.
        // Let's try to get the active window.
        Stage stage = (Stage) javafx.stage.Window.getWindows().stream().filter(javafx.stage.Window::isShowing).findFirst().orElse(null);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("SKU;Nama Barang;Kategori;Satuan;Stok");
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
