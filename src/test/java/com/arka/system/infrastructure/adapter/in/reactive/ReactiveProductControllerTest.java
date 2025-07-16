package com.arka.system.infrastructure.adapter.in.reactive;

import com.arka.system.domain.port.in.ProductManagementUseCase;
import com.arka.system.shared.dto.ProductDTO;
import com.arka.system.shared.dto.CreateProductCommand;
import com.arka.system.shared.dto.UpdateProductCommand;
import com.arka.system.shared.exception.ProductNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ReactiveProductController usando StepVerifier
 */
@ExtendWith(MockitoExtension.class)
class ReactiveProductControllerTest {

    @Mock
    private ProductManagementUseCase productManagementUseCase;

    private ReactiveProductController reactiveProductController;
    private ProductDTO sampleProduct;
    private CreateProductCommand createCommand;
    private UpdateProductCommand updateCommand;

    @BeforeEach
    void setUp() {
        reactiveProductController = new ReactiveProductController(productManagementUseCase);
        
        sampleProduct = ProductDTO.builder()
                .id(1L)
                .sku("TEST-001")
                .name("Producto Test")
                .description("Descripción de prueba")
                .brand("Test Brand")
                .categoryId(1L)
                .categoryName("Test Category")
                .purchasePrice(new BigDecimal("100.00"))
                .salePrice(new BigDecimal("150.00"))
                .stockQuantity(50)
                .minimumStock(10)
                .active(true)
                .build();

        createCommand = CreateProductCommand.builder()
                .sku("NEW-001")
                .name("Nuevo Producto")
                .description("Nuevo producto de prueba")
                .brand("New Brand")
                .categoryId(1L)
                .purchasePrice(new BigDecimal("80.00"))
                .salePrice(new BigDecimal("120.00"))
                .stockQuantity(30)
                .minimumStock(5)
                .build();

        updateCommand = UpdateProductCommand.builder()
                .name("Producto Actualizado")
                .description("Descripción actualizada")
                .brand("Updated Brand")
                .categoryId(1L)
                .purchasePrice(new BigDecimal("90.00"))
                .salePrice(new BigDecimal("135.00"))
                .stockQuantity(40)
                .minimumStock(8)
                .build();
    }

