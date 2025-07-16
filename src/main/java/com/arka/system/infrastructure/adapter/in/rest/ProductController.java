package com.arka.system.infrastructure.adapter.in.rest;

import com.arka.system.domain.port.in.ProductManagementUseCase;
import com.arka.system.shared.dto.ProductDTO;
import com.arka.system.shared.dto.CreateProductCommand;
import com.arka.system.shared.dto.UpdateProductCommand;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Controlador REST para gestión de productos.
 * Adaptador de entrada que expone los casos de uso como endpoints HTTP.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ProductController {
    
    private final ProductManagementUseCase productManagementUseCase;
    
    /**
     * Crear un nuevo producto
     */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductCommand command) {
        log.info("Creating product with SKU: {}", command.getSku());
        ProductDTO createdProduct = productManagementUseCase.createProduct(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    
    /**
     * Actualizar un producto existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductCommand command) {
        log.info("Updating product with ID: {}", id);
        ProductDTO updatedProduct = productManagementUseCase.updateProduct(id, command);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Obtener un producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productManagementUseCase.getProductById(id)
            .map(product -> ResponseEntity.ok(product))
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener un producto por SKU
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable @NotBlank String sku) {
        return productManagementUseCase.getProductBySku(sku)
            .map(product -> ResponseEntity.ok(product))
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Listar todos los productos activos
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllActiveProducts() {
        List<ProductDTO> products = productManagementUseCase.getAllActiveProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Listar productos por categoría
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productManagementUseCase.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Buscar productos por texto
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam @NotBlank String q) {
        List<ProductDTO> products = productManagementUseCase.searchProducts(q);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Actualizar stock de un producto
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDTO> updateStock(
            @PathVariable Long id,
            @RequestParam @NotNull @Positive Integer quantity) {
        log.info("Updating stock for product ID: {} to quantity: {}", id, quantity);
        ProductDTO updatedProduct = productManagementUseCase.updateStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Reservar stock para una venta
     */
    @PostMapping("/{id}/reserve")
    public ResponseEntity<Void> reserveStock(
            @PathVariable Long id,
            @RequestParam @NotNull @Positive Integer quantity) {
        log.info("Reserving {} units of product ID: {}", quantity, id);
        boolean reserved = productManagementUseCase.reserveStock(id, quantity);
        if (reserved) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Stock insuficiente
        }
    }
    
    /**
     * Liberar stock reservado
     */
    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseStock(
            @PathVariable Long id,
            @RequestParam @NotNull @Positive Integer quantity) {
        log.info("Releasing {} units of product ID: {}", quantity, id);
        productManagementUseCase.releaseStock(id, quantity);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Confirmar venta
     */
    @PostMapping("/{id}/confirm-sale")
    public ResponseEntity<ProductDTO> confirmSale(
            @PathVariable Long id,
            @RequestParam @NotNull @Positive Integer quantity) {
        log.info("Confirming sale of {} units of product ID: {}", quantity, id);
        ProductDTO updatedProduct = productManagementUseCase.confirmSale(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Obtener productos con stock bajo
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts() {
        List<ProductDTO> products = productManagementUseCase.getLowStockProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Desactivar un producto
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        log.info("Deactivating product with ID: {}", id);
        productManagementUseCase.deactivateProduct(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Activar un producto
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable Long id) {
        log.info("Activating product with ID: {}", id);
        productManagementUseCase.activateProduct(id);
        return ResponseEntity.ok().build();
    }
}
