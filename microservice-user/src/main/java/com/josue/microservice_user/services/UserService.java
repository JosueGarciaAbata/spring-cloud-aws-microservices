package com.josue.microservice_user.services;

import com.josue.microservice_user.entities.User;

import java.util.List;

public interface UserService {
    User save(User user);
    List<User> findAll();
    User findById(Long id);
    void deleteById(Long id);
}
