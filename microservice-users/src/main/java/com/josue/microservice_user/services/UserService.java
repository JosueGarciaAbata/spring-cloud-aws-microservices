package com.josue.microservice_user.services;

import com.josue.microservice_user.entities.User;

import java.util.List;

public interface UserService {
    User save(User user);
    User update(Long id, User user);
    void updatePassword(Long id, String newPassword);
    List<User> findAll();
    User findById(Long id);
    void deleteById(Long id);
    User findByUsername(String username);
    boolean existByUsername(String username);
    boolean existsByEmail(String email);
}
