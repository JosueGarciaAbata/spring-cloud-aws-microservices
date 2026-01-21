package com.josue.microservice_user.services;

import com.josue.microservice_user.entities.Role;
import com.josue.microservice_user.entities.User;
import com.josue.microservice_user.repositories.RoleRepository;
import com.josue.microservice_user.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final String defaultRole = "ROLE_USER";
    private final String adminRole = "ROLE_ADMIN";

    private final UserRepository repository;
    private final UserService service;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository  roleRepository;

    public UserServiceImpl(UserRepository repository,
                           UserService service,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.repository = repository;
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User save(User user) {
        if (existByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        // Lo correcto es que venga un DTO, de ese dto yo tengo los datos, lo mapeo como entidad, le agrego los roles y lo guardo.
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoles = roleRepository.findByName(defaultRole);
        optionalRoles.ifPresent(roles::add);
        if (user.isAdmin()) {
            Optional<Role> optionalAdminRole = roleRepository.findByName(defaultRole);
            optionalAdminRole.ifPresent(roles::add);
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Transactional
    // Por simplicidad solo se puede actualizar el email.
    // Lo correcto es cambiar la contrasena, username, verificar que la contrasena sea segurda, qeu el username, email no se repita...
    @Override
    public User update(Long id, User user) {

        User userFound = service.findById(id);
        if (existsByEmail(user.getEmail()) || existByUsername(user.getUsername())) {
            throw new RuntimeException("Email or username already exists");
        }
        userFound.setEmail(user.getEmail());
        userFound.setUsername(user.getUsername());
        return repository.save(userFound);
    }

    @Transactional
    @Override
    public void updatePassword(Long id, String newPassword) {
        User user = service.findById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        service.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        service.findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Override
    public boolean existByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
