package com.arka.system.domain.port.in;

import com.arka.system.domain.model.Product;
import com.arka.system.shared.dto.ProductDTO;
import com.arka.system.shared.dto.CreateProductCommand;
import com.arka.system.shared.dto.UpdateProductCommand;

import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para casos de uso de gestión de productos.
 * Define las operaciones disponibles para la gestión de productos en el sistema.
 */
public interface ProductManagementUseCase {
    
    /**
     * Crear un nuevo producto
     * @param command Comando con la información del producto a crear
     * @return DTO del producto creado
     */
    ProductDTO createProduct(CreateProductCommand command);
    
    /**
     * Actualizar un producto existente
     * @param productId ID del producto a actualizar
     * @param command Comando con la información actualizada
     * @return DTO del producto actualizado
     */
    ProductDTO updateProduct(Long productId, UpdateProductCommand command);
    
    /**
     * Obtener un producto por ID
     * @param productId ID del producto
     * @return DTO del producto si existe
     */
    ProductDTO findById(Long productId);
    
    /**
     * Obtener un producto por SKU
     * @param sku SKU del producto
     * @return DTO del producto si existe
     */
    ProductDTO findBySku(String sku);
    
    /**
     * Obtener un producto por ID (Optional)
     * @param productId ID del producto
     * @return Optional con el DTO del producto si existe
     */
    Optional<ProductDTO> getProductById(Long productId);
    
    /**
     * Obtener un producto por SKU (Optional)
     * @param sku SKU del producto
     * @return Optional con el DTO del producto si existe
     */
    Optional<ProductDTO> getProductBySku(String sku);
    
    /**
     * Listar todos los productos con paginación
     * @param pageRequest Información de paginación
     * @return Lista de DTOs de productos
     */
    List<ProductDTO> findAll(PageRequest pageRequest);
    
    /**
     * Listar todos los productos activos
     * @return Lista de DTOs de productos activos
     */
    List<ProductDTO> getAllActiveProducts();
    
    /**
     * Listar productos por categoría con paginación
     * @param categoryId ID de la categoría
     * @param pageRequest Información de paginación
     * @return Lista de DTOs de productos de la categoría
     */
    List<ProductDTO> findByCategory(Long categoryId, PageRequest pageRequest);
    
    /**
     * Listar productos por categoría
     * @param categoryId ID de la categoría
     * @return Lista de DTOs de productos de la categoría
     */
    List<ProductDTO> getProductsByCategory(Long categoryId);
    
    /**
     * Buscar productos por texto con paginación
     * @param searchText Texto a buscar en nombre, descripción o marca
     * @param pageRequest Información de paginación
     * @return Lista de DTOs de productos que coinciden
     */
    List<ProductDTO> searchProducts(String searchText, PageRequest pageRequest);
    
    /**
     * Buscar productos por texto
     * @param searchText Texto a buscar en nombre, descripción o marca
     * @return Lista de DTOs de productos que coinciden
     */
    List<ProductDTO> searchProducts(String searchText);
    
    /**
     * Obtener productos con stock bajo (por debajo del mínimo)
     * @return Lista de DTOs de productos con stock bajo
     */
    List<ProductDTO> findLowStockProducts();
    
    /**
     * Eliminar un producto
     * @param productId ID del producto a eliminar
     */
    void deleteProduct(Long productId);
    
    /**
     * Actualizar stock de un producto
     * @param productId ID del producto
     * @param quantity Nueva cantidad en stock
     * @return DTO del producto actualizado
     */
    ProductDTO updateStock(Long productId, Integer quantity);
    
    /**
     * Reservar stock para una venta
     * @param productId ID del producto
     * @param quantity Cantidad a reservar
     * @return true si la reserva fue exitosa, false si no hay suficiente stock
     */
    boolean reserveStock(Long productId, Integer quantity);
    
    /**
     * Liberar stock reservado
     * @param productId ID del producto
     * @param quantity Cantidad a liberar
     */
    void releaseStock(Long productId, Integer quantity);
    
    /**
     * Confirmar venta (reducir stock definitivamente)
     * @param productId ID del producto
     * @param quantity Cantidad vendida
     * @return DTO del producto actualizado
     */
    ProductDTO confirmSale(Long productId, Integer quantity);
    
    /**
     * Obtener productos con stock bajo (por debajo del mínimo)
     * @return Lista de DTOs de productos con stock bajo
     */
    List<ProductDTO> getLowStockProducts();
    
    /**
     * Desactivar un producto
     * @param productId ID del producto a desactivar
     */
    void deactivateProduct(Long productId);
    
    /**
     * Activar un producto
     * @param productId ID del producto a activar
     */
    void activateProduct(Long productId);
}
