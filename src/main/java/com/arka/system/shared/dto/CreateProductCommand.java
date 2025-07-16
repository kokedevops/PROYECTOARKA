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

/**
 * Comando para crear un nuevo producto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductCommand {
    
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
    
    @NotNull(message = "El stock inicial es obligatorio")
    @PositiveOrZero(message = "El stock inicial no puede ser negativo")
    private Integer initialStock;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @PositiveOrZero(message = "El stock mínimo debe ser positivo")
    private Integer minimumStock;
    
    private BigDecimal weight;
    
    @Size(max = 100, message = "Las dimensiones no pueden exceder 100 caracteres")
    private String dimensions;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;
}
