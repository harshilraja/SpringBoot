package com.company.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
