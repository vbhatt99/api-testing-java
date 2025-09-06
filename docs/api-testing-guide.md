# API Testing Guide

This comprehensive guide covers all aspects of API testing using RestAssured, JUnit 5, and Spring Boot. Learn how to write effective, maintainable, and comprehensive API tests.

## 📋 Table of Contents

- [Overview](#overview)
- [Testing Fundamentals](#testing-fundamentals)
- [RestAssured Basics](#restassured-basics)
- [Test Categories](#test-categories)
- [Advanced Testing Techniques](#advanced-testing-techniques)
- [Test Data Management](#test-data-management)
- [Performance Testing](#performance-testing)
- [Best Practices](#best-practices)
- [Common Patterns](#common-patterns)

## 🏗️ Overview

API testing is a critical component of modern software development. This guide teaches you how to:

- Write comprehensive API tests
- Use RestAssured effectively
- Manage test data
- Handle authentication and authorization
- Test performance and security
- Create maintainable test suites

## 🧪 Testing Fundamentals

### What is API Testing?

API testing verifies that your REST APIs work correctly by:

1. **Functional Testing**: Testing API functionality and business logic
2. **Performance Testing**: Testing response times and throughput
3. **Security Testing**: Testing authentication, authorization, and data validation
4. **Integration Testing**: Testing API interactions with other systems

### Testing Pyramid

```
    /\
   /  \     E2E Tests (Few)
  /____\    Integration Tests (Some)
 /______\   Unit Tests (Many)
```

### Test Types

1. **Unit Tests**: Test individual components in isolation
2. **Integration Tests**: Test component interactions
3. **API Tests**: Test REST endpoints end-to-end
4. **Performance Tests**: Test response times and load handling

## 🚀 RestAssured Basics

### Setup and Configuration

```java
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }
}
```

### Basic HTTP Methods

#### GET Request
```java
@Test
void testGetRequest() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users")
    .then()
        .statusCode(200)
        .body("$", hasSize(greaterThan(0)))
        .body("[0].id", notNullValue());
}
```

#### POST Request
```java
@Test
void testPostRequest() {
    Map<String, Object> userData = new HashMap<>();
    userData.put("username", "testuser");
    userData.put("email", "test@example.com");
    userData.put("firstName", "Test");
    userData.put("lastName", "User");

    given()
        .contentType(ContentType.JSON)
        .body(userData)
    .when()
        .post("/users")
    .then()
        .statusCode(201)
        .body("username", equalTo("testuser"))
        .body("id", notNullValue());
}
```

#### PUT Request
```java
@Test
void testPutRequest() {
    Map<String, Object> updateData = new HashMap<>();
    updateData.put("firstName", "Updated");
    updateData.put("lastName", "Name");

    given()
        .contentType(ContentType.JSON)
        .body(updateData)
    .when()
        .put("/users/1")
    .then()
        .statusCode(200)
        .body("firstName", equalTo("Updated"));
}
```

#### DELETE Request
```java
@Test
void testDeleteRequest() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .delete("/users/1")
    .then()
        .statusCode(204);
}
```

### Response Validation

#### Status Code Validation
```java
.then()
    .statusCode(200)                    // Exact status code
    .statusCode(equalTo(200))           // Using matcher
    .statusCode(both(greaterThan(199)).and(lessThan(300)))  // Range
```

#### Body Validation
```java
.then()
    .body("id", equalTo(1))             // Exact value
    .body("name", notNullValue())       // Not null
    .body("price", greaterThan(0.0))    // Numeric comparison
    .body("active", equalTo(true))      // Boolean
    .body("tags", hasSize(3))           // Array size
    .body("tags", hasItem("electronics")) // Array contains
```

#### JSON Path Validation
```java
.then()
    .body("users[0].id", equalTo(1))    // Array element
    .body("users.size()", equalTo(5))   // Array size
    .body("users.findAll { it.active }.size()", equalTo(3)) // Filtered
    .body("users.find { it.id == 1 }.name", equalTo("John")) // Find specific
```

## 🧪 Test Categories

### 1. Functional Tests

#### Happy Path Testing
```java
@Test
void testCreateUser_Success() {
    Map<String, Object> userData = createValidUserData();

    given()
        .contentType(ContentType.JSON)
        .body(userData)
    .when()
        .post("/users")
    .then()
        .statusCode(201)
        .body("username", equalTo(userData.get("username")))
        .body("email", equalTo(userData.get("email")))
        .body("id", notNullValue())
        .body("createdAt", notNullValue());
}
```

#### Error Path Testing
```java
@Test
void testCreateUser_ValidationError() {
    Map<String, Object> invalidData = createInvalidUserData();

    given()
        .contentType(ContentType.JSON)
        .body(invalidData)
    .when()
        .post("/users")
    .then()
        .statusCode(400)
        .body("errors", hasSize(greaterThan(0)))
        .body("errors[0].field", equalTo("email"))
        .body("errors[0].message", containsString("valid"));
}
```

#### Business Logic Testing
```java
@Test
void testCreateUser_DuplicateUsername() {
    // First, create a user
    createUser("testuser", "test@example.com");
    
    // Try to create another user with same username
    Map<String, Object> duplicateData = createUserData("testuser", "another@example.com");

    given()
        .contentType(ContentType.JSON)
        .body(duplicateData)
    .when()
        .post("/users")
    .then()
        .statusCode(409)
        .body("message", containsString("already exists"));
}
```

### 2. Data Validation Tests

#### Required Field Validation
```java
@Test
void testCreateUser_MissingRequiredFields() {
    Map<String, Object> incompleteData = new HashMap<>();
    incompleteData.put("username", "testuser");
    // Missing email, firstName, lastName

    given()
        .contentType(ContentType.JSON)
        .body(incompleteData)
    .when()
        .post("/users")
    .then()
        .statusCode(400)
        .body("errors", hasSize(3))
        .body("errors.field", hasItems("email", "firstName", "lastName"));
}
```

#### Data Type Validation
```java
@Test
void testCreateProduct_InvalidPrice() {
    Map<String, Object> invalidData = new HashMap<>();
    invalidData.put("name", "Test Product");
    invalidData.put("price", "invalid-price"); // Should be number
    invalidData.put("stockQuantity", 10);

    given()
        .contentType(ContentType.JSON)
        .body(invalidData)
    .when()
        .post("/products")
    .then()
        .statusCode(400);
}
```

#### Boundary Value Testing
```java
@Test
void testCreateUser_UsernameTooShort() {
    Map<String, Object> userData = createUserData();
    userData.put("username", "ab"); // Less than minimum 3 characters

    given()
        .contentType(ContentType.JSON)
        .body(userData)
    .when()
        .post("/users")
    .then()
        .statusCode(400)
        .body("errors[0].message", containsString("3"));
}
```

### 3. Security Tests

#### Authentication Testing
```java
@Test
void testProtectedEndpoint_WithoutAuth() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/admin/users")
    .then()
        .statusCode(401);
}

@Test
void testProtectedEndpoint_WithAuth() {
    String token = getAuthToken();

    given()
        .contentType(ContentType.JSON)
        .header("Authorization", "Bearer " + token)
    .when()
        .get("/admin/users")
    .then()
        .statusCode(200);
}
```

#### Authorization Testing
```java
@Test
void testAdminEndpoint_RegularUser() {
    String userToken = getUserToken();

    given()
        .contentType(ContentType.JSON)
        .header("Authorization", "Bearer " + userToken)
    .when()
        .delete("/admin/users/1")
    .then()
        .statusCode(403);
}
```

### 4. Performance Tests

#### Response Time Testing
```java
@Test
void testGetUsers_ResponseTime() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users")
    .then()
        .statusCode(200)
        .time(lessThan(1000L)); // Less than 1 second
}
```

#### Load Testing
```java
@Test
void testGetUsers_LoadTest() {
    int numberOfRequests = 100;
    
    for (int i = 0; i < numberOfRequests; i++) {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .time(lessThan(2000L));
    }
}
```

## 🔧 Advanced Testing Techniques

### 1. Request/Response Specifications

```java
public class ApiSpecifications {
    
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .addHeader("User-Agent", "API-Test-Suite")
            .build();
    }
    
    public static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .expectResponseTime(lessThan(3000L))
            .build();
    }
}

// Usage
@Test
void testWithSpecifications() {
    given()
        .spec(getRequestSpec())
    .when()
        .get("/users")
    .then()
        .spec(getResponseSpec())
        .statusCode(200);
}
```

### 2. JSON Schema Validation

```java
@Test
void testUserResponse_SchemaValidation() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users/1")
    .then()
        .statusCode(200)
        .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
}
```

### 3. Response Extraction

```java
@Test
void testCreateAndUpdateUser() {
    // Create user and extract ID
    Response createResponse = given()
        .contentType(ContentType.JSON)
        .body(createUserData())
    .when()
        .post("/users")
    .then()
        .statusCode(201)
        .extract().response();
    
    Long userId = createResponse.jsonPath().getLong("id");
    
    // Use extracted ID for update
    Map<String, Object> updateData = new HashMap<>();
    updateData.put("firstName", "Updated");
    
    given()
        .contentType(ContentType.JSON)
        .body(updateData)
    .when()
        .put("/users/" + userId)
    .then()
        .statusCode(200)
        .body("firstName", equalTo("Updated"));
}
```

### 4. File Upload Testing

```java
@Test
void testFileUpload() {
    File file = new File("src/test/resources/test-file.txt");
    
    given()
        .contentType("multipart/form-data")
        .multiPart("file", file)
    .when()
        .post("/upload")
    .then()
        .statusCode(200)
        .body("filename", equalTo("test-file.txt"));
}
```

## 📊 Test Data Management

### 1. Test Data Builders

```java
public class UserTestDataBuilder {
    
    public static Map<String, Object> createValidUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "testuser_" + System.currentTimeMillis());
        user.put("email", "test" + System.currentTimeMillis() + "@example.com");
        user.put("firstName", "Test");
        user.put("lastName", "User");
        user.put("password", "[hashed password - use passwordEncoder.encode()]");
        return user;
    }
    
    public static Map<String, Object> createInvalidUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "");
        user.put("email", "invalid-email");
        user.put("firstName", "");
        return user;
    }
}
```

### 2. Test Data Factories

```java
public class TestDataFactory {
    
    private static final Faker faker = new Faker();
    
    public static User createRandomUser() {
        User user = new User();
        user.setUsername(faker.name().username());
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setPassword(faker.internet().password());
        return user;
    }
    
    public static List<User> createMultipleUsers(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> createRandomUser())
            .collect(Collectors.toList());
    }
}
```

### 3. Database Setup/Teardown

```java
@SpringBootTest
@Transactional
class UserApiTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        // Create test data
    }
    
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
```

## ⚡ Performance Testing

### 1. Response Time Testing

```java
@Test
void testResponseTime() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users")
    .then()
        .statusCode(200)
        .time(lessThan(1000L)) // 1 second
        .time(greaterThan(100L)); // At least 100ms (realistic)
}
```

### 2. Concurrent Testing

```java
@Test
void testConcurrentRequests() {
    int numberOfThreads = 10;
    int requestsPerThread = 10;
    
    ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads * requestsPerThread);
    
    for (int i = 0; i < numberOfThreads; i++) {
        executor.submit(() -> {
            for (int j = 0; j < requestsPerThread; j++) {
                given()
                    .contentType(ContentType.JSON)
                .when()
                    .get("/users")
                .then()
                    .statusCode(200);
                latch.countDown();
            }
        });
    }
    
    latch.await(30, TimeUnit.SECONDS);
    executor.shutdown();
}
```

### 3. Load Testing with JMeter

```java
// Integration with JMeter for load testing
@Test
void testLoadWithJMeter() {
    // Configure JMeter test plan
    // Run load test
    // Validate results
}
```

## ✅ Best Practices

### 1. Test Naming Conventions

```java
// Good naming examples
@Test
void createUser_ShouldReturn201_WhenValidDataProvided()

@Test
void getUserById_ShouldReturnUser_WhenUserExists()

@Test
void getUserById_ShouldReturn404_WhenUserDoesNotExist()

@Test
void updateUser_ShouldReturn200_WhenValidUpdateDataProvided()
```

### 2. Test Organization

```java
@Nested
class CreateUserTests {
    
    @Test
    void shouldCreateUser_WhenValidDataProvided() { }
    
    @Test
    void shouldReturn400_WhenInvalidDataProvided() { }
    
    @Test
    void shouldReturn409_WhenUsernameAlreadyExists() { }
}

@Nested
class GetUserTests {
    
    @Test
    void shouldReturnUser_WhenUserExists() { }
    
    @Test
    void shouldReturn404_WhenUserDoesNotExist() { }
}
```

### 3. Assertion Best Practices

```java
@Test
void testUserResponse() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users/1")
    .then()
        .statusCode(200)
        .body("id", equalTo(1))
        .body("username", notNullValue())
        .body("email", matchesPattern("^[^@]+@[^@]+\\.[^@]+$"))
        .body("createdAt", notNullValue())
        .body("updatedAt", notNullValue());
}
```

### 4. Error Handling

```java
@Test
void testErrorResponse() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users/99999")
    .then()
        .statusCode(404)
        .body("timestamp", notNullValue())
        .body("status", equalTo(404))
        .body("error", equalTo("Not Found"))
        .body("message", containsString("not found"));
}
```

## 🔄 Common Patterns

### 1. CRUD Operations Testing

```java
@Test
void testFullCrudOperations() {
    // Create
    Response createResponse = given()
        .contentType(ContentType.JSON)
        .body(createUserData())
    .when()
        .post("/users")
    .then()
        .statusCode(201)
        .extract().response();
    
    Long userId = createResponse.jsonPath().getLong("id");
    
    // Read
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users/" + userId)
    .then()
        .statusCode(200)
        .body("id", equalTo(userId.intValue()));
    
    // Update
    Map<String, Object> updateData = new HashMap<>();
    updateData.put("firstName", "Updated");
    
    given()
        .contentType(ContentType.JSON)
        .body(updateData)
    .when()
        .put("/users/" + userId)
    .then()
        .statusCode(200)
        .body("firstName", equalTo("Updated"));
    
    // Delete
    given()
        .contentType(ContentType.JSON)
    .when()
        .delete("/users/" + userId)
    .then()
        .statusCode(204);
    
    // Verify deletion
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users/" + userId)
    .then()
        .statusCode(404);
}
```

### 2. Pagination Testing

```java
@Test
void testPagination() {
    given()
        .contentType(ContentType.JSON)
        .queryParam("page", 0)
        .queryParam("size", 5)
    .when()
        .get("/users")
    .then()
        .statusCode(200)
        .body("content", hasSize(lessThanOrEqualTo(5)))
        .body("totalElements", greaterThan(0))
        .body("totalPages", greaterThan(0))
        .body("first", equalTo(true))
        .body("last", notNullValue());
}
```

### 3. Search and Filter Testing

```java
@Test
void testSearchAndFilter() {
    given()
        .contentType(ContentType.JSON)
        .queryParam("firstName", "John")
        .queryParam("status", "ACTIVE")
        .queryParam("sort", "createdAt,desc")
    .when()
        .get("/users/search")
    .then()
        .statusCode(200)
        .body("content", hasSize(greaterThan(0)))
        .body("content.firstName", everyItem(containsString("John")));
}
```

---

**Next Steps**:
- Read [RestAssured Tutorial](./restassured-tutorial.md) for detailed framework usage
- Study [Test Data Management](./test-data-management.md) for data handling strategies
- Review [Performance Testing](./modules/module-4-performance-testing.md) for load testing
