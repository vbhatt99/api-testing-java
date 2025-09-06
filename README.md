# 🧪 API Testing Java Project

A comprehensive learning platform for API testing using Java, Spring Boot, and RestAssured. This project provides real-world examples, extensive documentation, and step-by-step learning modules to master API testing from basics to advanced concepts.

## 🚀 Quick Start

Get up and running in **5 minutes**:

```bash
# Clone and setup
git clone <repository-url>
cd api-testing-java
mvn clean install

# Run the application
mvn spring-boot:run

# Access the APIs
# API Documentation: http://localhost:8080/api/swagger-ui.html
# Health Check: http://localhost:8080/api/health
# H2 Database: http://localhost:8080/api/h2-console

# Run tests
mvn test
```

## 📚 What You'll Learn

### 🎯 Core Skills
- **REST API Testing** with RestAssured
- **Spring Boot Development** and testing
- **Database Integration** with JPA/Hibernate
- **Test Automation** and CI/CD practices
- **Performance Testing** and monitoring

### 🏗️ Architecture
- **Layered Architecture**: Controllers → Services → Repositories
- **RESTful APIs**: Standard HTTP methods and status codes
- **Data Validation**: Input validation and error handling
- **Testing Pyramid**: Unit → Integration → API tests

## 📊 Project Features

### ✅ Working APIs
- **User Management**: Full CRUD operations with search
- **Product Catalog**: Product management with categories
- **Health Monitoring**: Application health checks
- **Advanced Search**: Filtering and pagination

### ✅ Comprehensive Testing
- **50+ Test Scenarios**: Covering all API endpoints
- **RestAssured Framework**: Modern API testing
- **Test Data Management**: Automated data setup/cleanup
- **Performance Testing**: Response time validation

### ✅ Production Ready
- **Swagger Documentation**: Interactive API docs
- **Database Console**: H2 web interface
- **Logging**: Structured logging with SLF4J
- **Configuration**: Environment-based settings

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Programming language |
| **Spring Boot** | 3.2 | Application framework |
| **Spring Data JPA** | 3.2 | Data persistence |
| **H2 Database** | 2.2 | In-memory database |
| **RestAssured** | 5.3 | API testing framework |
| **JUnit 5** | 5.9 | Test execution |
| **Maven** | 3.6+ | Build tool |
| **Swagger/OpenAPI** | 2.2 | API documentation |

## 📁 Project Structure

```
api-testing-java/
├── src/
│   ├── main/java/com/apitester/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/            # Business Logic
│   │   ├── repository/         # Data Access
│   │   ├── model/              # Entity Models
│   │   └── config/             # Configuration
│   ├── main/resources/         # Configuration files
│   └── test/java/com/apitester/
│       └── api/                # API Tests
├── docs/                       # 📚 Comprehensive Documentation
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

## 🔌 Available APIs

### User Management
```bash
# Get all users
GET /api/users

# Create user
POST /api/users
{
  "username": "testuser",
  "email": "test@example.com",
  "firstName": "Test",
  "lastName": "User"
}

# Get user by ID
GET /api/users/{id}

# Update user
PUT /api/users/{id}

# Delete user
DELETE /api/users/{id}

# Search users
GET /api/users/search/firstname?firstName=John
GET /api/users/active
```

### Product Catalog
```bash
# Get all products
GET /api/products

# Create product
POST /api/products
{
  "name": "Test Product",
  "price": 99.99,
  "stockQuantity": 10,
  "category": "ELECTRONICS"
}

# Get by category
GET /api/products/category/ELECTRONICS

# Price range search
GET /api/products/price/range?minPrice=10&maxPrice=100

# Update stock
PATCH /api/products/{id}/stock?stockQuantity=50
```

### Health Checks
```bash
# Basic health check
GET /api/health

# Detailed health info
GET /api/health/detailed

