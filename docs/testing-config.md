# Test Configuration Guide

This document provides comprehensive information about test configuration, setup, and best practices for the API Testing Java project.

## 📋 Table of Contents

- [Overview](#overview)
- [Test Environment Setup](#test-environment-setup)
- [Test Configuration Files](#test-configuration-files)
- [RestAssured Configuration](#restassured-configuration)
- [Test Data Management](#test-data-management)
- [Test Categories](#test-categories)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Best Practices](#best-practices)

## 🏗️ Overview

The project uses a comprehensive testing strategy with multiple layers:

- **Unit Tests**: Testing individual components in isolation
- **Integration Tests**: Testing component interactions
- **API Tests**: End-to-end API testing with RestAssured
- **Performance Tests**: Load and stress testing

## 🔧 Test Environment Setup

### Prerequisites

1. **Java 17+**
2. **Maven 3.6+**
3. **IDE Support** (IntelliJ IDEA, Eclipse, VS Code)

### Dependencies

The testing dependencies are defined in `pom.xml`:

```xml
<!-- Testing Dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>json-schema-validator</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>
```

## 📁 Test Configuration Files

### 1. Test Application Properties

Create `src/test/resources/application-test.yml`:

```yaml
# Test-specific configuration
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: ${DB_USERNAME:sa}
    password: ${DB_PASSWORD:changeme123}
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
    properties:
      hibernate:
        format_sql: false
  
  h2:
    console:
      enabled: false

# Test logging configuration
logging:
  level:
    com.apitester: INFO
    org.springframework.web: WARN
    org.hibernate.SQL: WARN

# Test server configuration
server:
  port: 0  # Random port for tests
```

### 2. Test Data Files

#### Sample Test Data JSON

Create `src/test/resources/test-data/users.json`:

```json
{
  "validUser": {
    "username": "testuser",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "password": "[hashed password - use passwordEncoder.encode()]",
    "status": "ACTIVE"
  },
  "invalidUser": {
    "username": "",
    "email": "invalid-email",
    "firstName": "",
    "lastName": "",
    "password": "123"
  }
}
```

Create `src/test/resources/test-data/products.json`:

```json
{
  "validProduct": {
    "name": "Test Product",
    "description": "Test product description",
    "price": 99.99,
    "stockQuantity": 10,
    "category": "ELECTRONICS",
    "status": "AVAILABLE"
  },
  "invalidProduct": {
    "name": "",
    "price": -10.0,
    "stockQuantity": -5
  }
}
```

## 🚀 RestAssured Configuration

### Base Test Configuration

Create `src/test/java/com/apitester/config/RestAssuredConfig.java`:

```java
package com.apitester.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class RestAssuredConfig {

    @LocalServerPort
    protected int port;

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeAll
    static void setUp() {
        // Request specification
        requestSpec = new RequestSpecBuilder()
            .setContentType("application/json")
            .setAccept("application/json")
            .build();

        // Response specification
        responseSpec = new ResponseSpecBuilder()
            .expectResponseTime(lessThan(3000L)) // 3 seconds max
            .expectContentType("application/json")
            .build();

        // Global RestAssured configuration
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
    }

    protected void setBasePath(String basePath) {
        RestAssured.port = port;
        RestAssured.basePath = basePath;
    }
}
```

### Test Utilities

Create `src/test/java/com/apitester/util/TestUtils.java`:

```java
package com.apitester.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Load test data from JSON file
     */
    public static Map<String, Object> loadTestData(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("test-data/" + fileName);
        String jsonContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        return objectMapper.readValue(jsonContent, Map.class);
    }

    /**
     * Extract ID from response
     */
    public static Long extractId(Response response) {
        return response.jsonPath().getLong("id");
    }

    /**
     * Generate unique username
     */
    public static String generateUniqueUsername() {
        return "testuser_" + System.currentTimeMillis();
    }

    /**
     * Generate unique email
     */
    public static String generateUniqueEmail() {
        return "test" + System.currentTimeMillis() + "@example.com";
    }

    /**
     * Wait for specified milliseconds
     */
    public static void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

## 📊 Test Data Management

### 1. Test Data Initialization

Create `src/test/java/com/apitester/config/TestDataInitializer.java`:

```java
package com.apitester.config;

import com.apitester.model.Product;
import com.apitester.model.User;
import com.apitester.repository.ProductRepository;
import com.apitester.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@TestComponent
@RequiredArgsConstructor
@ActiveProfiles("test")
public class TestDataInitializer {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void initializeTestData() {
        // Create test users
        createTestUsers();
        
        // Create test products
        createTestProducts();
    }

    private void createTestUsers() {
        if (userRepository.count() == 0) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setEmail("test@example.com");
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(testUser);
        }
    }

    private void createTestProducts() {
        if (productRepository.count() == 0) {
            Product testProduct = new Product();
            testProduct.setName("Test Product");
            testProduct.setDescription("Test product description");
            testProduct.setPrice(new BigDecimal("99.99"));
            testProduct.setStockQuantity(10);
            testProduct.setCategory(Product.ProductCategory.ELECTRONICS);
            testProduct.setStatus(Product.ProductStatus.AVAILABLE);
            productRepository.save(testProduct);
        }
    }
}
```

### 2. Database Cleanup

Create `src/test/java/com/apitester/config/TestDatabaseCleanup.java`:

```java
package com.apitester.config;

import com.apitester.repository.ProductRepository;
import com.apitester.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
@RequiredArgsConstructor
@ActiveProfiles("test")
public class TestDatabaseCleanup {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void cleanupDatabase() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Transactional
    public void cleanupUsers() {
        userRepository.deleteAll();
    }

    @Transactional
    public void cleanupProducts() {
        productRepository.deleteAll();
    }
}
```

## 🧪 Test Categories

### 1. Unit Tests

Location: `src/test/java/com/apitester/unit/`

```java
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

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

### 2. Integration Tests

Location: `src/test/java/com/apitester/integration/`

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void createUser_ShouldPersistToDatabase() {
        // Integration test implementation
    }
}
```

### 3. API Tests

Location: `src/test/java/com/apitester/api/`

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserApiTest extends RestAssuredConfig {

    @BeforeEach
    void setUp() {
        setBasePath("/api");
    }

    @Test
    void testGetAllUsers() {
        // API test implementation
    }
}
```

### 4. Performance Tests

Location: `src/test/java/com/apitester/performance/`

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserApiPerformanceTest extends RestAssuredConfig {

    @Test
    void testGetAllUsers_Performance() {
        given()
            .spec(requestSpec)
        .when()
            .get("/users")
        .then()
            .spec(responseSpec)
            .time(lessThan(1000L)); // 1 second max
    }
}
```

## 🏃‍♂️ Running Tests

### Environment Setup for Tests
**⚠️ IMPORTANT**: Tests now require environment variables for secure configuration.

```bash
# Set up environment variables for testing
export DB_USERNAME=sa
export DB_PASSWORD=test_password_123
export H2_CONSOLE_ENABLED=false
export LOG_LEVEL=INFO
export SPRING_WEB_LOG_LEVEL=WARN
export HIBERNATE_SQL_LOG_LEVEL=WARN
export HIBERNATE_BINDER_LOG_LEVEL=WARN
export MANAGEMENT_ENDPOINTS=health
export HEALTH_SHOW_DETAILS=when-authorized
```

### 1. Run All Tests

```bash
# Run all tests (with environment variables)
mvn test
```

### 2. Run Specific Test Categories

```bash
# Run only unit tests
mvn test -Dtest="*UnitTest"

# Run only integration tests
mvn test -Dtest="*IntegrationTest"

# Run only API tests
mvn test -Dtest="*ApiTest"

# Run only performance tests
mvn test -Dtest="*PerformanceTest"
```

### 3. Run Specific Test Classes

```bash
mvn test -Dtest=UserApiTest
mvn test -Dtest=UserServiceUnitTest
```

### 4. Run Specific Test Methods

```bash
mvn test -Dtest=UserApiTest#testGetAllUsers
```

### 5. Run Tests with Profiles

```bash
mvn test -Dspring.profiles.active=test
```

## 📈 Test Reports

### 1. Surefire Reports

Maven Surefire generates test reports in `target/surefire-reports/`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
        </includes>
        <excludes>
            <exclude>**/*PerformanceTest.java</exclude>
        </excludes>
    </configuration>
</plugin>
```

### 2. Custom Test Reports

Create custom test reporting with TestNG or JUnit 5:

```java
@ExtendWith(TestReporter.class)
class UserApiTest {
    // Test methods
}
```

## ✅ Best Practices

### 1. Test Naming Conventions

```java
// Good naming
@Test
void createUser_ShouldReturnCreatedUser_WhenValidDataProvided()

@Test
void getUserById_ShouldReturnUser_WhenUserExists()

@Test
void getUserById_ShouldReturn404_WhenUserDoesNotExist()
```

### 2. Test Structure (AAA Pattern)

```java
@Test
void testMethod() {
    // Arrange - Set up test data
    User user = createTestUser();
    
    // Act - Execute the method under test
    User result = userService.createUser(user);
    
    // Assert - Verify the results
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
}
```

### 3. Test Data Isolation

```java
@Test
@Transactional
void testMethod() {
    // Each test runs in its own transaction
    // Data is rolled back after test
}
```

### 4. Test Configuration

```java
@TestConfiguration
public class TestConfig {
    
    @Bean
    public TestDataInitializer testDataInitializer() {
        return new TestDataInitializer();
    }
}
```

### 5. Parallel Test Execution

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
    </configuration>
</plugin>
```

## 🔧 Advanced Configuration

### 1. Test Properties

Create `src/test/resources/application-test.properties`:

```properties
# Test database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver

# Test logging
logging.level.com.apitester=DEBUG
logging.level.org.springframework.web=WARN

# Test timeouts
rest.assured.timeout=5000
rest.assured.connection.timeout=3000
```

### 2. Test Profiles

```java
@ActiveProfiles({"test", "integration"})
class IntegrationTest {
    // Integration test implementation
}
```

### 3. Conditional Test Execution

```java
@Test
@EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
void testMethod() {
    // Only runs on 64-bit systems
}
```

## 🚨 Troubleshooting

### Common Issues

1. **Port Conflicts**
   ```java
   @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
   ```

2. **Database Connection Issues**
   ```yaml
   spring:
     datasource:
       url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
   ```

3. **Test Data Cleanup**
   ```java
   @AfterEach
   void cleanup() {
       testDatabaseCleanup.cleanupDatabase();
   }
   ```

### Debug Configuration

```java
@Test
void debugTest() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    // Test implementation
}
```

---

**Next Steps**:
- Read [API Testing Guide](./api-testing-guide.md) for detailed testing strategies
- Review [RestAssured Tutorial](./restassured-tutorial.md) for framework usage
- Study [Test Data Management](./test-data-management.md) for data handling
