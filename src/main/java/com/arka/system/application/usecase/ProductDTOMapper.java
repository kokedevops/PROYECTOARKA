package com.arka.system.application.usecase;

import com.arka.system.domain.model.Product;
import com.arka.system.shared.dto.ProductDTO;
import com.arka.system.shared.dto.CategoryDTO;
import com.arka.system.shared.dto.ProductAttributeDTO;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversión entre entidades Product y DTOs.
 */
@Component
public class ProductDTOMapper {
    
    /**
     * Convierte una entidad Product a ProductDTO
     */
    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductDTO.builder()
            .id(product.getId())
            .sku(product.getSku())
            .name(product.getName())
            .description(product.getDescription())
            .brand(product.getBrand())
            .purchasePrice(product.getPurchasePrice())
            .salePrice(product.getSalePrice())
            .stockQuantity(product.getStockQuantity())
            .minimumStock(product.getMinimumStock())
            .weight(product.getWeight())
            .dimensions(product.getDimensions())
            .active(product.getActive())
            .category(toCategoryDTO(product.getCategory()))
            .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
            .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .attributes(toAttributeDTOList(product.getAttributes()))
            .build();
    }
    
    /**
     * Convierte una lista de entidades Product a lista de ProductDTO
     */
    public List<ProductDTO> toDTOList(List<Product> products) {
        if (products == null) {
            return null;
        }
        
        return products.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Convierte CategoryDTO (método auxiliar)
     */
    private CategoryDTO toCategoryDTO(com.arka.system.domain.model.Category category) {
        if (category == null) {
            return null;
        }
        
        return CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .active(category.getActive())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();
    }
    
    /**
     * Convierte lista de ProductAttribute a lista de ProductAttributeDTO
     */
    private List<ProductAttributeDTO> toAttributeDTOList(List<com.arka.system.domain.model.ProductAttribute> attributes) {
        if (attributes == null) {
            return null;
        }
        
        return attributes.stream()
            .map(this::toAttributeDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Convierte ProductAttribute a ProductAttributeDTO
     */
    private ProductAttributeDTO toAttributeDTO(com.arka.system.domain.model.ProductAttribute attribute) {
        if (attribute == null) {
            return null;
        }
        
        return ProductAttributeDTO.builder()
            .id(attribute.getId())
            .attributeName(attribute.getAttributeName())
            .attributeValue(attribute.getAttributeValue())
            .build();
    }
}