# Readiness check
GET /api/health/ready
```

## 🧪 Testing Examples

### Basic API Test
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

### Error Handling
```java
@Test
void testCreateUser_ValidationError() {
    Map<String, Object> invalidData = new HashMap<>();
    invalidData.put("username", ""); // Invalid: empty username

    given()
        .contentType(ContentType.JSON)
        .body(invalidData)
    .when()
        .post("/users")
    .then()
        .statusCode(400)
        .body("errors", hasSize(greaterThan(0)));
}
```

## 📖 Learning Path

### 🎯 For Beginners
1. **Start Here**: [Quick Start Guide](docs/quick-start.md)
2. **Understand Architecture**: [Project Overview](docs/project-overview.md)
3. **Learn Testing**: [API Testing Guide](docs/api-testing-guide.md)
4. **Practice**: Run tests and modify scenarios

### 🔧 For Developers
1. **Study Services**: [Services Explained](docs/services-explained.md)
2. **Configuration**: [Test Configuration](docs/testing-config.md)
3. **Add Features**: Create new endpoints
4. **Write Tests**: Add comprehensive test coverage

### 🧪 For Testers
1. **RestAssured Tutorial**: Learn the testing framework
2. **Test Data Management**: Handle test data effectively
3. **Performance Testing**: Load and stress testing
4. **Test Automation**: CI/CD integration

## 📚 Documentation

### 📖 Complete Documentation
- **[Main Documentation](docs/README.md)** - Index to all docs
- **[Quick Start](docs/quick-start.md)** - Get running in 5 minutes
- **[Project Overview](docs/project-overview.md)** - Architecture and design
- **[API Testing Guide](docs/api-testing-guide.md)** - Comprehensive testing tutorial
- **[Services Explained](docs/services-explained.md)** - Business logic layer
- **[Test Configuration](docs/testing-config.md)** - Testing setup and configuration

### 🎓 Learning Modules
- **[Module 1: Basic Testing](docs/modules/module-1-basic-testing.md)** - Getting started
- **[Module 2: Advanced Testing](docs/modules/module-2-advanced-testing.md)** - Complex scenarios
- **[Module 3: Test Automation](docs/modules/module-3-test-automation.md)** - Automation strategies
- **[Module 4: Performance Testing](docs/modules/module-4-performance-testing.md)** - Load testing

## 🚀 Getting Started

### Prerequisites
- ✅ **Java 17+** installed
- ✅ **Maven 3.6+** installed
- ✅ **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### Step-by-Step Setup

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

4. **Verify the setup**
   - Open: http://localhost:8080/api/health
   - Should return: `{"status":"UP","application":"API Testing Java"}`

5. **Explore the APIs**
   - API Documentation: http://localhost:8080/api/swagger-ui.html
   - Database Console: http://localhost:8080/api/h2-console

6. **Run tests**
   ```bash
   mvn test
   ```

## 📊 Sample Data

The application comes with pre-loaded sample data:

### 👥 Users
- **John Doe** (john_doe@example.com) - Active
- **Jane Smith** (jane.smith@example.com) - Active
- **Bob Wilson** (bob.wilson@example.com) - Inactive
- **Alice Brown** (alice.brown@example.com) - Active
- **Charlie Davis** (charlie.davis@example.com) - Suspended

### 📦 Products
- **Electronics**: MacBook Pro ($2499.99), iPhone 15 Pro ($999.99)
- **Clothing**: Cotton T-Shirt ($24.99), Slim Fit Jeans ($79.99)
- **Books**: Clean Code ($44.99), Design Patterns ($54.99)
- **Home & Sports**: Coffee Maker ($89.99), Yoga Mat ($34.99)

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
# Access H2 console (development only)
# Set H2_CONSOLE_ENABLED=true in environment variables
# Open: http://localhost:8080/api/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Username: sa (or DB_USERNAME value)
# Password: [your configured DB_PASSWORD]
```

## 🎯 What You'll Build

### Week 1: Foundation
- ✅ Run the application
- ✅ Explore API documentation
- ✅ Understand project structure
- ✅ Write basic API tests

### Week 2: Development
- 🔄 Study service layer architecture
- 🔄 Add new API endpoints
- 🔄 Implement business logic
- 🔄 Create custom queries

### Week 3: Testing
- 🔄 Write comprehensive test suites
- 🔄 Learn RestAssured advanced features
- 🔄 Implement test data management
- 🔄 Add performance tests

### Week 4: Advanced
- 🔄 Security testing
- 🔄 Load testing
- 🔄 CI/CD integration
- 🔄 Production deployment

## 🤝 Contributing

We welcome contributions! Here's how to get started:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes** and add tests
4. **Commit your changes**: `git commit -m 'Add amazing feature'`
5. **Push to the branch**: `git push origin feature/amazing-feature`
6. **Open a Pull Request**

