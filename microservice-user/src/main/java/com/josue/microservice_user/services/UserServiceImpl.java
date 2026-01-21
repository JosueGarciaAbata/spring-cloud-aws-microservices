package com.josue.microservice_user.services;

import com.josue.microservice_user.entities.User;
import com.josue.microservice_user.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Override
    public void deleteById(Long id) {
        this.findById(id);
        repository.deleteById(id);
    }
}
