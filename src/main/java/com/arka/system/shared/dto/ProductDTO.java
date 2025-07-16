package com.arka.system.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferencia de datos de productos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    
    private Long id;
    
    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 50, message = "El SKU no puede exceder 50 caracteres")
    private String sku;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String name;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    private String brand;
    
    @NotNull(message = "El precio de compra es obligatorio")
    @PositiveOrZero(message = "El precio de compra debe ser positivo")
    private BigDecimal purchasePrice;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @PositiveOrZero(message = "El precio de venta debe ser positivo")
    private BigDecimal salePrice;
    
    @NotNull(message = "El stock actual es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stockQuantity;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @PositiveOrZero(message = "El stock mínimo debe ser positivo")
    private Integer minimumStock;
    
    private BigDecimal weight;
    
    @Size(max = 100, message = "Las dimensiones no pueden exceder 100 caracteres")
    private String dimensions;
    
    private Boolean active;
    
    @NotNull(message = "La categoría es obligatoria")
    private CategoryDTO category;
    
    // Campos adicionales para facilitar el acceso
    private Long categoryId;
    private String categoryName;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<ProductAttributeDTO> attributes;
    
    /**
     * Verifica si el producto tiene stock bajo
     * @return true si el stock actual está por debajo del mínimo
     */
    public boolean isLowStock() {
        return stockQuantity != null && minimumStock != null && 
               stockQuantity <= minimumStock;
    }
    
    /**
     * Verificar si el producto está activo
     * @return true si el producto está activo
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
    
    /**
     * Calcula el margen de ganancia como porcentaje
     * @return Margen de ganancia (0.0 - 1.0)
     */
    public double getProfitMargin() {
        if (purchasePrice == null || salePrice == null || 
            salePrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        
        BigDecimal profit = salePrice.subtract(purchasePrice);
        return profit.divide(salePrice, 4, java.math.RoundingMode.HALF_UP).doubleValue();
    }
}
