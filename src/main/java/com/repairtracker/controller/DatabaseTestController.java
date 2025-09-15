package com.repairtracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                response.put("status", "success");
                response.put("message", "Database connection successful");
                response.put("database", connection.getCatalog());
                response.put("url", connection.getMetaData().getURL());
                response.put("driver", connection.getMetaData().getDriverName());
                response.put("driverVersion", connection.getMetaData().getDriverVersion());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Database connection failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Database connection failed: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(response);
        }
    }
}