package com.apitester.config;

import com.apitester.model.Product;
import com.apitester.model.User;
import com.apitester.repository.ProductRepository;
import com.apitester.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Data Initializer
 * Populates the database with sample data for testing purposes
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing sample data...");
        
        // Initialize users
        initializeUsers();
        
        // Initialize products
        initializeProducts();
        
        log.info("Sample data initialization completed!");
    }
    
    private void initializeUsers() {
        if (userRepository.count() == 0) {
            log.info("Creating sample users...");
            
            User user1 = new User();
            user1.setUsername("john_doe");
            user1.setEmail("john.doe@example.com");
            user1.setFirstName("John");
            user1.setLastName("Doe");
            user1.setPassword(passwordEncoder.encode("password123"));
            user1.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(user1);
            
            User user2 = new User();
            user2.setUsername("jane_smith");
            user2.setEmail("jane.smith@example.com");
            user2.setFirstName("Jane");
            user2.setLastName("Smith");
            user2.setPassword(passwordEncoder.encode("password123"));
            user2.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(user2);
            
            User user3 = new User();
            user3.setUsername("bob_wilson");
            user3.setEmail("bob.wilson@example.com");
            user3.setFirstName("Bob");
            user3.setLastName("Wilson");
            user3.setPassword(passwordEncoder.encode("password123"));
            user3.setStatus(User.UserStatus.INACTIVE);
            userRepository.save(user3);
            
            User user4 = new User();
            user4.setUsername("alice_brown");
            user4.setEmail("alice.brown@example.com");
            user4.setFirstName("Alice");
            user4.setLastName("Brown");
            user4.setPassword(passwordEncoder.encode("password123"));
            user4.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(user4);
            
            User user5 = new User();
            user5.setUsername("charlie_davis");
            user5.setEmail("charlie.davis@example.com");
            user5.setFirstName("Charlie");
            user5.setLastName("Davis");
            user5.setPassword(passwordEncoder.encode("password123"));
            user5.setStatus(User.UserStatus.SUSPENDED);
            userRepository.save(user5);
            
            log.info("Created {} sample users", userRepository.count());
        }
    }
    
    private void initializeProducts() {
        if (productRepository.count() == 0) {
            log.info("Creating sample products...");
            
            // Electronics
            Product laptop = new Product();
            laptop.setName("MacBook Pro 16-inch");
            laptop.setDescription("Apple MacBook Pro with M2 chip, 16GB RAM, 512GB SSD");
            laptop.setPrice(new BigDecimal("2499.99"));
            laptop.setStockQuantity(15);
            laptop.setCategory(Product.ProductCategory.ELECTRONICS);
            laptop.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(laptop);
            
            Product phone = new Product();
            phone.setName("iPhone 15 Pro");
            phone.setDescription("Apple iPhone 15 Pro with A17 Pro chip, 128GB storage");
            phone.setPrice(new BigDecimal("999.99"));
            phone.setStockQuantity(25);
            phone.setCategory(Product.ProductCategory.ELECTRONICS);
            phone.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(phone);
            
            Product headphones = new Product();
            headphones.setName("Sony WH-1000XM5");
            headphones.setDescription("Wireless noise-canceling headphones with 30-hour battery life");
            headphones.setPrice(new BigDecimal("399.99"));
            headphones.setStockQuantity(8);
            headphones.setCategory(Product.ProductCategory.ELECTRONICS);
            headphones.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(headphones);
            
            // Clothing
            Product tshirt = new Product();
            tshirt.setName("Cotton T-Shirt");
            tshirt.setDescription("Comfortable 100% cotton t-shirt, available in multiple colors");
            tshirt.setPrice(new BigDecimal("24.99"));
            tshirt.setStockQuantity(50);
            tshirt.setCategory(Product.ProductCategory.CLOTHING);
            tshirt.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(tshirt);
            
            Product jeans = new Product();
            jeans.setName("Slim Fit Jeans");
            jeans.setDescription("Modern slim fit jeans with stretch fabric");
            jeans.setPrice(new BigDecimal("79.99"));
            jeans.setStockQuantity(30);
            jeans.setCategory(Product.ProductCategory.CLOTHING);
            jeans.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(jeans);
            
            // Books
            Product book1 = new Product();
            book1.setName("Clean Code");
            book1.setDescription("A Handbook of Agile Software Craftsmanship by Robert C. Martin");
            book1.setPrice(new BigDecimal("44.99"));
            book1.setStockQuantity(20);
            book1.setCategory(Product.ProductCategory.BOOKS);
            book1.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(book1);
            
            Product book2 = new Product();
            book2.setName("Design Patterns");
            book2.setDescription("Elements of Reusable Object-Oriented Software");
            book2.setPrice(new BigDecimal("54.99"));
            book2.setStockQuantity(12);
            book2.setCategory(Product.ProductCategory.BOOKS);
            book2.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(book2);
            
            // Home
            Product coffeeMaker = new Product();
            coffeeMaker.setName("Programmable Coffee Maker");
            coffeeMaker.setDescription("12-cup programmable coffee maker with auto-shutoff");
            coffeeMaker.setPrice(new BigDecimal("89.99"));
            coffeeMaker.setStockQuantity(5);
            coffeeMaker.setCategory(Product.ProductCategory.HOME);
            coffeeMaker.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(coffeeMaker);
            
            // Sports
            Product yogaMat = new Product();
            yogaMat.setName("Premium Yoga Mat");
            yogaMat.setDescription("Non-slip yoga mat with carrying strap");
            yogaMat.setPrice(new BigDecimal("34.99"));
            yogaMat.setStockQuantity(0);
            yogaMat.setCategory(Product.ProductCategory.SPORTS);
            yogaMat.setStatus(Product.ProductStatus.OUT_OF_STOCK);
            productRepository.save(yogaMat);
            
            // Food
            Product coffee = new Product();
            coffee.setName("Organic Coffee Beans");
            coffee.setDescription("Premium organic coffee beans, medium roast");
            coffee.setPrice(new BigDecimal("19.99"));
            coffee.setStockQuantity(3);
            coffee.setCategory(Product.ProductCategory.FOOD);
            coffee.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(coffee);
            
            log.info("Created {} sample products", productRepository.count());
        }
    }
}
