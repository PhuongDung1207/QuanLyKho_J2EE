package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.service.BarcodeGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/labels")
public class LabelController {

    private final BarcodeGeneratorService barcodeGeneratorService;

    public LabelController(BarcodeGeneratorService barcodeGeneratorService) {
        this.barcodeGeneratorService = barcodeGeneratorService;
    }

    @GetMapping(value = "/batch/{code}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getBatchQRCode(@PathVariable String code) {
        try {
            byte[] image = barcodeGeneratorService.generateQRCodeImage(code, 250, 250);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/product/{barcode}/barcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getProductBarcode(@PathVariable String barcode) {
        try {
            byte[] image = barcodeGeneratorService.generateBarcodeImage(barcode, 600, 150);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
