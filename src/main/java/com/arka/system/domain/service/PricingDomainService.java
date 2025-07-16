package com.arka.system.domain.service;

import com.arka.system.domain.model.Product;
import com.arka.system.domain.model.OrderItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Servicio de dominio para cálculos relacionados con precios.
 * Contiene lógica de negocio para pricing que no pertenece a una entidad específica.
 */
@Service
public class PricingDomainService {
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.19"); // IVA 19% Colombia
    private static final int CURRENCY_SCALE = 2; // Dos decimales para moneda
    
    /**
     * Calcula el subtotal de un item de pedido
     * 
     * @param product Producto del item
     * @param quantity Cantidad del item
     * @return Subtotal calculado
     */
    public BigDecimal calculateItemSubtotal(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        
        return product.getSalePrice()
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(CURRENCY_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula el total de una lista de items
     * 
     * @param orderItems Lista de items del pedido
     * @return Total calculado
     */
    public BigDecimal calculateOrderTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(CURRENCY_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula el total con impuestos
     * 
     * @param subtotal Subtotal antes de impuestos
     * @return Total con impuestos incluidos
     */
    public BigDecimal calculateTotalWithTax(BigDecimal subtotal) {
        BigDecimal tax = subtotal.multiply(TAX_RATE)
                .setScale(CURRENCY_SCALE, RoundingMode.HALF_UP);
        
        return subtotal.add(tax)
                .setScale(CURRENCY_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula el margen de ganancia de un producto
     * 
     * @param product Producto a evaluar
     * @return Margen de ganancia como porcentaje (0.0 - 1.0)
     */
    public BigDecimal calculateProfitMargin(Product product) {
        BigDecimal salePrice = product.getSalePrice();
        BigDecimal purchasePrice = product.getPurchasePrice();
        
        if (purchasePrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("El precio de compra no puede ser cero");
        }
        
        BigDecimal profit = salePrice.subtract(purchasePrice);
        return profit.divide(salePrice, 4, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula el margen de ganancia en valor absoluto
     * 
     * @param product Producto a evaluar
     * @return Margen de ganancia en dinero
     */
    public BigDecimal calculateProfitAmount(Product product) {
        return product.getSalePrice()
                .subtract(product.getPurchasePrice())
                .setScale(CURRENCY_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * Valida que el precio de venta sea mayor al precio de compra
     * 
     * @param salePrice Precio de venta propuesto
     * @param purchasePrice Precio de compra
     * @return true si el precio es válido
     */
    public boolean isValidSalePrice(BigDecimal salePrice, BigDecimal purchasePrice) {
        return salePrice.compareTo(purchasePrice) > 0;
    }
    
    /**
     * Sugiere un precio de venta basado en un margen mínimo
     * 
     * @param purchasePrice Precio de compra
     * @param minimumMarginPercentage Margen mínimo deseado (ej: 0.30 para 30%)
     * @return Precio de venta sugerido
     */
    public BigDecimal suggestSalePrice(BigDecimal purchasePrice, BigDecimal minimumMarginPercentage) {
        if (minimumMarginPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El margen mínimo no puede ser negativo");
        }
        
        // Precio = Costo / (1 - Margen)
        BigDecimal divisor = BigDecimal.ONE.subtract(minimumMarginPercentage);
        
        return purchasePrice.divide(divisor, CURRENCY_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula descuento por volumen basado en cantidad
     * 
     * @param basePrice Precio base
     * @param quantity Cantidad del pedido
     * @return Precio con descuento aplicado
     */
    public BigDecimal calculateVolumeDiscount(BigDecimal basePrice, int quantity) {
        BigDecimal discountRate = BigDecimal.ZERO;
        
        // Escalas de descuento por volumen
        if (quantity >= 100) {
            discountRate = new BigDecimal("0.10"); // 10% descuento
        } else if (quantity >= 50) {
            discountRate = new BigDecimal("0.05"); // 5% descuento
        } else if (quantity >= 20) {
            discountRate = new BigDecimal("0.02"); // 2% descuento
        }
        
        BigDecimal discount = basePrice.multiply(discountRate);
        return basePrice.subtract(discount)
                .setScale(CURRENCY_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * Obtiene la tasa de impuesto actual
     * 
     * @return Tasa de impuesto como decimal
     */
    public BigDecimal getCurrentTaxRate() {
        return TAX_RATE;
    }
}
