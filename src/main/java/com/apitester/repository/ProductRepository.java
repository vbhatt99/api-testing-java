package com.apitester.repository;

import com.apitester.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Product entity
 * Provides data access methods for product catalog management
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Find products by category
     */
    List<Product> findByCategory(Product.ProductCategory category);
    
    /**
     * Find products by status
     */
    List<Product> findByStatus(Product.ProductStatus status);
    
    /**
     * Find products by name containing the given string (case-insensitive)
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products with price less than or equal to the given price
     */
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    
    /**
     * Find products with price greater than or equal to the given price
     */
    List<Product> findByPriceGreaterThanEqual(BigDecimal price);
    
    /**
     * Find products with stock quantity greater than 0
     */
    List<Product> findByStockQuantityGreaterThan(Integer quantity);
    
    /**
     * Find products by category and status
     */
    List<Product> findByCategoryAndStatus(Product.ProductCategory category, Product.ProductStatus status);
    
    /**
     * Find products with price between min and max
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Custom query to find available products in a specific category
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.status = 'AVAILABLE'")
    List<Product> findAvailableProductsByCategory(@Param("category") Product.ProductCategory category);
    
    /**
     * Custom query to find products with low stock (less than 10 items)
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < 10 AND p.status = 'AVAILABLE'")
    List<Product> findProductsWithLowStock();
    
    /**
     * Custom query to find products by price range and category
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.category = :category")
    List<Product> findProductsByPriceRangeAndCategory(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("category") Product.ProductCategory category);
    
    /**
     * Custom query to find the most expensive product
     */
    @Query("SELECT p FROM Product p WHERE p.price = (SELECT MAX(price) FROM Product)")
    List<Product> findMostExpensiveProducts();
    
    /**
     * Custom query to find products created in the last 7 days
     */
    @Query("SELECT p FROM Product p WHERE p.createdAt >= :startDate")
    List<Product> findRecentlyCreatedProducts(@Param("startDate") java.time.LocalDateTime startDate);
}