### Development Guidelines
- Follow Java coding standards
- Add tests for new functionality
- Update documentation for changes
- Ensure all tests pass

## 🐛 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   lsof -ti:8080 | xargs kill -9
   ```

2. **Java version issues**
   ```bash
   java -version  # Should be 17+
   ```

3. **Maven build issues**
   ```bash
   mvn clean install -U
   ```

4. **Database connection issues**
   - Check H2 console accessibility
   - Verify JDBC URL and credentials

### Getting Help
- 📚 **Documentation**: Check the [docs](docs/) folder
- 🐛 **Issues**: Create an issue for bugs
- 💬 **Discussions**: Use GitHub discussions for questions

## 📈 Performance

### Response Times
- **Simple GET requests**: < 100ms
- **Complex queries**: < 500ms
- **Create/Update operations**: < 200ms

### Scalability
- **Concurrent users**: 100+ simultaneous requests
- **Database connections**: Configurable pool
- **Memory usage**: Optimized for development

## 🔒 Security

### Implemented Security Features
- ✅ **Password Hashing**: BCrypt password encryption
- ✅ **Input Validation**: Bean Validation on all inputs
- ✅ **Security Headers**: Comprehensive security headers
- ✅ **Environment Variables**: Secure configuration management
- ✅ **SQL Injection Prevention**: JPA/Hibernate protection
- ✅ **XSS Prevention**: Content-Type and XSS protection headers
- ✅ **H2 Console Control**: Environment-based console access
- ✅ **Logging Security**: Configurable log levels

### Security Configuration
- 🔧 **Environment Variables**: See `env.example` for configuration
- 🔧 **Security Documentation**: See `SECURITY.md` for detailed security info
- 🔧 **Password Protection**: Passwords excluded from API responses

### Ready for Integration
- 🔄 JWT/OAuth authentication
- 🔄 Role-based authorization
- 🔄 API rate limiting
- 🔄 CORS configuration

## 📞 Support

### Learning Resources
- 📖 **[Complete Documentation](docs/README.md)** - All guides and tutorials
- 🎓 **[API Testing Guide](docs/api-testing-guide.md)** - Comprehensive testing tutorial
- 🛠️ **[Services Explained](docs/services-explained.md)** - Business logic deep dive
- ⚙️ **[Test Configuration](docs/testing-config.md)** - Testing setup guide

### Community
- 🌟 **Star the repository** if you find it helpful
- 🔄 **Fork and contribute** to improve the project
- 💬 **Share your experience** in discussions
- 🐛 **Report issues** for bugs or improvements

## ⚠️ Important Disclaimers

### User Responsibility
**By using, forking, or modifying this project, you acknowledge and agree that:**

- **You are solely responsible** for the security, configuration, and operation of your instance
- **The original creator is not liable** for any security breaches, data loss, or damages
- **You must implement proper security measures** before using in any production environment
- **You are responsible for** keeping dependencies updated and following security best practices
- **This is educational software** - use at your own risk

### Security Warning
- This project contains **sample data and default configurations** for learning purposes
- **DO NOT use default passwords or configurations** in production
- **Always change default credentials** and implement proper authentication
- **Review all security settings** before deployment
- **This project is not production-ready** without additional security hardening

### No Warranty
This software is provided "as is" without warranty of any kind. The original creator disclaims all warranties, express or implied, including but not limited to the implied warranties of merchantability and fitness for a particular purpose.

## 📄 License & Legal

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

**Important**: Please read the [DISCLAIMER.md](DISCLAIMER.md) file for important legal disclaimers and user responsibility information.

## 🙏 Acknowledgments

- **Spring Boot Team** for the amazing framework
- **RestAssured Community** for the testing framework
- **H2 Database** for the in-memory database
- **All Contributors** who help improve this project

---

## 🎉 Ready to Start?

**Begin your API testing journey today!**

1. **Quick Start**: Follow the [5-minute setup guide](docs/quick-start.md)
2. **Learn**: Study the [comprehensive documentation](docs/README.md)
3. **Practice**: Run tests and experiment with the APIs
4. **Build**: Add new features and write tests
5. **Share**: Contribute back to the community

**Happy Testing! 🧪✨**

---

<div align="center">

**Made with ❤️ for the testing community**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![RestAssured](https://img.shields.io/badge/RestAssured-5.3-blue.svg)](https://rest-assured.io/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)

</div>
