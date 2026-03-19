package com.example.JavaQuanLyKho.exception;

public class OutboundStockNotEnoughException extends RuntimeException {

    public OutboundStockNotEnoughException(String message) {
        super(message);
    }
}
