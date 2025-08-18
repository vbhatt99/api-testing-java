package com.apitester.service;

import com.apitester.model.Product;
import com.apitester.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service class for product management operations
 * Contains business logic for product CRUD operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    
    /**
     * Create a new product
     */
    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());
        
        // Set default status if not provided
        if (product.getStatus() == null) {
            product.setStatus(Product.ProductStatus.AVAILABLE);
        }
        
        // Set default category if not provided
        if (product.getCategory() == null) {
            product.setCategory(Product.ProductCategory.OTHER);
        }
        
        // Validate stock quantity
        if (product.getStockQuantity() <= 0) {
            product.setStatus(Product.ProductStatus.OUT_OF_STOCK);
        }
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return savedProduct;
    }
    
    /**
     * Get product by ID
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        log.info("Fetching product by ID: {}", id);
        return productRepository.findById(id);
    }
    
    /**
     * Get all products
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }
    
    /**
     * Get products by category
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Product.ProductCategory category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategory(category);
    }
    
    /**
     * Get products by status
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByStatus(Product.ProductStatus status) {
        log.info("Fetching products by status: {}", status);
        return productRepository.findByStatus(status);
    }
    
    /**
     * Get available products
     */
    @Transactional(readOnly = true)
    public List<Product> getAvailableProducts() {
        log.info("Fetching available products");
        return productRepository.findByStatus(Product.ProductStatus.AVAILABLE);
    }
    
    /**
     * Get available products by category
     */
    @Transactional(readOnly = true)
    public List<Product> getAvailableProductsByCategory(Product.ProductCategory category) {
        log.info("Fetching available products by category: {}", category);
        return productRepository.findAvailableProductsByCategory(category);
    }
    
    /**
     * Search products by name
     */
    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        log.info("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Get products with price less than or equal to given price
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByMaxPrice(BigDecimal maxPrice) {
        log.info("Fetching products with max price: {}", maxPrice);
        return productRepository.findByPriceLessThanEqual(maxPrice);
    }
    
    /**
     * Get products with price greater than or equal to given price
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByMinPrice(BigDecimal minPrice) {
        log.info("Fetching products with min price: {}", minPrice);
        return productRepository.findByPriceGreaterThanEqual(minPrice);
    }
    
    /**
     * Get products by price range
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Fetching products with price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    /**
     * Get products by price range and category
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRangeAndCategory(BigDecimal minPrice, BigDecimal maxPrice, Product.ProductCategory category) {
        log.info("Fetching products with price range: {} - {} and category: {}", minPrice, maxPrice, category);
        return productRepository.findProductsByPriceRangeAndCategory(minPrice, maxPrice, category);
    }
    
    /**
     * Get products with low stock
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsWithLowStock() {
        log.info("Fetching products with low stock");
        return productRepository.findProductsWithLowStock();
    }
    
    /**
     * Get most expensive products
     */
    @Transactional(readOnly = true)
    public List<Product> getMostExpensiveProducts() {
        log.info("Fetching most expensive products");
        return productRepository.findMostExpensiveProducts();
    }
    
    /**
     * Get recently created products
     */
    @Transactional(readOnly = true)
    public List<Product> getRecentlyCreatedProducts() {
        log.info("Fetching recently created products");
        java.time.LocalDateTime sevenDaysAgo = java.time.LocalDateTime.now().minusDays(7);
        return productRepository.findRecentlyCreatedProducts(sevenDaysAgo);
    }
    
    /**
     * Update product
     */
    public Product updateProduct(Long id, Product productDetails) {
        log.info("Updating product with ID: {}", id);
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        
        // Update fields
        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setStockQuantity(productDetails.getStockQuantity());
        existingProduct.setCategory(productDetails.getCategory());
        
        // Update status based on stock quantity
        if (productDetails.getStockQuantity() <= 0) {
            existingProduct.setStatus(Product.ProductStatus.OUT_OF_STOCK);
        } else if (productDetails.getStatus() != null) {
            existingProduct.setStatus(productDetails.getStatus());
        }
        
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        return updatedProduct;
    }
    
    /**
     * Delete product
     */
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }
    
    /**
     * Update product stock
     */
    public Product updateProductStock(Long id, Integer newStockQuantity) {
        log.info("Updating stock for product ID: {} to quantity: {}", id, newStockQuantity);
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        
        existingProduct.setStockQuantity(newStockQuantity);
        
        // Update status based on stock quantity
        if (newStockQuantity <= 0) {
            existingProduct.setStatus(Product.ProductStatus.OUT_OF_STOCK);
        } else if (existingProduct.getStatus() == Product.ProductStatus.OUT_OF_STOCK) {
            existingProduct.setStatus(Product.ProductStatus.AVAILABLE);
        }
        
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product stock updated successfully for ID: {}", updatedProduct.getId());
        return updatedProduct;
    }
}
