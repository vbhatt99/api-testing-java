package com.apitester.controller;

import com.apitester.model.Product;
import com.apitester.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Product Management
 * Provides endpoints for product CRUD operations
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * Create a new product
     */
    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product details", required = true)
            @Valid @RequestBody Product product) {
        log.info("POST /products - Creating product: {}", product.getName());
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    
    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        log.info("GET /products/{} - Fetching product by ID", id);
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all products
     */
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("GET /products - Fetching all products");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by category
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieves products by category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "Product category", required = true)
            @PathVariable Product.ProductCategory category) {
        log.info("GET /products/category/{} - Fetching products by category", category);
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get products by status", description = "Retrieves products by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getProductsByStatus(
            @Parameter(description = "Product status", required = true)
            @PathVariable Product.ProductStatus status) {
        log.info("GET /products/status/{} - Fetching products by status", status);
        List<Product> products = productService.getProductsByStatus(status);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get available products
     */
    @GetMapping("/available")
    @Operation(summary = "Get available products", description = "Retrieves all available products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available products retrieved successfully")
    })
    public ResponseEntity<List<Product>> getAvailableProducts() {
        log.info("GET /products/available - Fetching available products");
        List<Product> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get available products by category
     */
    @GetMapping("/available/category/{category}")
    @Operation(summary = "Get available products by category", description = "Retrieves available products by category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getAvailableProductsByCategory(
            @Parameter(description = "Product category", required = true)
            @PathVariable Product.ProductCategory category) {
        log.info("GET /products/available/category/{} - Fetching available products by category", category);
        List<Product> products = productService.getAvailableProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Search products by name
     */
    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Searches products by name (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> searchProductsByName(
            @Parameter(description = "Product name to search for", required = true)
            @RequestParam String name) {
        log.info("GET /products/search?name={} - Searching products by name", name);
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by maximum price
     */
    @GetMapping("/price/max/{maxPrice}")
    @Operation(summary = "Get products by maximum price", description = "Retrieves products with price less than or equal to the specified amount")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getProductsByMaxPrice(
            @Parameter(description = "Maximum price", required = true)
            @PathVariable BigDecimal maxPrice) {
        log.info("GET /products/price/max/{} - Fetching products by max price", maxPrice);
        List<Product> products = productService.getProductsByMaxPrice(maxPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by minimum price
     */
    @GetMapping("/price/min/{minPrice}")
    @Operation(summary = "Get products by minimum price", description = "Retrieves products with price greater than or equal to the specified amount")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getProductsByMinPrice(
            @Parameter(description = "Minimum price", required = true)
            @PathVariable BigDecimal minPrice) {
        log.info("GET /products/price/min/{} - Fetching products by min price", minPrice);
        List<Product> products = productService.getProductsByMinPrice(minPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by price range
     */
    @GetMapping("/price/range")
    @Operation(summary = "Get products by price range", description = "Retrieves products within a price range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice) {
        log.info("GET /products/price/range?minPrice={}&maxPrice={} - Fetching products by price range", minPrice, maxPrice);
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by price range and category
     */
    @GetMapping("/price/range/category/{category}")
    @Operation(summary = "Get products by price range and category", description = "Retrieves products within a price range and category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getProductsByPriceRangeAndCategory(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice,
            @Parameter(description = "Product category", required = true)
            @PathVariable Product.ProductCategory category) {
        log.info("GET /products/price/range/category/{}?minPrice={}&maxPrice={} - Fetching products by price range and category", 
                category, minPrice, maxPrice);
        List<Product> products = productService.getProductsByPriceRangeAndCategory(minPrice, maxPrice, category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products with low stock
     */
    @GetMapping("/low-stock")
    @Operation(summary = "Get products with low stock", description = "Retrieves products with stock quantity less than 10")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getProductsWithLowStock() {
        log.info("GET /products/low-stock - Fetching products with low stock");
        List<Product> products = productService.getProductsWithLowStock();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get most expensive products
     */
    @GetMapping("/most-expensive")
    @Operation(summary = "Get most expensive products", description = "Retrieves the most expensive products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getMostExpensiveProducts() {
        log.info("GET /products/most-expensive - Fetching most expensive products");
        List<Product> products = productService.getMostExpensiveProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get recently created products
     */
    @GetMapping("/recent")
    @Operation(summary = "Get recently created products", description = "Retrieves products created in the last 30 days")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found")
    })
    public ResponseEntity<List<Product>> getRecentlyCreatedProducts() {
        log.info("GET /products/recent - Fetching recently created products");
        List<Product> products = productService.getRecentlyCreatedProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Update product
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated product details", required = true)
            @Valid @RequestBody Product productDetails) {
        log.info("PUT /products/{} - Updating product", id);
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Update product stock
     */
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock", description = "Updates the stock quantity of a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product stock updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> updateProductStock(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New stock quantity", required = true)
            @RequestParam Integer stockQuantity) {
        log.info("PATCH /products/{}/stock?stockQuantity={} - Updating product stock", id, stockQuantity);
        try {
            Product updatedProduct = productService.updateProductStock(id, stockQuantity);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete product
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        log.info("DELETE /products/{} - Deleting product", id);
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
