package com.arka.system.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad que representa los atributos adicionales de un producto.
 * Permite especificar características específicas como color, modelo, etc.
 */
@Entity
@Table(name = "product_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del atributo es obligatorio")
    @Size(max = 100, message = "El nombre del atributo no puede exceder 100 caracteres")
    @Column(name = "attribute_name", nullable = false, length = 100)
    private String attributeName;
    
    @NotBlank(message = "El valor del atributo es obligatorio")
    @Size(max = 200, message = "El valor del atributo no puede exceder 200 caracteres")
    @Column(name = "attribute_value", nullable = false, length = 200)
    private String attributeValue;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
