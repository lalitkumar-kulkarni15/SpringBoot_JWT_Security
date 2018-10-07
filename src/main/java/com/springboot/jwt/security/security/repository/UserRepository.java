package com.springboot.jwt.security.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jwt.security.model.User;

/**
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
