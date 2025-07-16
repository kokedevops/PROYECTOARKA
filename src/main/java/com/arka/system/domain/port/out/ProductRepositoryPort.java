package com.arka.system.domain.port.out;

import com.arka.system.domain.model.Product;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de productos.
 * Define las operaciones de acceso a datos para productos.
 */
public interface ProductRepositoryPort {
    
    /**
     * Guardar un producto
     * @param product Producto a guardar
     * @return Producto guardado con ID asignado
     */
    Product save(Product product);
    
    /**
     * Buscar producto por ID
     * @param id ID del producto
     * @return Optional con el producto si existe
     */
    Optional<Product> findById(Long id);
    
    /**
     * Buscar producto por SKU
     * @param sku SKU del producto
     * @return Optional con el producto si existe
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Buscar todos los productos con paginación
     * @param pageRequest Información de paginación
     * @return Lista de productos
     */
    List<Product> findAll(PageRequest pageRequest);
    
    /**
     * Buscar todos los productos activos
     * @return Lista de productos activos
     */
    List<Product> findAllActive();
    
    /**
     * Buscar productos por categoría
     * @param categoryId ID de la categoría
     * @return Lista de productos de la categoría
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * Buscar productos por texto en nombre, descripción o marca
     * @param searchText Texto a buscar
     * @return Lista de productos que coinciden
     */
    List<Product> findByNameOrDescriptionOrBrandContaining(String searchText);
    
    /**
     * Buscar productos con stock por debajo del mínimo
     * @return Lista de productos con stock bajo
     */
    List<Product> findLowStockProducts();
    
    /**
     * Actualizar stock de un producto de forma atómica
     * @param productId ID del producto
     * @param quantity Nueva cantidad
     * @return true si la actualización fue exitosa
     */
    boolean updateStock(Long productId, Integer quantity);
    
    /**
     * Decrementar stock de forma atómica (para ventas)
     * @param productId ID del producto
     * @param quantity Cantidad a decrementar
     * @return true si había suficiente stock y se decrementó
     */
    boolean decrementStock(Long productId, Integer quantity);
    
    /**
     * Incrementar stock de forma atómica (para recepciones)
     * @param productId ID del producto
     * @param quantity Cantidad a incrementar
     * @return true si la operación fue exitosa
     */
    boolean incrementStock(Long productId, Integer quantity);
    
    /**
     * Verificar si existe un producto con el SKU especificado
     * @param sku SKU a verificar
     * @return true si existe un producto con ese SKU
     */
    boolean existsBySku(String sku);
    
    /**
     * Verificar si existe un producto con el SKU especificado, excluyendo un ID
     * @param sku SKU a verificar
     * @param excludeId ID a excluir de la búsqueda
     * @return true si existe un producto con ese SKU (diferente al ID excluido)
     */
    boolean existsBySkuAndIdNot(String sku, Long excludeId);
    
    /**
     * Eliminar un producto
     * @param id ID del producto a eliminar
     */
    void deleteById(Long id);
    
    /**
     * Contar total de productos activos
     * @return Número total de productos activos
     */
    long countActiveProducts();
    
    /**
     * Verificar si existe un producto por ID
     * @param id ID del producto
     * @return true si existe
     */
    boolean existsById(Long id);
    
    /**
     * Buscar productos por categoría con paginación
     * @param categoryId ID de la categoría
     * @param pageRequest Información de paginación
     * @return Lista de productos activos de la categoría
     */
    List<Product> findByCategoryIdAndActiveTrue(Long categoryId, PageRequest pageRequest);
    
    /**
     * Buscar productos por texto con paginación
     * @param name Texto a buscar en nombre
     * @param description Texto a buscar en descripción  
     * @param brand Texto a buscar en marca
     * @param pageRequest Información de paginación
     * @return Lista de productos que coinciden
     */
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCase(
        String name, String description, String brand, PageRequest pageRequest);
}
