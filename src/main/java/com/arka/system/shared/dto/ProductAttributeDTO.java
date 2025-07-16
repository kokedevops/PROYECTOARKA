package com.arka.system.shared.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferencia de datos de atributos de productos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre del atributo es obligatorio")
    @Size(max = 100, message = "El nombre del atributo no puede exceder 100 caracteres")
    private String attributeName;
    
    @NotBlank(message = "El valor del atributo es obligatorio")
    @Size(max = 200, message = "El valor del atributo no puede exceder 200 caracteres")
    private String attributeValue;
}
