package com.arka.system.infrastructure.adapter.out.persistence;

import com.arka.system.domain.port.out.ProductRepositoryPort;
import com.arka.system.domain.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para persistencia de productos.
 * Implementa el puerto de salida ProductRepositoryPort.
 */
@Repository
public interface JpaProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Buscar producto por SKU
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Buscar todos los productos activos
     */
    @Query("SELECT p FROM Product p WHERE p.active = true")
    List<Product> findAllActive();
    
    /**
     * Buscar productos por categoría
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.active = true")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * Buscar productos por texto en nombre, descripción o marca
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    List<Product> findByNameOrDescriptionOrBrandContaining(@Param("searchText") String searchText);
    
    /**
     * Buscar productos con stock por debajo del mínimo
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.stockQuantity <= p.minimumStock")
    List<Product> findLowStockProducts();
    
    /**
     * Actualizar stock de un producto de forma atómica
     */
    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = :quantity WHERE p.id = :productId")
    int updateStockById(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    /**
     * Decrementar stock de forma atómica (para ventas)
     */
    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity " +
           "WHERE p.id = :productId AND p.stockQuantity >= :quantity")
    int decrementStockById(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    /**
     * Incrementar stock de forma atómica (para recepciones)
     */
    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity WHERE p.id = :productId")
    int incrementStockById(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    /**
     * Verificar si existe un producto con el SKU especificado
     */
    boolean existsBySku(String sku);
    
    /**
     * Verificar si existe un producto con el SKU especificado, excluyendo un ID
     */
    boolean existsBySkuAndIdNot(String sku, Long id);
    
    /**
     * Contar total de productos activos
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true")
    long countActiveProducts();
    
    /**
     * Buscar productos por categoría con paginación
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.active = true")
    org.springframework.data.domain.Page<Product> findByCategoryIdAndActiveTrue(
        @Param("categoryId") Long categoryId, 
        org.springframework.data.domain.Pageable pageable);
    
    /**
     * Buscar productos por texto con paginación
     */
    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%')))")
    org.springframework.data.domain.Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCase(
        @Param("name") String name,
        @Param("description") String description, 
        @Param("brand") String brand,
        org.springframework.data.domain.Pageable pageable);
}
