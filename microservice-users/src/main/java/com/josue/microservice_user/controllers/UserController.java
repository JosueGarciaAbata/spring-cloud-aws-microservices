package com.josue.microservice_user.controllers;

import com.josue.microservice_user.entities.User;
import com.josue.microservice_user.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<User> create(@RequestBody User user) {
        logger.info("Request creating user {}", user);
        return ResponseEntity.ok(service.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        logger.info("Request updating user {}", user);
        return ResponseEntity.ok(service.update(id, user));
    }

    @GetMapping
    public ResponseEntity<List<User>> list() {
        logger.info("Request listing all users");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        logger.info("Request getting user {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        logger.info("Request getting user by username {}", username);
        return ResponseEntity.ok(service.findByUsername(username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Request deleting user {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
