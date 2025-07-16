package com.arka.system.domain.service;

import com.arka.system.domain.model.Product;
import com.arka.system.shared.exception.InsufficientStockException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio para gestión de inventario.
 * Contiene lógica de negocio específica del inventario que no pertenece a una entidad específica.
 */
@Service
public class InventoryDomainService {
    
    /**
     * Valida si hay suficiente stock para satisfacer una cantidad requerida
     * 
     * @param product Producto a verificar
     * @param requiredQuantity Cantidad requerida
     * @throws InsufficientStockException si no hay stock suficiente
     */
    public void validateStockAvailability(Product product, int requiredQuantity) {
        if (!product.isActive()) {
            throw new IllegalStateException("No se puede usar producto inactivo: " + product.getSku());
        }
        
        if (product.getStockQuantity() < requiredQuantity) {
            throw new InsufficientStockException(
                String.format("Stock insuficiente para producto %s. Disponible: %d, Requerido: %d", 
                    product.getSku(), product.getStockQuantity(), requiredQuantity)
            );
        }
    }
    
    /**
     * Reduce el stock de un producto
     * 
     * @param product Producto a modificar
     * @param quantity Cantidad a reducir
     */
    public void reduceStock(Product product, int quantity) {
        validateStockAvailability(product, quantity);
        product.setStockQuantity(product.getStockQuantity() - quantity);
    }
    
    /**
     * Aumenta el stock de un producto (por ejemplo, al recibir mercancía)
     * 
     * @param product Producto a modificar
     * @param quantity Cantidad a aumentar
     */
    public void increaseStock(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad a aumentar debe ser positiva");
        }
        product.setStockQuantity(product.getStockQuantity() + quantity);
    }
    
    /**
     * Verifica si un producto necesita reabastecimiento
     * 
     * @param product Producto a verificar
     * @return true si el stock actual está en o por debajo del mínimo
     */
    public boolean requiresReplenishment(Product product) {
        return product.getStockQuantity() <= product.getMinimumStock();
    }
    
    /**
     * Identifica productos que requieren reabastecimiento
     * 
     * @param products Lista de productos a evaluar
     * @return Lista de productos que necesitan reabastecimiento
     */
    public List<Product> findProductsRequiringReplenishment(List<Product> products) {
        return products.stream()
                .filter(this::requiresReplenishment)
                .filter(Product::isActive)
                .toList();
    }
    
    /**
     * Calcula la cantidad sugerida para reabastecimiento basada en el stock mínimo
     * 
     * @param product Producto a evaluar
     * @param targetStockLevel Nivel objetivo de stock (por defecto 2x el mínimo)
     * @return Cantidad sugerida para ordenar
     */
    public int calculateReplenishmentQuantity(Product product, int targetStockLevel) {
        if (targetStockLevel <= product.getMinimumStock()) {
            targetStockLevel = product.getMinimumStock() * 2; // Por defecto, 2x el mínimo
        }
        
        int currentStock = product.getStockQuantity();
        return Math.max(0, targetStockLevel - currentStock);
    }
    
    /**
     * Calcula la cantidad sugerida para reabastecimiento (usando el nivel por defecto)
     * 
     * @param product Producto a evaluar
     * @return Cantidad sugerida para ordenar
     */
    public int calculateReplenishmentQuantity(Product product) {
        return calculateReplenishmentQuantity(product, product.getMinimumStock() * 2);
    }
    
    /**
     * Verifica si se puede realizar una reserva de stock (sin aplicarla)
     * 
     * @param product Producto a verificar
     * @param quantity Cantidad a reservar
     * @return true si la reserva es posible
     */
    public boolean canReserveStock(Product product, int quantity) {
        return product.isActive() && product.getStockQuantity() >= quantity;
    }
}
