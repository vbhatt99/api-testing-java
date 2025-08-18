# Project Overview

Welcome to the **API Testing Java Project** - a comprehensive learning platform designed to teach API testing from fundamentals to advanced concepts using Java, Spring Boot, and RestAssured.

## 🎯 Project Purpose

This project serves as a **complete learning environment** for API testing, providing:

- **Real-world API examples** with full CRUD operations
- **Comprehensive testing framework** with RestAssured
- **Step-by-step learning modules** from basic to advanced
- **Production-ready code patterns** and best practices
- **Extensive documentation** and examples

## 🏗️ Architecture Overview

### Technology Stack

```
Frontend (Optional)
    ↓
REST APIs (Spring Boot)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Database (H2/MySQL/PostgreSQL)
```

### Core Technologies

- **Java 17**: Modern Java features and performance
- **Spring Boot 3.2**: Rapid application development
- **Spring Data JPA**: Data access and persistence
- **H2 Database**: In-memory database for development/testing
- **RestAssured 5.3**: API testing framework
- **JUnit 5**: Test execution framework
- **Maven**: Build and dependency management
- **Swagger/OpenAPI**: API documentation

### Project Structure

```
api-testing-java/
├── src/
│   ├── main/java/com/apitester/
│   │   ├── ApiTestingApplication.java     # Main application class
│   │   ├── controller/                    # REST Controllers
│   │   │   ├── UserController.java       # User management APIs
│   │   │   ├── ProductController.java    # Product management APIs
│   │   │   └── HealthController.java     # Health check APIs
│   │   ├── service/                      # Business Logic Layer
│   │   │   ├── UserService.java          # User business logic
│   │   │   └── ProductService.java       # Product business logic
│   │   ├── repository/                   # Data Access Layer
│   │   │   ├── UserRepository.java       # User data access
│   │   │   └── ProductRepository.java    # Product data access
│   │   ├── model/                        # Entity Models
│   │   │   ├── User.java                 # User entity
│   │   │   └── Product.java              # Product entity
│   │   └── config/                       # Configuration
│   │       └── DataInitializer.java      # Sample data initialization
│   ├── main/resources/                   # Configuration files
│   │   ├── application.yml               # Main configuration
│   │   └── data.sql                      # Initial data (optional)
│   └── test/java/com/apitester/          # Test Classes
│       ├── ApiTestingApplicationTests.java # Basic context test
│       └── api/                          # API Tests
│           ├── UserApiTest.java          # User API tests
│           └── ProductApiTest.java       # Product API tests
├── docs/                                 # Documentation
├── pom.xml                              # Maven configuration
└── README.md                            # Project README
```

## 📊 Domain Models

### User Management

The **User** entity represents system users with the following features:

