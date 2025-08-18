# Quick Start Guide

Get up and running with the API Testing Java project in 5 minutes! This guide will help you set up the project, run the application, and start testing APIs immediately.

## ⚡ 5-Minute Setup

### Prerequisites Check

Before starting, ensure you have:

- ✅ **Java 17+** installed
- ✅ **Maven 3.6+** installed
- ✅ **Git** installed
- ✅ **IDE** (IntelliJ IDEA, Eclipse, VS Code OR CURSOR)

### Step 1: Clone and Setup (1 minute)

```bash
# Clone the repository
git clone <repository-url>
cd api-testing-java

# Build the project
mvn clean install
```

### Step 2: Run the Application (1 minute)

```bash
# Start the Spring Boot application
mvn spring-boot:run
```

You should see output like:
```
🚀 API Testing Application started successfully!
📖 API Documentation: http://localhost:8080/api/swagger-ui.html
🔍 H2 Database Console: http://localhost:8080/api/h2-console
💡 Health Check: http://localhost:8080/api/health
```

### Step 3: Verify the Setup (1 minute)

Open your browser and visit:

1. **Health Check**: http://localhost:8080/api/health
   - Should return: `{"status":"UP","timestamp":"...","application":"API Testing Java","version":"1.0.0"}`

2. **API Documentation**: http://localhost:8080/api/swagger-ui.html
   - Interactive API documentation with all endpoints

3. **Database Console**: http://localhost:8080/api/h2-console
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: `password`

### Step 4: Test Your First API (1 minute)

Using curl or Postman:

```bash
# Get all users
curl http://localhost:8080/api/users

# Get all products
curl http://localhost:8080/api/products

# Health check
curl http://localhost:8080/api/health
```

### Step 5: Run Tests (1 minute)

```bash
# Run all tests
mvn test

# Run only API tests
mvn test -Dtest="*ApiTest"

# Run with detailed output
mvn test -Dspring.profiles.active=test
```

## 🎯 What You Get

After completing the setup, you'll have:

### ✅ Working APIs
- **User Management**: CRUD operations for users
- **Product Catalog**: Product management with categories
- **Health Monitoring**: Application health checks
- **Search & Filtering**: Advanced search capabilities

### ✅ Sample Data
- **5 Users**: Different statuses (active, inactive, suspended)
- **10 Products**: Various categories (electronics, clothing, books, etc.)
- **Realistic Data**: Names, prices, descriptions

### ✅ Testing Framework
- **RestAssured**: API testing framework
- **JUnit 5**: Test execution
- **H2 Database**: In-memory database for testing
- **Comprehensive Tests**: 50+ test scenarios

## 🚀 Next Steps

### For Beginners
1. **Explore the APIs**: Use Swagger UI to test endpoints
2. **Read the Code**: Start with `UserController` and `ProductController`
3. **Run Tests**: Execute different test categories
4. **Modify Data**: Use H2 console to view/edit data

### For Developers
1. **Study Architecture**: Review service and repository layers
2. **Add Features**: Create new endpoints or modify existing ones
3. **Write Tests**: Add new test scenarios
4. **Customize**: Modify configuration and add new entities

### For Testers
1. **Learn RestAssured**: Study the test examples
2. **Create Test Data**: Add new test scenarios
3. **Performance Testing**: Add load tests
4. **API Automation**: Build test suites

## 🔧 Common Commands

### Development
```bash
# Start application
mvn spring-boot:run

# Build project
mvn clean install

# Run tests
mvn test

# Package application
mvn package
```

### Testing
```bash
# Run specific test class
mvn test -Dtest=UserApiTest

# Run specific test method
mvn test -Dtest=UserApiTest#testGetAllUsers

# Run with test profile
mvn test -Dspring.profiles.active=test

# Run with debug logging
mvn test -Dlogging.level.com.apitester=DEBUG
```

### Database
```bash
# Access H2 console
# Open: http://localhost:8080/api/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Username: sa
# Password: password
```

## 📊 Available Endpoints

### User APIs
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/active` - Get active users
- `GET /api/users/search/firstname?firstName=John` - Search by first name

### Product APIs
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `GET /api/products/category/ELECTRONICS` - Get by category
- `GET /api/products/price/range?minPrice=10&maxPrice=100` - Price range search

### Health APIs
- `GET /api/health` - Basic health check
- `GET /api/health/detailed` - Detailed health information
- `GET /api/health/ready` - Readiness check
- `GET /api/health/live` - Liveness check

## 🧪 Sample Test Scenarios

### Basic API Testing
```java
@Test
void testGetAllUsers() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .get("/users")
    .then()
        .statusCode(200)
        .body("$", hasSize(greaterThan(0)));
}
```

### Create and Verify
```java
@Test
void testCreateUser() {
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
        .body("username", equalTo("testuser"));
}
```

## 🔍 Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Kill process on port 8080
   lsof -ti:8080 | xargs kill -9
   ```

2. **Java Version Issues**
   ```bash
   # Check Java version
   java -version
   # Should be 17 or higher
   ```

3. **Maven Issues**
   ```bash
   # Clean and rebuild
   mvn clean install -U
   ```

4. **Database Connection Issues**
   - Ensure H2 console is accessible
   - Check JDBC URL and credentials
   - Verify database is initialized

### Getting Help

- **Check Logs**: Look at console output for error messages
- **Verify URLs**: Ensure all URLs are correct
- **Test Health**: Start with health check endpoint
- **Review Configuration**: Check `application.yml` settings

## 📚 Learning Path

### Week 1: Basics
- [ ] Run the application
- [ ] Explore Swagger UI
- [ ] Test basic endpoints
- [ ] Understand project structure

### Week 2: Development
- [ ] Study service layer
- [ ] Add new endpoints
- [ ] Modify existing functionality
- [ ] Learn about transactions

### Week 3: Testing
- [ ] Write unit tests
- [ ] Create API tests
- [ ] Learn RestAssured
- [ ] Add test data

### Week 4: Advanced
- [ ] Performance testing
- [ ] Security testing
- [ ] CI/CD integration
- [ ] Production deployment

## 🎉 Congratulations!

You've successfully set up the API Testing Java project! You now have:

- ✅ A working Spring Boot application
- ✅ Comprehensive API endpoints
- ✅ Sample data for testing
- ✅ Testing framework ready to use
- ✅ Documentation and examples

**Ready to start testing?** 🚀

---

**Next Steps**:
- Read [Project Overview](./project-overview.md) for detailed architecture
- Study [API Testing Guide](./api-testing-guide.md) for testing strategies
- Explore [Services Explained](./services-explained.md) for business logic
