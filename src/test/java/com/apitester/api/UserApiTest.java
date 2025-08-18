package com.apitester.api;

import com.apitester.model.User;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Comprehensive API Tests for User Management
 * Tests all CRUD operations and edge cases for the User API
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("User Management API")
@Feature("User CRUD Operations")
public class UserApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @Test
    @Order(1)
    @Story("Get All Users")
    @Description("Test retrieving all users from the system")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should retrieve all users successfully")
    public void testGetAllUsers() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)))
            .body("[0].id", notNullValue())
            .body("[0].username", notNullValue())
            .body("[0].email", notNullValue())
            .time(lessThan(2000L));
    }

    @Test
    @Order(2)
    @Story("Get User by ID")
    @Description("Test retrieving a specific user by their ID")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Should retrieve user by ID successfully")
    public void testGetUserById() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("username", notNullValue())
            .body("email", notNullValue())
            .body("firstName", notNullValue())
            .body("lastName", notNullValue())
            .time(lessThan(1000L));
    }

    @Test
    @Order(3)
    @Story("Get User by Username")
    @Description("Test retrieving a user by their username")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Should retrieve user by username successfully")
    public void testGetUserByUsername() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/username/john_doe")
        .then()
            .statusCode(200)
            .body("username", equalTo("john_doe"))
            .body("email", equalTo("john.doe@example.com"))
            .time(lessThan(1000L));
    }

    @Test
    @Order(4)
    @Story("Create New User")
    @Description("Test creating a new user with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Should create new user successfully")
    public void testCreateUser() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        User newUser = new User();
        newUser.setUsername("test_user_" + timestamp);
        newUser.setEmail("test" + timestamp + "@example.com");
        newUser.setFirstName("Test");
        newUser.setLastName("User");
        newUser.setPassword("password123");
        newUser.setStatus(User.UserStatus.ACTIVE);

        given()
            .contentType(ContentType.JSON)
            .body(newUser)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("username", equalTo(newUser.getUsername()))
            .body("email", equalTo(newUser.getEmail()))
            .body("firstName", equalTo(newUser.getFirstName()))
            .body("lastName", equalTo(newUser.getLastName()))
            .time(lessThan(2000L));
    }

    @Test
    @Order(5)
    @Story("Create User with Duplicate Username")
    @Description("Test creating a user with an existing username should return conflict")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Should return conflict when creating user with duplicate username")
    public void testCreateUserWithDuplicateUsername() {
        User duplicateUser = new User();
        duplicateUser.setUsername("john_doe"); // This username already exists
        duplicateUser.setEmail("new@example.com");
        duplicateUser.setFirstName("New");
        duplicateUser.setLastName("User");
        duplicateUser.setPassword("password123");
        duplicateUser.setStatus(User.UserStatus.ACTIVE);

        given()
            .contentType(ContentType.JSON)
            .body(duplicateUser)
        .when()
            .post("/users")
        .then()
            .statusCode(409) // Conflict
            .time(lessThan(2000L));
    }

    @Test
    @Order(6)
    @Story("Delete User")
    @Description("Test deleting a user by their ID")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Should delete user successfully")
    public void testDeleteUser() {
        // First create a user to delete
        String timestamp = String.valueOf(System.currentTimeMillis());
        User userToDelete = new User();
        userToDelete.setUsername("delete_test_" + timestamp);
        userToDelete.setEmail("delete" + timestamp + "@example.com");
        userToDelete.setFirstName("Delete");
        userToDelete.setLastName("Test");
        userToDelete.setPassword("password123");
        userToDelete.setStatus(User.UserStatus.ACTIVE);

        Response createResponse = given()
            .contentType(ContentType.JSON)
            .body(userToDelete)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().response();

        Long userId = createResponse.jsonPath().getLong("id");

        // Now delete the user
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("/users/" + userId)
        .then()
            .statusCode(204) // No Content
            .time(lessThan(2000L));

        // Verify user is deleted
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/" + userId)
        .then()
            .statusCode(404); // Not Found
    }

    @Test
    @Order(7)
    @Story("Get Active Users")
    @Description("Test retrieving only active users")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should retrieve active users successfully")
    public void testGetActiveUsers() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/active")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)))
            .body("[0].status", equalTo("ACTIVE"))
            .time(lessThan(1000L));
    }

    @Test
    @Order(8)
    @Story("Get Non-existent User")
    @Description("Test retrieving a user that doesn't exist")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Should return 404 for non-existent user")
    public void testGetNonExistentUser() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/99999")
        .then()
            .statusCode(404) // Not Found
            .time(lessThan(1000L));
    }

    @Test
    @Order(9)
    @Story("Get Users by Status")
    @Description("Test retrieving users filtered by status")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should retrieve users by status successfully")
    public void testGetUsersByStatus() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/status/INACTIVE")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(0)))
            .time(lessThan(1000L));
    }

    @Test
    @Order(10)
    @Story("Search Users by First Name")
    @Description("Test searching users by first name")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should search users by first name successfully")
    public void testSearchUsersByFirstName() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/search/firstname?firstName=John")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(0)))
            .time(lessThan(1000L));
    }

    @Test
    @Order(11)
    @Story("Search Users by Last Name")
    @Description("Test searching users by last name")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should search users by last name successfully")
    public void testSearchUsersByLastName() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/search/lastname?lastName=Doe")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(0)))
            .time(lessThan(1000L));
    }

    @Test
    @Order(12)
    @Story("Get Users Created After Date")
    @Description("Test retrieving users created after a specific date")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should retrieve users created after date successfully")
    public void testGetUsersCreatedAfter() {
        String date = LocalDateTime.now().minusDays(1).toString();
        
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/created-after?date=" + date)
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(0)))
            .time(lessThan(1000L));
    }

    @Test
    @Order(13)
    @Story("Get Users by Email Domain")
    @Description("Test retrieving users by email domain")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should retrieve users by email domain successfully")
    public void testGetUsersByEmailDomain() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/email-domain/example.com")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)))
            .time(lessThan(1000L));
    }

    @Test
    @Order(14)
    @Story("API Response Time Performance")
    @Description("Test that API responses are within acceptable time limits")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should respond within acceptable time limits")
    public void testApiResponseTime() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .time(lessThan(1000L)); // Should respond within 1 second
    }

    @Test
    @Order(15)
    @Story("API Content Type Validation")
    @Description("Test that API returns correct content type")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Should return correct content type")
    public void testContentType() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @Order(16)
    @Story("User Data Validation")
    @Description("Test that user data contains all required fields")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Should return user with all required fields")
    public void testUserDataValidation() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("username", notNullValue())
            .body("email", notNullValue())
            .body("firstName", notNullValue())
            .body("lastName", notNullValue())
            .body("status", notNullValue())
            .body("createdAt", notNullValue())
            .body("updatedAt", notNullValue());
    }

    @Test
    @Order(17)
    @Story("User List Structure Validation")
    @Description("Test that user list contains properly structured data")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Should return properly structured user list")
    public void testUserListStructure() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)))
            .body("[0]", hasKey("id"))
            .body("[0]", hasKey("username"))
            .body("[0]", hasKey("email"))
            .body("[0]", hasKey("firstName"))
            .body("[0]", hasKey("lastName"))
            .body("[0]", hasKey("status"));
    }

    @Test
    @Order(18)
    @Story("Error Handling")
    @Description("Test that API handles invalid requests properly")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Should handle invalid requests properly")
    public void testErrorHandling() {
        // Test invalid user ID format
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/users/invalid")
        .then()
            .statusCode(400); // Bad Request
    }
}
