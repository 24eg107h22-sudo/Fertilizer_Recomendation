package com.fertilizer.recommendation.service;

import com.fertilizer.recommendation.dto.AuthRequest;
import com.fertilizer.recommendation.dto.AuthResponse;
import com.fertilizer.recommendation.dto.RegisterRequest;
import com.fertilizer.recommendation.entity.User;
import com.fertilizer.recommendation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return new AuthResponse(false, "Email address already registered", null, null, null, null, null);
        }

        User user = new User();
        user.setName(req.getName().trim());
        user.setEmail(req.getEmail().trim().toLowerCase());
        user.setPassword(req.getPassword());
        user.setPhone(req.getPhone());
        user.setLocation(req.getLocation());
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return new AuthResponse(true, "Registration successful!", saved.getId(), saved.getName(), saved.getEmail(), saved.getPhone(), saved.getLocation());
    }

    public AuthResponse login(AuthRequest req) {
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail().trim().toLowerCase());
        if (userOpt.isEmpty()) {
            return new AuthResponse(false, "Account with this email does not exist", null, null, null, null, null);
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(req.getPassword())) {
            return new AuthResponse(false, "Invalid password credentials", null, null, null, null, null);
        }

        return new AuthResponse(true, "Login successful!", user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getLocation());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateFarmerProfile(Long id, User updatedInfo) {
        return userRepository.findById(id).map(user -> {
            if (updatedInfo.getName() != null) user.setName(updatedInfo.getName());
            if (updatedInfo.getPhone() != null) user.setPhone(updatedInfo.getPhone());
            if (updatedInfo.getLocation() != null) user.setLocation(updatedInfo.getLocation());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Farmer profile not found for id: " + id));
    }
}
