package com.arka.system.infrastructure.adapter.in.reactive;

import com.arka.system.domain.port.in.ProductManagementUseCase;
import com.arka.system.shared.dto.ProductDTO;
import com.arka.system.shared.dto.CreateProductCommand;
import com.arka.system.shared.dto.UpdateProductCommand;
import com.arka.system.shared.exception.ProductNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

import java.time.Duration;

/**
 * Controlador reactivo para gestión de productos usando WebFlux.
 * Proporciona endpoints no bloqueantes para operaciones de productos.
 */
@RestController
@RequestMapping("/api/reactive/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reactive Products", description = "API reactiva para gestión de productos")
public class ReactiveProductController {

    private final ProductManagementUseCase productManagementUseCase;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Obtener todos los productos de forma reactiva")
    @ApiResponse(responseCode = "200", description = "Stream de productos")
    public Flux<ProductDTO> getAllProducts(
            @Parameter(description = "Número de página")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Obteniendo productos de forma reactiva - página: {}, tamaño: {}", page, size);
        
        return Flux.fromIterable(productManagementUseCase.findAll(PageRequest.of(page, size)))
                .delayElements(Duration.ofMillis(100)) // Simular streaming
                .doOnNext(product -> log.debug("Enviando producto: {}", product.getSku()))
                .doOnComplete(() -> log.info("Stream de productos completado"))
                .doOnError(error -> log.error("Error en stream de productos", error));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID de forma reactiva")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public Mono<ProductDTO> getProductById(
            @Parameter(description = "ID del producto")
            @PathVariable Long id) {
        
        log.info("Buscando producto reactivo con ID: {}", id);
        
        return Mono.fromCallable(() -> productManagementUseCase.findById(id))
                .doOnSuccess(product -> log.info("Producto encontrado: {}", product.getSku()))
                .doOnError(error -> log.error("Error buscando producto con ID: {}", id, error))
                .onErrorMap(ProductNotFoundException.class, 
                    ex -> new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Obtener producto por SKU de forma reactiva")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public Mono<ProductDTO> getProductBySku(
            @Parameter(description = "SKU del producto")
            @PathVariable String sku) {
        
        log.info("Buscando producto reactivo con SKU: {}", sku);
        
        return Mono.fromCallable(() -> productManagementUseCase.findBySku(sku))
                .doOnSuccess(product -> log.info("Producto encontrado: {}", product.getSku()))
                .doOnError(error -> log.error("Error buscando producto con SKU: {}", sku, error))
                .onErrorMap(ProductNotFoundException.class, 
                    ex -> new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nuevo producto de forma reactiva")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public Mono<ProductDTO> createProduct(
            @Parameter(description = "Datos del producto a crear")
            @Valid @RequestBody CreateProductCommand command) {
        
        log.info("Creando producto reactivo con SKU: {}", command.getSku());
        
        return Mono.fromCallable(() -> productManagementUseCase.createProduct(command))
                .doOnSuccess(product -> log.info("Producto creado exitosamente: {}", product.getSku()))
                .doOnError(error -> log.error("Error creando producto", error))
                .onErrorMap(IllegalArgumentException.class,
                    ex -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente de forma reactiva")
    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public Mono<ProductDTO> updateProduct(
            @Parameter(description = "ID del producto")
            @PathVariable Long id,
            @Parameter(description = "Datos del producto a actualizar")
            @Valid @RequestBody UpdateProductCommand command) {
        
        log.info("Actualizando producto reactivo con ID: {}", id);
        
        return Mono.fromCallable(() -> productManagementUseCase.updateProduct(id, command))
                .doOnSuccess(product -> log.info("Producto actualizado exitosamente: {}", product.getSku()))
                .doOnError(error -> log.error("Error actualizando producto con ID: {}", id, error))
                .onErrorMap(ProductNotFoundException.class,
                    ex -> new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage()))
                .onErrorMap(IllegalArgumentException.class,
                    ex -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar producto de forma reactiva")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public Mono<Void> deleteProduct(
            @Parameter(description = "ID del producto")
            @PathVariable Long id) {
        
        log.info("Eliminando producto reactivo con ID: {}", id);
        
        return Mono.fromRunnable(() -> productManagementUseCase.deleteProduct(id))
                .doOnSuccess(result -> log.info("Producto eliminado exitosamente con ID: {}", id))
                .doOnError(error -> log.error("Error eliminando producto con ID: {}", id, error))
                .onErrorMap(ProductNotFoundException.class,
                    ex -> new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage()))
                .then();
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Obtener productos por categoría de forma reactiva")
    @ApiResponse(responseCode = "200", description = "Stream de productos de la categoría")
    public Flux<ProductDTO> getProductsByCategory(
            @Parameter(description = "ID de la categoría")
            @PathVariable Long categoryId,
            @Parameter(description = "Número de página")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Obteniendo productos reactivos por categoría: {}", categoryId);
        
        return Flux.fromIterable(productManagementUseCase.findByCategory(categoryId, PageRequest.of(page, size)))
                .delayElements(Duration.ofMillis(50))
                .doOnNext(product -> log.debug("Enviando producto de categoría {}: {}", categoryId, product.getSku()))
                .doOnComplete(() -> log.info("Stream de productos por categoría {} completado", categoryId))
                .doOnError(error -> log.error("Error en stream de productos por categoría {}", categoryId, error));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Obtener productos con stock bajo de forma reactiva")
    @ApiResponse(responseCode = "200", description = "Stream de productos con stock bajo")
    public Flux<ProductDTO> getLowStockProducts() {
        
        log.info("Obteniendo productos reactivos con stock bajo");
        
        return Flux.fromIterable(productManagementUseCase.findLowStockProducts())
                .delayElements(Duration.ofMillis(200)) // Delay más largo para simular procesamiento
                .doOnNext(product -> log.debug("Producto con stock bajo: {} (stock: {})", 
                    product.getSku(), product.getStockQuantity()))
                .doOnComplete(() -> log.info("Stream de productos con stock bajo completado"))
                .doOnError(error -> log.error("Error en stream de productos con stock bajo", error));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar productos de forma reactiva")
    @ApiResponse(responseCode = "200", description = "Stream de productos que coinciden con la búsqueda")
    public Flux<ProductDTO> searchProducts(
            @Parameter(description = "Término de búsqueda")
            @RequestParam String term,
            @Parameter(description = "Número de página")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Búsqueda reactiva de productos con término: {}", term);
        
        return Flux.fromIterable(productManagementUseCase.searchProducts(term, PageRequest.of(page, size)))
                .delayElements(Duration.ofMillis(75))
                .doOnNext(product -> log.debug("Producto encontrado en búsqueda: {}", product.getSku()))
                .doOnComplete(() -> log.info("Búsqueda reactiva completada para término: {}", term))
                .doOnError(error -> log.error("Error en búsqueda reactiva para término: {}", term, error));
    }
}
