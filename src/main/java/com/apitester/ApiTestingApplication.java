package com.apitester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for API Testing Project
 * 
 * This application provides various REST APIs for learning and testing purposes.
 * It includes:
 * - User management APIs
 * - Product catalog APIs  
 * - Order processing APIs
 * - Health check endpoints
 */
@SpringBootApplication
public class ApiTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiTestingApplication.class, args);
        System.out.println("🚀 API Testing Application started successfully!");
        System.out.println("📖 API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("🔍 H2 Database Console: http://localhost:8080/h2-console");
        System.out.println("💡 Health Check: http://localhost:8080/actuator/health");
    }
}
