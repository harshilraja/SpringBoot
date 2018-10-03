package com.company.auth.service;

import java.util.List;

import com.company.auth.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
    
    List<User> getAllUsers();
}