    @Test
    void getAllProducts_ShouldReturnFluxOfProducts() {
        // Given
        List<ProductDTO> products = List.of(sampleProduct);
        when(productManagementUseCase.findAll(any(PageRequest.class)))
                .thenReturn(products);

        // When
        Flux<ProductDTO> result = reactiveProductController.getAllProducts(0, 10);

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .verifyComplete();

        verify(productManagementUseCase).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getAllProducts_WithDelay_ShouldEmitWithInterval() {
        // Given
        List<ProductDTO> products = List.of(sampleProduct, sampleProduct);
        when(productManagementUseCase.findAll(any(PageRequest.class)))
                .thenReturn(products);

        // When
        Flux<ProductDTO> result = reactiveProductController.getAllProducts(0, 10);

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .expectNext(sampleProduct)
                .expectComplete()
                .verify(Duration.ofSeconds(1));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Given
        when(productManagementUseCase.findById(1L)).thenReturn(sampleProduct);

        // When
        Mono<ProductDTO> result = reactiveProductController.getProductById(1L);

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .verifyComplete();

        verify(productManagementUseCase).findById(1L);
    }

    @Test
    void getProductById_WhenProductNotFound_ShouldReturnError() {
        // Given
        when(productManagementUseCase.findById(1L))
                .thenThrow(new ProductNotFoundException("Producto no encontrado"));

        // When
        Mono<ProductDTO> result = reactiveProductController.getProductById(1L);

        // Then
        StepVerifier.create(result)
                .expectError(ResponseStatusException.class)
                .verify();

        verify(productManagementUseCase).findById(1L);
    }

    @Test
    void getProductBySku_WhenProductExists_ShouldReturnProduct() {
        // Given
        when(productManagementUseCase.findBySku("TEST-001")).thenReturn(sampleProduct);

        // When
        Mono<ProductDTO> result = reactiveProductController.getProductBySku("TEST-001");

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .verifyComplete();

        verify(productManagementUseCase).findBySku("TEST-001");
    }

    @Test
    void createProduct_WithValidData_ShouldReturnCreatedProduct() {
        // Given
        when(productManagementUseCase.createProduct(createCommand)).thenReturn(sampleProduct);

        // When
        Mono<ProductDTO> result = reactiveProductController.createProduct(createCommand);

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .verifyComplete();

        verify(productManagementUseCase).createProduct(createCommand);
    }

    @Test
    void createProduct_WithInvalidData_ShouldReturnError() {
        // Given
        when(productManagementUseCase.createProduct(createCommand))
                .thenThrow(new IllegalArgumentException("Datos inválidos"));

        // When
        Mono<ProductDTO> result = reactiveProductController.createProduct(createCommand);

        // Then
        StepVerifier.create(result)
                .expectError(ResponseStatusException.class)
                .verify();

        verify(productManagementUseCase).createProduct(createCommand);
    }

    @Test
    void updateProduct_WithValidData_ShouldReturnUpdatedProduct() {
        // Given
        when(productManagementUseCase.updateProduct(1L, updateCommand)).thenReturn(sampleProduct);

        // When
        Mono<ProductDTO> result = reactiveProductController.updateProduct(1L, updateCommand);

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .verifyComplete();

        verify(productManagementUseCase).updateProduct(1L, updateCommand);
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldComplete() {
        // Given
        doNothing().when(productManagementUseCase).deleteProduct(1L);

        // When
        Mono<Void> result = reactiveProductController.deleteProduct(1L);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(productManagementUseCase).deleteProduct(1L);
    }

    @Test
    void deleteProduct_WhenProductNotFound_ShouldReturnError() {
        // Given
        doThrow(new ProductNotFoundException("Producto no encontrado"))
                .when(productManagementUseCase).deleteProduct(1L);

        // When
        Mono<Void> result = reactiveProductController.deleteProduct(1L);

        // Then
        StepVerifier.create(result)
                .expectError(ResponseStatusException.class)
                .verify();

        verify(productManagementUseCase).deleteProduct(1L);
    }

    @Test
    void getProductsByCategory_ShouldReturnFluxOfProducts() {
        // Given
        List<ProductDTO> products = List.of(sampleProduct);
        when(productManagementUseCase.findByCategory(eq(1L), any(PageRequest.class)))
                .thenReturn(products);

        // When
        Flux<ProductDTO> result = reactiveProductController.getProductsByCategory(1L, 0, 10);

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .verifyComplete();

        verify(productManagementUseCase).findByCategory(1L, PageRequest.of(0, 10));
    }

    @Test
    void getLowStockProducts_ShouldReturnFluxOfLowStockProducts() {
        // Given
        List<ProductDTO> lowStockProducts = List.of(sampleProduct);
        when(productManagementUseCase.findLowStockProducts()).thenReturn(lowStockProducts);

        // When
        Flux<ProductDTO> result = reactiveProductController.getLowStockProducts();

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .verifyComplete();

        verify(productManagementUseCase).findLowStockProducts();
    }

    @Test
    void searchProducts_ShouldReturnFluxOfMatchingProducts() {
        // Given
        List<ProductDTO> products = List.of(sampleProduct);
        when(productManagementUseCase.searchProducts(eq("test"), any(PageRequest.class)))
                .thenReturn(products);

        // When
        Flux<ProductDTO> result = reactiveProductController.searchProducts("test", 0, 10);

        // Then
        StepVerifier.create(result)
                .expectNext(sampleProduct)
                .verifyComplete();

        verify(productManagementUseCase).searchProducts("test", PageRequest.of(0, 10));
    }

    @Test
    void getAllProducts_WithEmptyResult_ShouldCompleteWithoutEmission() {
        // Given
        when(productManagementUseCase.findAll(any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        // When
        Flux<ProductDTO> result = reactiveProductController.getAllProducts(0, 10);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(productManagementUseCase).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getAllProducts_WithError_ShouldPropagateError() {
        // Given
        when(productManagementUseCase.findAll(any(PageRequest.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When
        Flux<ProductDTO> result = reactiveProductController.getAllProducts(0, 10);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(productManagementUseCase).findAll(PageRequest.of(0, 10));
    }

    @Test
    void reactiveOperations_ShouldHandleBackpressure() {
        // Given
        List<ProductDTO> manyProducts = Collections.nCopies(100, sampleProduct);
        when(productManagementUseCase.findAll(any(PageRequest.class)))
                .thenReturn(manyProducts);

        // When
        Flux<ProductDTO> result = reactiveProductController.getAllProducts(0, 100);

        // Then
        StepVerifier.create(result)
                .expectNextCount(100)
                .verifyComplete();
    }
}
