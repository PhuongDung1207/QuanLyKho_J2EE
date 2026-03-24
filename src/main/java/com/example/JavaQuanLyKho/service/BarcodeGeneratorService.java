package com.example.JavaQuanLyKho.service;

public interface BarcodeGeneratorService {
    byte[] generateQRCodeImage(String text, int width, int height) throws Exception;
    byte[] generateBarcodeImage(String text, int width, int height) throws Exception;
}
