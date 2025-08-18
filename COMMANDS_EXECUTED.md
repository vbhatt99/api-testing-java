# Commands Executed During API Testing Project Setup

This document tracks all the commands executed during the setup and configuration of the API testing project, along with explanations of their purpose.

## Project Initialization and Setup

### 1. Maven Build and Compilation
```bash
mvn clean install
```
**Purpose:** Initial build to check if the project compiles correctly and identify any issues with dependencies or code.

### 2. Lombok Configuration Fix
```bash
# Modified pom.xml to include Lombok annotation processor
# Added to maven-compiler-plugin configuration:
<annotationProcessorPaths>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version>
    </path>
</annotationProcessorPaths>
```
**Purpose:** Fixed compilation errors where Lombok annotations (@Slf4j, @Data) were not being processed, causing "cannot find symbol" errors for log and setter methods.

### 3. HQL Query Fix
```bash
# Modified ProductRepository.java and ProductService.java
# Changed from: @Query("SELECT p FROM Product p WHERE p.createdAt >= CURRENT_DATE - 30")
# To: @Query("SELECT p FROM Product p WHERE p.createdAt >= :startDate")
```
**Purpose:** Fixed Hibernate query error where temporal operations were incompatible with Hibernate's query parser.

### 4. Validation Constraint Fix
```bash
# Modified Product.java
# Changed from: @Positive
# To: @jakarta.validation.constraints.Min(value = 0)
```
**Purpose:** Fixed validation error where @Positive annotation was too restrictive and didn't allow zero stock quantities.

### 5. Controller Exception Handling
```bash
# Modified UserController.java to add try-catch blocks
# Added proper HTTP status codes (409 for conflicts, 404 for not found)
```
**Purpose:** Fixed API test failures where duplicate username/email was returning 500 instead of 409 Conflict status.

## Test Configuration and Execution

### 6. Test Execution with Allure Results
```bash
mvn clean test -Dallure.results.directory=target/allure-results
```
**Purpose:** Run all tests while collecting Allure test results for advanced reporting. This generates the necessary JSON files that Allure needs to create reports.

### 7. Allure Report Generation
```bash
mvn allure:report
```
**Purpose:** Generate the Allure HTML report from the collected test results. This creates the beautiful interactive dashboard.

### 8. Allure Report Directory Check
```bash
ls -la target/allure-results/
```
**Purpose:** Verify that Allure test results were properly generated and contain the necessary JSON files for report creation.

### 9. Allure Report File Verification
```bash
ls -la target/allure-report/
```
**Purpose:** Confirm that the Allure HTML report files were created successfully.

## Report Viewing Setup

### 10. HTTP Server for Allure Report
```bash
cd target/allure-report && python3 -m http.server 8080
```
**Purpose:** Start a local HTTP server to serve the Allure report. This is necessary because Allure reports cannot be opened directly as files due to browser security restrictions (CORS policies).

### 11. Open Allure Report in Browser
```bash
open http://localhost:8080
```
**Purpose:** Open the Allure report in the default browser using the HTTP server instead of file:// protocol.

### 12. Alternative Report Opening
```bash
open target/test-reports/index.html
```
**Purpose:** Open the main test dashboard report (this works with file:// protocol since it's a simpler HTML file).

## Documentation Updates

### 13. Test Reporting Documentation
```bash
# Updated docs/test-reporting.md to include Allure viewing instructions
```
**Purpose:** Added crucial information about how to properly view Allure reports, including the warning about file:// protocol issues and multiple methods to serve the reports.

## Key Technical Insights

### Why HTTP Server is Needed for Allure Reports:
- **CORS Restrictions**: Modern browsers block certain JavaScript operations when opening files directly
- **AJAX Requests**: Allure uses AJAX to dynamically load test data
- **Security Policies**: Browsers restrict file:// access for security reasons

### Common Issues Encountered:
1. **Lombok Processing**: Required explicit annotation processor configuration
2. **HQL Queries**: Temporal operations need parameterized queries
3. **Validation Constraints**: @Positive vs @Min for different business rules
4. **Exception Handling**: Proper HTTP status codes for different scenarios
5. **Report Viewing**: Allure reports require web server, not file:// protocol

## Commands for Future Use

### Generate All Reports:
```bash
./generate-report.sh
```

### View Allure Report:
```bash
cd target/allure-report
python3 -m http.server 8080
open http://localhost:8080
```

### Run Tests with Coverage:
```bash
mvn clean test jacoco:report
```

### Generate Surefire Report:
```bash
mvn surefire-report:report
```

## Project Structure Created:
- ✅ Spring Boot REST API with User and Product endpoints
- ✅ Comprehensive API tests using RestAssured
- ✅ Multiple test reporting systems (Allure, Surefire, JaCoCo)
- ✅ Beautiful HTML dashboards
- ✅ Extensive documentation
- ✅ Automated report generation script

## Final Status:
- ✅ All tests passing (19 tests)
- ✅ All reports generating successfully
- ✅ Allure report working with HTTP server
- ✅ Documentation complete with viewing instructions
- ✅ Project ready for production use
