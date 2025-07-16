package com.arka.system.shared.exception;

/**
 * Excepción lanzada cuando se intenta crear un producto con un SKU duplicado.
 */
public class DuplicateSkuException extends RuntimeException {
    
    public DuplicateSkuException(String message) {
        super(message);
    }
    
    public DuplicateSkuException(String message, Throwable cause) {
        super(message, cause);
    }
}
