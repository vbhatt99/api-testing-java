# Services Explained

This document provides a detailed explanation of all service layers in the API Testing Java project. Services are the core business logic components that handle data processing, validation, and business rules.

## 📋 Table of Contents

- [Overview](#overview)
- [Service Architecture](#service-architecture)
- [UserService](#userservice)
- [ProductService](#productservice)
- [Service Best Practices](#service-best-practices)
- [Transaction Management](#transaction-management)
- [Error Handling](#error-handling)
- [Logging](#logging)

## 🏗️ Overview

Services in this project follow the **Service Layer Pattern**, which encapsulates business logic and provides a clean separation between controllers and data access layers. Each service is responsible for:

- Business logic implementation
- Data validation
- Transaction management
- Error handling
- Logging

## 🏛️ Service Architecture

```
Controller Layer (REST APIs)
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (Data Access)
        ↓
Database Layer (H2/MySQL/PostgreSQL)
```

### Key Principles

1. **Single Responsibility**: Each service handles one domain area
2. **Dependency Injection**: Services are injected into controllers
3. **Transaction Management**: Automatic transaction handling
4. **Exception Handling**: Centralized error management
5. **Logging**: Comprehensive logging for debugging

## 👤 UserService

The `UserService` manages all user-related business operations.

### Location
```java
src/main/java/com/apitester/service/UserService.java
```

### Key Methods

#### 1. User Creation
```java
public User createUser(User user)
```
**Purpose**: Creates a new user with validation
**Business Rules**:
- Username must be unique
- Email must be unique
- Password must meet minimum requirements
- Default status is ACTIVE

**Validation Logic**:
```java
// Check if username already exists
if (userRepository.existsByUsername(user.getUsername())) {
    throw new RuntimeException("Username already exists: " + user.getUsername());
}

// Check if email already exists
if (userRepository.existsByEmail(user.getEmail())) {
    throw new RuntimeException("Email already exists: " + user.getEmail());
}
```

#### 2. User Retrieval
```java
public Optional<User> getUserById(Long id)
public Optional<User> getUserByUsername(String username)
public Optional<User> getUserByEmail(String email)
public List<User> getAllUsers()
```

**Purpose**: Retrieve users by various criteria
**Features**:
- Returns `Optional` for single user queries
- Handles null cases gracefully
- Logs all operations

#### 3. User Updates
```java
public User updateUser(Long id, User userDetails)
```

**Purpose**: Updates existing user information
**Business Rules**:
- Validates user exists
- Checks for username/email conflicts
- Preserves existing password if not provided
- Updates timestamps automatically

#### 4. User Search
```java
public List<User> searchUsersByFirstName(String firstName)
public List<User> searchUsersByLastName(String lastName)
public List<User> getUsersByStatus(UserStatus status)
public List<User> getActiveUsers()
```

**Purpose**: Advanced user search functionality
**Features**:
- Case-insensitive search
- Status-based filtering
- Custom query methods

### Transaction Management

```java
@Service
@Transactional
public class UserService {
    
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        // Read-only transaction for better performance
    }
    
    public User createUser(User user) {
        // Full transaction with write operations
    }
}
```

## 📦 ProductService

The `ProductService` manages all product-related business operations.

### Location
```java
src/main/java/com/apitester/service/ProductService.java
```

### Key Methods

#### 1. Product Creation
```java
public Product createProduct(Product product)
```

**Purpose**: Creates a new product with validation
**Business Rules**:
- Sets default status to AVAILABLE
- Sets default category to OTHER
- Automatically sets status to OUT_OF_STOCK if quantity is 0

#### 2. Product Retrieval
```java
public Optional<Product> getProductById(Long id)
public List<Product> getAllProducts()
public List<Product> getProductsByCategory(ProductCategory category)
public List<Product> getAvailableProducts()
```

**Purpose**: Retrieve products with various filters
**Features**:
- Category-based filtering
- Status-based filtering
- Availability checking

#### 3. Product Search
```java
public List<Product> searchProductsByName(String name)
public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice)
public List<Product> getProductsWithLowStock()
public List<Product> getMostExpensiveProducts()
```

**Purpose**: Advanced product search and analytics
**Features**:
- Name-based search (case-insensitive)
- Price range filtering
- Business intelligence queries

#### 4. Stock Management
```java
public Product updateProductStock(Long id, Integer newStockQuantity)
```

**Purpose**: Updates product stock levels
**Business Rules**:
- Automatically updates status based on stock
- Prevents negative stock quantities
- Logs all stock changes

### Advanced Queries

```java
// Custom repository methods used by service
public List<Product> findAvailableProductsByCategory(ProductCategory category)
public List<Product> findProductsWithLowStock()
public List<Product> findMostExpensiveProducts()
public List<Product> findRecentlyCreatedProducts()
```

## 🔧 Service Best Practices

### 1. Dependency Injection
```java
@Service
@RequiredArgsConstructor  // Lombok annotation for constructor injection
public class UserService {
    private final UserRepository userRepository;  // Final field for immutability
}
```

### 2. Method Naming Conventions
- **Create**: `create*()`
- **Read**: `get*()`, `find*()`, `search*()`
- **Update**: `update*()`
- **Delete**: `delete*()`

### 3. Return Types
- **Single Entity**: `Optional<Entity>` or `Entity`
- **Multiple Entities**: `List<Entity>`
- **Void Operations**: `void` (for delete operations)

### 4. Exception Handling
```java
public User updateUser(Long id, User userDetails) {
    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    // ... rest of the method
}
```

## 💾 Transaction Management

### Read-Only Transactions
```java
@Transactional(readOnly = true)
public List<User> getAllUsers() {
    return userRepository.findAll();
}
```

**Benefits**:
- Better performance
- No write locks
- Optimized for read operations

### Full Transactions
```java
@Transactional
public User createUser(User user) {
    // Validation logic
    User savedUser = userRepository.save(user);
    // Additional business logic
    return savedUser;
}
```

**Features**:
- Automatic rollback on exceptions
- ACID compliance
- Proper isolation levels

## ⚠️ Error Handling

### Business Logic Exceptions
```java
// Duplicate username check
if (userRepository.existsByUsername(user.getUsername())) {
    throw new RuntimeException("Username already exists: " + user.getUsername());
}

// Resource not found
User existingUser = userRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
```

### Validation Exceptions
- Handled by Spring's `@Valid` annotation
- Automatic HTTP 400 responses
- Detailed validation messages

## 📝 Logging

### Logging Strategy
```java
@Slf4j  // Lombok annotation for logging
public class UserService {
    
    public User createUser(User user) {
        log.info("Creating new user with username: {}", user.getUsername());
        
        // Business logic
        
        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }
}
```

### Log Levels
- **INFO**: Business operations (create, update, delete)
- **DEBUG**: Detailed method execution
- **ERROR**: Exception handling
- **WARN**: Business rule violations

## 🔄 Service Lifecycle

### 1. Initialization
- Spring creates service beans
- Dependencies are injected
- Service is ready for use

### 2. Method Execution
- Request comes from controller
- Service validates input
- Business logic is executed
- Database operations are performed
- Response is returned

### 3. Transaction Management
- Transaction begins
- Database operations execute
- Transaction commits (or rolls back on error)

## 🧪 Testing Services

### Unit Testing
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void createUser_ShouldCreateUser_WhenValidData() {
        // Test implementation
    }
}
```

### Integration Testing
```java
@SpringBootTest
@Transactional
class UserServiceIntegrationTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    void createUser_ShouldPersistToDatabase() {
        // Integration test implementation
    }
}
```

## 📊 Performance Considerations

### 1. Lazy Loading
- Use `@Transactional(readOnly = true)` for read operations
- Avoid N+1 query problems
- Use appropriate fetch strategies

### 2. Caching
- Consider caching frequently accessed data
- Use Spring's `@Cacheable` annotation
- Implement cache invalidation strategies

### 3. Batch Operations
- Use batch inserts for large datasets
- Implement pagination for large result sets
- Consider async operations for heavy processing

## 🔮 Future Enhancements

### 1. Event-Driven Architecture
```java
@EventListener
public void handleUserCreatedEvent(UserCreatedEvent event) {
    // Handle user creation events
}
```

### 2. Circuit Breaker Pattern
```java
@CircuitBreaker(name = "userService")
public User getUserById(Long id) {
    // Implementation with circuit breaker
}
```

### 3. Caching Layer
```java
@Cacheable("users")
public Optional<User> getUserById(Long id) {
    // Cached implementation
}
```

---

**Next Steps**: 
- Read [Controllers Guide](./controllers-guide.md) to understand how services are used
- Review [Repository Layer](./repository-layer.md) for data access patterns
- Study [API Testing Guide](./api-testing-guide.md) for testing strategies
