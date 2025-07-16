package com.arka.system.application.usecase;

import com.arka.system.domain.port.in.ProductManagementUseCase;
import com.arka.system.domain.port.out.ProductRepositoryPort;
import com.arka.system.domain.model.Product;
import com.arka.system.domain.model.Category;
import com.arka.system.shared.dto.ProductDTO;
import com.arka.system.shared.dto.CreateProductCommand;
import com.arka.system.shared.dto.UpdateProductCommand;
import com.arka.system.shared.exception.ProductNotFoundException;
import com.arka.system.shared.exception.DuplicateSkuException;
import com.arka.system.shared.exception.InsufficientStockException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de los casos de uso de gestión de productos.
 * Contiene la lógica de negocio para todas las operaciones relacionadas con productos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductManagementUseCaseImpl implements ProductManagementUseCase {
    
    private final ProductRepositoryPort productRepository;
    private final ProductDTOMapper productMapper;
    
    @Override
    public ProductDTO createProduct(CreateProductCommand command) {
        log.info("Creating new product with SKU: {}", command.getSku());
        
        // Validar que no exista otro producto con el mismo SKU
        if (productRepository.existsBySku(command.getSku())) {
            throw new DuplicateSkuException("Ya existe un producto con el SKU: " + command.getSku());
        }
        
        // Crear entidad de producto
        Product product = Product.builder()
            .sku(command.getSku())
            .name(command.getName())
            .description(command.getDescription())
            .brand(command.getBrand())
            .purchasePrice(command.getPurchasePrice())
            .salePrice(command.getSalePrice())
            .stockQuantity(command.getInitialStock())
            .minimumStock(command.getMinimumStock())
            .weight(command.getWeight())
            .dimensions(command.getDimensions())
            .active(true)
            .category(Category.builder().id(command.getCategoryId()).build())
            .build();
        
        Product savedProduct = productRepository.save(product);
        
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return productMapper.toDTO(savedProduct);
    }
    
    @Override
    public ProductDTO updateProduct(Long productId, UpdateProductCommand command) {
        log.info("Updating product with ID: {}", productId);
        
        Product existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + productId));
        
        // Validar SKU si cambió
        if (!existingProduct.getSku().equals(command.getSku()) && 
            productRepository.existsBySkuAndIdNot(command.getSku(), productId)) {
            throw new DuplicateSkuException("Ya existe otro producto con el SKU: " + command.getSku());
        }
        
        // Actualizar campos
        existingProduct.setSku(command.getSku());
        existingProduct.setName(command.getName());
        existingProduct.setDescription(command.getDescription());
        existingProduct.setBrand(command.getBrand());
        existingProduct.setPurchasePrice(command.getPurchasePrice());
        existingProduct.setSalePrice(command.getSalePrice());
        existingProduct.setMinimumStock(command.getMinimumStock());
        existingProduct.setWeight(command.getWeight());
        existingProduct.setDimensions(command.getDimensions());
        
        if (command.getCategoryId() != null) {
            existingProduct.setCategory(Category.builder().id(command.getCategoryId()).build());
        }
        
        Product updatedProduct = productRepository.save(existingProduct);
        
        log.info("Product updated successfully with ID: {}", productId);
        return productMapper.toDTO(updatedProduct);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductById(Long productId) {
        return productRepository.findById(productId)
            .map(productMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductBySku(String sku) {
        return productRepository.findBySku(sku)
            .map(productMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllActiveProducts() {
        return productRepository.findAllActive().stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String searchText) {
        return productRepository.findByNameOrDescriptionOrBrandContaining(searchText).stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProductDTO updateStock(Long productId, Integer quantity) {
        log.info("Updating stock for product ID: {} to quantity: {}", productId, quantity);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + productId));
        
        boolean updated = productRepository.updateStock(productId, quantity);
        if (!updated) {
            throw new RuntimeException("Error al actualizar el stock del producto");
        }
        
        // Recargar el producto actualizado
        product = productRepository.findById(productId).get();
        
        log.info("Stock updated successfully for product ID: {}", productId);
        return productMapper.toDTO(product);
    }
    
    @Override
    public boolean reserveStock(Long productId, Integer quantity) {
        log.info("Attempting to reserve {} units of product ID: {}", quantity, productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + productId));
        
        if (product.getStockQuantity() < quantity) {
            log.warn("Insufficient stock for product ID: {}. Available: {}, Requested: {}", 
                     productId, product.getStockQuantity(), quantity);
            return false;
        }
        
        boolean reserved = productRepository.decrementStock(productId, quantity);
        
        if (reserved) {
            log.info("Successfully reserved {} units of product ID: {}", quantity, productId);
        } else {
            log.warn("Failed to reserve stock for product ID: {}", productId);
        }
        
        return reserved;
    }
    
    @Override
    public void releaseStock(Long productId, Integer quantity) {
        log.info("Releasing {} units of product ID: {}", quantity, productId);
        
        boolean released = productRepository.incrementStock(productId, quantity);
        if (!released) {
            log.error("Failed to release stock for product ID: {}", productId);
            throw new RuntimeException("Error al liberar el stock del producto");
        }
        
        log.info("Successfully released {} units of product ID: {}", quantity, productId);
    }
    
    @Override
    public ProductDTO confirmSale(Long productId, Integer quantity) {
        log.info("Confirming sale of {} units of product ID: {}", quantity, productId);
        
        // En este caso, asumimos que el stock ya fue decrementado durante la reserva
        // Solo necesitamos retornar el producto actualizado
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + productId));
        
        log.info("Sale confirmed for {} units of product ID: {}", quantity, productId);
        return productMapper.toDTO(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deactivateProduct(Long productId) {
        log.info("Deactivating product with ID: {}", productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + productId));
        
        product.setActive(false);
        productRepository.save(product);
        
        log.info("Product deactivated successfully with ID: {}", productId);
    }
    
    @Override
    public void activateProduct(Long productId) {
        log.info("Activating product with ID: {}", productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + productId));
        
        product.setActive(true);
        productRepository.save(product);
        
        log.info("Product activated successfully with ID: {}", productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long productId) {
        log.debug("Finding product by ID: {}", productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + productId));
        
        return productMapper.toDTO(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductDTO findBySku(String sku) {
        log.debug("Finding product by SKU: {}", sku);
        
        Product product = productRepository.findBySku(sku)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con SKU: " + sku));
        
        return productMapper.toDTO(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll(org.springframework.data.domain.PageRequest pageRequest) {
        log.debug("Finding all products with pagination: page={}, size={}", pageRequest.getPageNumber(), pageRequest.getPageSize());
        
        return productRepository.findAll(pageRequest).stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCategory(Long categoryId, org.springframework.data.domain.PageRequest pageRequest) {
        log.debug("Finding products by category ID: {} with pagination", categoryId);
        
        return productRepository.findByCategoryIdAndActiveTrue(categoryId, pageRequest).stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String searchText, org.springframework.data.domain.PageRequest pageRequest) {
        log.debug("Searching products with text: '{}' with pagination", searchText);
        
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCase(
                searchText, searchText, searchText, pageRequest).stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findLowStockProducts() {
        log.debug("Finding products with low stock");
        
        return productRepository.findLowStockProducts().stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteProduct(Long productId) {
        log.info("Deleting product with ID: {}", productId);
        
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Producto no encontrado con ID: " + productId);
        }
        
        productRepository.deleteById(productId);
        log.info("Product deleted successfully with ID: {}", productId);
    }
}
