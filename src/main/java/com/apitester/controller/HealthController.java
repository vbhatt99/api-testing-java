package com.apitester.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Provides endpoints for monitoring application health
 */
@RestController
@RequestMapping("/health")
@Slf4j
@Tag(name = "Health Check", description = "APIs for monitoring application health")
public class HealthController {
    
    /**
     * Basic health check
     */
    @GetMapping
    @Operation(summary = "Health check", description = "Returns basic application health status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application is healthy")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.info("GET /health - Health check requested");
        
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("application", "API Testing Java");
        healthStatus.put("version", "1.0.0");
        
        return ResponseEntity.ok(healthStatus);
    }
    
    /**
     * Detailed health check
     */
    @GetMapping("/detailed")
    @Operation(summary = "Detailed health check", description = "Returns detailed application health information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detailed health information")
    })
    public ResponseEntity<Map<String, Object>> detailedHealthCheck() {
        log.info("GET /health/detailed - Detailed health check requested");
        
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("application", "API Testing Java");
        healthStatus.put("version", "1.0.0");
        
        // Database status (simplified)
        Map<String, Object> database = new HashMap<>();
        database.put("status", "UP");
        database.put("type", "H2");
        database.put("url", "jdbc:h2:mem:testdb");
        healthStatus.put("database", database);
        
        // Memory status
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> memory = new HashMap<>();
        memory.put("total", runtime.totalMemory());
        memory.put("free", runtime.freeMemory());
        memory.put("used", runtime.totalMemory() - runtime.freeMemory());
        memory.put("max", runtime.maxMemory());
        healthStatus.put("memory", memory);
        
        return ResponseEntity.ok(healthStatus);
    }
    
    /**
     * Ready check for load balancers
     */
    @GetMapping("/ready")
    @Operation(summary = "Ready check", description = "Indicates if the application is ready to receive traffic")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application is ready")
    })
    public ResponseEntity<Map<String, Object>> readyCheck() {
        log.info("GET /health/ready - Ready check requested");
        
        Map<String, Object> readyStatus = new HashMap<>();
        readyStatus.put("status", "READY");
        readyStatus.put("timestamp", LocalDateTime.now());
        readyStatus.put("message", "Application is ready to handle requests");
        
        return ResponseEntity.ok(readyStatus);
    }
    
    /**
     * Live check for Kubernetes
     */
    @GetMapping("/live")
    @Operation(summary = "Live check", description = "Indicates if the application is alive")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Application is alive")
    })
    public ResponseEntity<Map<String, Object>> liveCheck() {
        log.info("GET /health/live - Live check requested");
        
        Map<String, Object> liveStatus = new HashMap<>();
        liveStatus.put("status", "ALIVE");
        liveStatus.put("timestamp", LocalDateTime.now());
        liveStatus.put("message", "Application is running");
        
        return ResponseEntity.ok(liveStatus);
    }
}
