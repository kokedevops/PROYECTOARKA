package com.arka.system.infrastructure.adapter.out.persistence;

import com.arka.system.domain.port.out.ProductRepositoryPort;
import com.arka.system.domain.model.Product;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para productos.
 * Implementa el puerto de salida ProductRepositoryPort usando JPA.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    
    private final JpaProductRepository jpaProductRepository;
    
    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return jpaProductRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findBySku(String sku) {
        return jpaProductRepository.findBySku(sku);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllActive() {
        return jpaProductRepository.findAllActive();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(Long categoryId) {
        return jpaProductRepository.findByCategoryId(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByNameOrDescriptionOrBrandContaining(String searchText) {
        return jpaProductRepository.findByNameOrDescriptionOrBrandContaining(searchText);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findLowStockProducts() {
        return jpaProductRepository.findLowStockProducts();
    }
    
    @Override
    public boolean updateStock(Long productId, Integer quantity) {
        int updatedRows = jpaProductRepository.updateStockById(productId, quantity);
        return updatedRows > 0;
    }
    
    @Override
    public boolean decrementStock(Long productId, Integer quantity) {
        int updatedRows = jpaProductRepository.decrementStockById(productId, quantity);
        return updatedRows > 0;
    }
    
    @Override
    public boolean incrementStock(Long productId, Integer quantity) {
        int updatedRows = jpaProductRepository.incrementStockById(productId, quantity);
        return updatedRows > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return jpaProductRepository.existsBySku(sku);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsBySkuAndIdNot(String sku, Long excludeId) {
        return jpaProductRepository.existsBySkuAndIdNot(sku, excludeId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll(org.springframework.data.domain.PageRequest pageRequest) {
        return jpaProductRepository.findAll(pageRequest).getContent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return jpaProductRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryIdAndActiveTrue(Long categoryId, org.springframework.data.domain.PageRequest pageRequest) {
        return jpaProductRepository.findByCategoryIdAndActiveTrue(categoryId, pageRequest).getContent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCase(
            String name, String description, String brand, org.springframework.data.domain.PageRequest pageRequest) {
        return jpaProductRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCase(
            name, description, brand, pageRequest).getContent();
    }
    
    @Override
    public void deleteById(Long id) {
        jpaProductRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveProducts() {
        return jpaProductRepository.countActiveProducts();
    }
}
