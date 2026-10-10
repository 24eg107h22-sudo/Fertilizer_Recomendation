package com.fertilizer.recommendation.controller;

import com.fertilizer.recommendation.dto.AuthRequest;
import com.fertilizer.recommendation.dto.AuthResponse;
import com.fertilizer.recommendation.dto.RegisterRequest;
import com.fertilizer.recommendation.entity.User;
import com.fertilizer.recommendation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication & Farmer Profile", description = "Endpoints for user registration, login, and farmer details")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new farmer account")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate farmer and login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/farmer/{id}")
    @Operation(summary = "Get farmer profile details by user ID")
    public ResponseEntity<User> getFarmerProfile(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/farmer/{id}")
    @Operation(summary = "Update farmer profile information")
    public ResponseEntity<User> updateFarmerProfile(@PathVariable Long id, @RequestBody User user) {
        try {
            User updated = userService.updateFarmerProfile(id, user);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
