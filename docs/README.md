# API Testing Java Project - Documentation

Welcome to the comprehensive API testing project built with Java, Spring Boot, and RestAssured. This project is designed to help you learn and practice API testing from basic concepts to advanced scenarios.

## 📚 Documentation Index

### 🚀 Getting Started
- [Project Overview](./project-overview.md) - Complete project description and architecture
- [Quick Start Guide](./quick-start.md) - Get up and running in 5 minutes
- [Installation Guide](./installation.md) - Detailed setup instructions

### 🏗️ Architecture & Design
- [Architecture Overview](./architecture.md) - System design and component relationships
- [API Design Patterns](./api-design-patterns.md) - REST API best practices implemented
- [Database Schema](./database-schema.md) - Entity relationships and data model

### 🔧 Development
- [Services Explained](./services-explained.md) - Detailed explanation of all service layers
- [Controllers Guide](./controllers-guide.md) - REST controller implementation details
- [Repository Layer](./repository-layer.md) - Data access patterns and custom queries
- [Configuration Guide](./configuration-guide.md) - Application configuration details

### 🧪 Testing
- [Testing Overview](./testing-overview.md) - Testing strategy and approach
- [API Testing Guide](./api-testing-guide.md) - How to write effective API tests
- [Test Configuration](./testing-config.md) - Test setup and configuration
- [RestAssured Tutorial](./restassured-tutorial.md) - RestAssured framework usage
- [Test Data Management](./test-data-management.md) - Managing test data and fixtures
- [Test Reporting System](./test-reporting.md) - Beautiful HTML reports and dashboards

### 📊 API Reference
- [User API Reference](./api/user-api-reference.md) - Complete User API documentation
- [Product API Reference](./api/product-api-reference.md) - Complete Product API documentation
- [Health Check API](./api/health-api-reference.md) - Health monitoring endpoints

### 🛠️ Tools & Utilities
- [Swagger Documentation](./swagger-docs.md) - API documentation with Swagger UI
- [Database Console](./database-console.md) - H2 database management
- [Logging Configuration](./logging-config.md) - Application logging setup

### 🔒 Security
- [Security Configuration](../SECURITY.md) - Comprehensive security documentation
- [Environment Variables](../env.example) - Secure configuration template

### 🚀 Deployment & Operations
- [Local Development](./local-development.md) - Development environment setup
- [Production Deployment](./production-deployment.md) - Production deployment guide
- [Monitoring & Health Checks](./monitoring.md) - Application monitoring

### 📖 Learning Modules
- [Module 1: Basic API Testing](./modules/module-1-basic-testing.md) - Getting started with API testing
- [Module 2: Advanced Testing](./modules/module-2-advanced-testing.md) - Advanced testing scenarios
- [Module 3: Test Automation](./modules/module-3-test-automation.md) - Automated testing strategies
- [Module 4: Performance Testing](./modules/module-4-performance-testing.md) - Performance testing basics

### 🔍 Troubleshooting
- [Common Issues](./troubleshooting/common-issues.md) - Frequently encountered problems
- [Debug Guide](./troubleshooting/debug-guide.md) - Debugging techniques
- [Performance Tuning](./troubleshooting/performance-tuning.md) - Performance optimization

## 🎯 Quick Navigation

### For Beginners
1. Start with [Project Overview](./project-overview.md)
2. Follow the [Quick Start Guide](./quick-start.md)
3. Read [Module 1: Basic API Testing](./modules/module-1-basic-testing.md)

### For Developers
1. Review [Architecture Overview](./architecture.md)
2. Understand [Services Explained](./services-explained.md)
3. Study [API Testing Guide](./api-testing-guide.md)

### For Testers
1. Begin with [Testing Overview](./testing-overview.md)
2. Learn [RestAssured Tutorial](./restassured-tutorial.md)
3. Practice with [API Testing Guide](./api-testing-guide.md)

## 📋 Project Structure

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
├── docs/                       # Documentation
├── pom.xml                     # Maven configuration
└── README.md                   # Project README
```

## 🚀 Quick Start

1. **Clone and Setup**
   ```bash
   git clone <repository-url>
   cd api-testing-java
   mvn clean install
   ```

2. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the APIs**
   - API Documentation: http://localhost:8080/api/swagger-ui.html
   - Health Check: http://localhost:8080/api/health
   - H2 Database: http://localhost:8080/api/h2-console (disabled by default)

4. **Run Tests**
   ```bash
   mvn test
   ```

## 📞 Support

If you need help or have questions:
- Check the [Troubleshooting Guide](./troubleshooting/common-issues.md)
- Review the [Debug Guide](./troubleshooting/debug-guide.md)
- Create an issue in the project repository

## 🤝 Contributing

We welcome contributions! Please see our contributing guidelines and code of conduct.

---

**Happy Testing! 🧪✨**
