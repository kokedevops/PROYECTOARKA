package com.arka.system.shared.exception;

/**
 * Excepción lanzada cuando no hay suficiente stock para una operación.
 */
public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
