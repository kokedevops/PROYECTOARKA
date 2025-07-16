package com.arka.system.infrastructure.adapter.in.reactive;

import com.arka.system.domain.port.in.ProductManagementUseCase;
import com.arka.system.shared.dto.ProductDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Controlador reactivo avanzado que demuestra múltiples llamadas asíncronas
 * y manejo de backpressure con WebFlux.
 */
@RestController
@RequestMapping("/api/reactive/advanced")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Advanced Reactive Operations", description = "Operaciones reactivas avanzadas con múltiples llamadas asíncronas")
public class AdvancedReactiveController {

    private final ProductManagementUseCase productManagementUseCase;

    @GetMapping(value = "/dashboard", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Dashboard reactivo con múltiples fuentes de datos asíncronas")
    @ApiResponse(responseCode = "200", description = "Stream de datos del dashboard")
    public Flux<Map<String, Object>> getDashboardData() {
        
        log.info("Iniciando dashboard reactivo con múltiples llamadas asíncronas");
        
        // Múltiples fuentes de datos asíncronas
        Mono<List<ProductDTO>> allProductsMono = Mono.fromCallable(() -> 
                productManagementUseCase.findAll(PageRequest.of(0, 100)))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(products -> log.info("Cargados {} productos", products.size()));
        
        Mono<List<ProductDTO>> lowStockMono = Mono.fromCallable(() -> 
                productManagementUseCase.findLowStockProducts())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(products -> log.info("Productos con stock bajo: {}", products.size()));
        
        Mono<Long> totalProductsMono = allProductsMono
                .map(products -> (long) products.size())
                .doOnNext(count -> log.info("Total de productos: {}", count));
        
        // Combinar múltiples flujos de forma asíncrona
        return Flux.interval(Duration.ofSeconds(2))
                .take(10) // Limitar a 10 emisiones
                .flatMap(tick -> 
                    Mono.zip(allProductsMono, lowStockMono, totalProductsMono)
                        .map(tuple -> {
                            List<ProductDTO> allProducts = tuple.getT1();
                            List<ProductDTO> lowStockProducts = tuple.getT2();
                            Long totalProducts = tuple.getT3();
                            
                            return Map.<String, Object>of(
                                "timestamp", System.currentTimeMillis(),
                                "tick", tick,
                                "totalProducts", totalProducts,
                                "lowStockCount", lowStockProducts.size(),
                                "activeProducts", allProducts.stream().filter(p -> p.isActive()).count(),
                                "averageStock", allProducts.stream()
                                    .mapToInt(ProductDTO::getStockQuantity)
                                    .average()
                                    .orElse(0.0),
                                "lowStockProducts", lowStockProducts.stream()
                                    .limit(5)
                                    .map(ProductDTO::getSku)
                                    .toList()
                            );
                        })
                        .doOnNext(data -> log.debug("Dashboard data emitido: tick {}", data.get("tick")))
                )
                .doOnComplete(() -> log.info("Dashboard stream completado"))
                .doOnError(error -> log.error("Error en dashboard stream", error))
                .onErrorResume(error -> Flux.just(Map.of("error", error.getMessage())));
    }

    @GetMapping(value = "/products/parallel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Procesamiento paralelo de productos con backpressure")
    @ApiResponse(responseCode = "200", description = "Stream de productos procesados en paralelo")
    public Flux<ProductDTO> getProductsParallel(
            @Parameter(description = "Número de hilos paralelos")
            @RequestParam(defaultValue = "4") int parallelism) {
        
        log.info("Iniciando procesamiento paralelo de productos con {} hilos", parallelism);
        
        return Flux.fromIterable(productManagementUseCase.findAll(PageRequest.of(0, 50)))
                .parallel(parallelism) // Procesamiento en paralelo
                .runOn(Schedulers.parallel())
                .map(product -> {
                    // Simular procesamiento costoso
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    log.debug("Procesando producto: {} en hilo: {}", 
                        product.getSku(), Thread.currentThread().getName());
                    
                    return product;
                })
                .sequential() // Volver a flujo secuencial
                .delayElements(Duration.ofMillis(50)) // Control de backpressure
                .doOnNext(product -> log.debug("Producto procesado: {}", product.getSku()))
                .doOnComplete(() -> log.info("Procesamiento paralelo completado"))
                .doOnError(error -> log.error("Error en procesamiento paralelo", error));
    }

    @GetMapping(value = "/products/enriched", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Productos enriquecidos con múltiples llamadas asíncronas")
    @ApiResponse(responseCode = "200", description = "Stream de productos enriquecidos")
    public Flux<Map<String, Object>> getEnrichedProducts(
            @Parameter(description = "Categoría a filtrar (opcional)")
            @RequestParam(required = false) Long categoryId) {
        
        log.info("Iniciando enriquecimiento de productos asíncrono");
        
        List<ProductDTO> products = categoryId != null 
            ? productManagementUseCase.findByCategory(categoryId, PageRequest.of(0, 20))
            : productManagementUseCase.findAll(PageRequest.of(0, 20));
        
        return Flux.fromIterable(products)
                .flatMap(product -> 
                    // Múltiples llamadas asíncronas para enriquecer cada producto
                    Mono.zip(
                        getProductDetails(product),
                        getStockStatus(product),
                        getPriceAnalysis(product)
                    )
                    .map(tuple -> Map.<String, Object>of(
                        "product", tuple.getT1(),
                        "stockStatus", tuple.getT2(),
                        "priceAnalysis", tuple.getT3(),
                        "enrichedAt", System.currentTimeMillis()
                    ))
                    .subscribeOn(Schedulers.boundedElastic())
                )
                .delayElements(Duration.ofMillis(200)) // Control de backpressure
                .doOnNext(enriched -> log.debug("Producto enriquecido: {}", 
                    ((ProductDTO) enriched.get("product")).getSku()))
                .doOnComplete(() -> log.info("Enriquecimiento de productos completado"))
                .doOnError(error -> log.error("Error en enriquecimiento de productos", error))
                .onErrorResume(error -> Flux.just(Map.of("error", error.getMessage())));
    }

    @GetMapping(value = "/analytics/realtime", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Analíticas en tiempo real con múltiples métricas")
    @ApiResponse(responseCode = "200", description = "Stream de analíticas en tiempo real")
    public Flux<Map<String, Object>> getRealtimeAnalytics() {
        
        log.info("Iniciando analíticas en tiempo real");
        
        return Flux.interval(Duration.ofSeconds(3))
                .flatMap(tick -> {
                    // Múltiples cálculos asíncronos
                    Mono<Map<String, Object>> inventoryMetrics = calculateInventoryMetrics();
                    Mono<Map<String, Object>> performanceMetrics = calculatePerformanceMetrics();
                    Mono<Map<String, Object>> alertMetrics = calculateAlertMetrics();
                    
                    return Mono.zip(inventoryMetrics, performanceMetrics, alertMetrics)
                            .map(tuple -> {
                                Map<String, Object> combined = Map.of(
                                    "timestamp", System.currentTimeMillis(),
                                    "tick", tick,
                                    "inventory", tuple.getT1(),
                                    "performance", tuple.getT2(),
                                    "alerts", tuple.getT3()
                                );
                                
                                log.debug("Métricas calculadas para tick: {}", tick);
                                return combined;
                            });
                })
                .take(20) // Limitar emisiones
                .doOnComplete(() -> log.info("Stream de analíticas completado"))
                .doOnError(error -> log.error("Error en analíticas en tiempo real", error))
                .onErrorResume(error -> Flux.just(Map.of("error", error.getMessage())));
    }

    // Métodos auxiliares para simular llamadas asíncronas

    private Mono<ProductDTO> getProductDetails(ProductDTO product) {
        return Mono.fromCallable(() -> {
            // Simular llamada asíncrona a servicio externo
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return product;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Map<String, Object>> getStockStatus(ProductDTO product) {
        return Mono.fromCallable(() -> {
            String status = product.getStockQuantity() <= product.getMinimumStock() 
                ? "LOW" : "NORMAL";
            
            return Map.<String, Object>of(
                "status", status,
                "currentStock", product.getStockQuantity(),
                "minimumStock", product.getMinimumStock(),
                "needsReplenishment", product.getStockQuantity() <= product.getMinimumStock()
            );
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Map<String, Object>> getPriceAnalysis(ProductDTO product) {
        return Mono.fromCallable(() -> {
            double margin = product.getSalePrice().subtract(product.getPurchasePrice())
                    .divide(product.getSalePrice())
                    .doubleValue();
            
            return Map.<String, Object>of(
                "margin", margin,
                "marginCategory", margin > 0.3 ? "HIGH" : margin > 0.15 ? "MEDIUM" : "LOW",
                "salePrice", product.getSalePrice(),
                "purchasePrice", product.getPurchasePrice()
            );
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Map<String, Object>> calculateInventoryMetrics() {
        return Mono.fromCallable(() -> {
            List<ProductDTO> allProducts = productManagementUseCase.findAll(PageRequest.of(0, 100));
            List<ProductDTO> lowStock = productManagementUseCase.findLowStockProducts();
            
            return Map.<String, Object>of(
                "totalProducts", allProducts.size(),
                "lowStockProducts", lowStock.size(),
                "totalValue", allProducts.stream()
                    .mapToDouble(p -> p.getSalePrice().doubleValue() * p.getStockQuantity())
                    .sum()
            );
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Map<String, Object>> calculatePerformanceMetrics() {
        return Mono.fromCallable(() -> 
            Map.<String, Object>of(
                "responseTime", Math.random() * 100 + 50,
                "throughput", Math.random() * 1000 + 500,
                "errorRate", Math.random() * 5
            )
        ).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Map<String, Object>> calculateAlertMetrics() {
        return Mono.fromCallable(() -> {
            List<ProductDTO> lowStock = productManagementUseCase.findLowStockProducts();
            
            return Map.<String, Object>of(
                "lowStockAlerts", lowStock.size(),
                "criticalAlerts", lowStock.stream()
                    .filter(p -> p.getStockQuantity() == 0)
                    .count(),
                "alertLevel", lowStock.size() > 10 ? "HIGH" : lowStock.size() > 5 ? "MEDIUM" : "LOW"
            );
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