```java
@Entity
public class User {
    private Long id;
    private String username;        // Unique username
    private String email;          // Unique email
    private String firstName;      // First name
    private String lastName;       // Last name
    private String password;       // Password (hashed in production)
    private UserStatus status;     // ACTIVE, INACTIVE, SUSPENDED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

**Key Features**:
- Unique username and email validation
- Status management (active, inactive, suspended)
- Audit fields (created/updated timestamps)
- Comprehensive search capabilities

### Product Catalog

The **Product** entity represents products in the catalog:

```java
@Entity
public class Product {
    private Long id;
    private String name;           // Product name
    private String description;    // Product description
    private BigDecimal price;      // Product price
    private Integer stockQuantity; // Available stock
    private ProductCategory category; // ELECTRONICS, CLOTHING, etc.
    private ProductStatus status;  // AVAILABLE, OUT_OF_STOCK, DISCONTINUED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

**Key Features**:
- Price and stock management
- Category-based organization
- Status tracking (available, out of stock, discontinued)
- Advanced search and filtering

## 🔌 API Endpoints

### User APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get user by username |
| GET | `/api/users/email/{email}` | Get user by email |
| GET | `/api/users/active` | Get active users only |
| GET | `/api/users/status/{status}` | Get users by status |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update existing user |
| DELETE | `/api/users/{id}` | Delete user |
| GET | `/api/users/search/firstname?firstName=John` | Search by first name |
| GET | `/api/users/search/lastname?lastName=Smith` | Search by last name |

### Product APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/category/{category}` | Get products by category |
| GET | `/api/products/status/{status}` | Get products by status |
| GET | `/api/products/available` | Get available products |
| GET | `/api/products/search?name=laptop` | Search products by name |
| GET | `/api/products/price/range?minPrice=10&maxPrice=100` | Price range search |
| GET | `/api/products/low-stock` | Get products with low stock |
| POST | `/api/products` | Create new product |
| PUT | `/api/products/{id}` | Update existing product |
| PATCH | `/api/products/{id}/stock?stockQuantity=50` | Update stock quantity |
| DELETE | `/api/products/{id}` | Delete product |

### Health Check APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Basic health check |
| GET | `/api/health/detailed` | Detailed health information |
| GET | `/api/health/ready` | Readiness check |
| GET | `/api/health/live` | Liveness check |

## 🧪 Testing Strategy

### Testing Pyramid

The project implements a comprehensive testing strategy following the testing pyramid:

```
    /\
   /  \     E2E Tests (API Tests)
  /____\    Integration Tests
 /______\   Unit Tests
```

### Test Categories

1. **Unit Tests**: Test individual components in isolation
   - Service layer business logic
   - Repository data access methods
   - Utility functions

2. **Integration Tests**: Test component interactions
   - Service + Repository integration
   - Database operations
   - Transaction management

3. **API Tests**: End-to-end API testing
   - REST endpoint functionality
   - Request/response validation
   - Error handling
   - Performance testing

4. **Performance Tests**: Load and stress testing
   - Response time validation
   - Concurrent request handling
   - Throughput testing

### Testing Tools

- **RestAssured**: API testing framework
- **JUnit 5**: Test execution
- **Hamcrest**: Assertion matchers
- **H2 Database**: In-memory database for testing
- **Spring Boot Test**: Integration testing support

## 📚 Learning Modules

### Module 1: Basic API Testing
- Setting up the testing environment
- Writing your first API test
- Understanding HTTP methods
- Basic assertions and validations

### Module 2: Advanced Testing
- Complex request/response handling
- Test data management
- Error scenario testing
- Performance testing basics

### Module 3: Test Automation
- Test suite organization
- CI/CD integration
- Test reporting
- Automated test execution

### Module 4: Performance Testing
- Response time testing
- Load testing
- Stress testing
- Performance monitoring

## 🛠️ Development Features

### Code Quality

- **Lombok**: Reduces boilerplate code
- **Validation**: Comprehensive input validation
- **Error Handling**: Centralized exception handling
- **Logging**: Structured logging with SLF4J
- **Documentation**: Swagger/OpenAPI documentation

### Database Features

- **H2 Database**: In-memory database for development
- **JPA/Hibernate**: Object-relational mapping
- **Custom Queries**: Advanced search and filtering
- **Audit Fields**: Automatic timestamp management
- **Data Initialization**: Sample data for testing

### API Features

- **RESTful Design**: Standard REST conventions
- **HTTP Status Codes**: Proper status code usage
- **Content Negotiation**: JSON request/response
- **Validation**: Input validation with detailed error messages
- **Search & Filtering**: Advanced query capabilities

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd api-testing-java
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the APIs**
   - API Documentation: http://localhost:8080/api/swagger-ui.html
   - Health Check: http://localhost:8080/api/health
   - H2 Console: http://localhost:8080/api/h2-console

5. **Run tests**
   ```bash
   mvn test
   ```

## 📊 Sample Data

The application comes with pre-loaded sample data:

### Users
- **John Doe**: Active user (john_doe@example.com)
- **Jane Smith**: Active user (jane.smith@example.com)
- **Bob Wilson**: Inactive user (bob.wilson@example.com)
- **Alice Brown**: Active user (alice.brown@example.com)
- **Charlie Davis**: Suspended user (charlie.davis@example.com)

### Products
- **Electronics**: MacBook Pro, iPhone 15 Pro, Sony Headphones
- **Clothing**: Cotton T-Shirt, Slim Fit Jeans
- **Books**: Clean Code, Design Patterns
- **Home**: Programmable Coffee Maker
- **Sports**: Premium Yoga Mat
- **Food**: Organic Coffee Beans

## 🔧 Configuration

### Application Properties

Key configuration options in `application.yml`:

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: password
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

logging:
  level:
    com.apitester: DEBUG
```

### Environment Profiles

- **Default**: Development configuration
- **Test**: Testing configuration with H2 database
- **Production**: Production-ready configuration

## 📈 Performance Characteristics

### Response Times
- **Simple GET requests**: < 100ms
- **Complex queries**: < 500ms
- **Create/Update operations**: < 200ms

### Scalability
- **Concurrent users**: 100+ simultaneous requests
- **Database connections**: Configurable connection pool
- **Memory usage**: Optimized for development/testing

## 🔒 Security Considerations

### Input Validation
- **Bean Validation**: Automatic input validation
- **SQL Injection Prevention**: Parameterized queries
- **XSS Prevention**: Input sanitization

### Authentication & Authorization
- **Ready for integration**: Prepared for JWT/OAuth
- **Role-based access**: Extensible authorization
- **Secure headers**: Security headers configuration

## 🚀 Deployment Options

### Local Development
- **H2 Database**: In-memory database
- **Hot reload**: Automatic restart on changes
- **Debug mode**: Detailed logging

### Production Deployment
- **External Database**: MySQL/PostgreSQL
- **Docker Support**: Containerized deployment
- **Environment Configuration**: Profile-based config

## 🤝 Contributing

### Development Workflow
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

### Code Standards
- **Java Coding Standards**: Follow Java conventions
- **Test Coverage**: Maintain high test coverage
- **Documentation**: Update documentation for changes
- **Code Review**: All changes require review

## 📞 Support

### Getting Help
- **Documentation**: Comprehensive docs in `/docs` folder
- **Issues**: Create issues for bugs or feature requests
- **Discussions**: Use GitHub discussions for questions

### Learning Resources
- **API Testing Guide**: Complete testing tutorial
- **RestAssured Tutorial**: Framework-specific guide
- **Best Practices**: Industry-standard practices
- **Examples**: Real-world testing scenarios

---

**Ready to start learning API testing?** 🚀

Begin with the [Quick Start Guide](./quick-start.md) to get up and running in 5 minutes!
