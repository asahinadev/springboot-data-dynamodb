package com.example.spring.repository;

import java.util.Optional;

import com.example.spring.entity.User;

public interface UserRepository
		extends AppRepository<User, String, Void> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
}
