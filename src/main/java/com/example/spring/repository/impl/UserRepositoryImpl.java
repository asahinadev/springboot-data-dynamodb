package com.example.spring.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.spring.entity.User;
import com.example.spring.repository.UserRepository;

@Repository
public class UserRepositoryImpl
		extends AppRepositoryImpl<User, String, Void>
		implements UserRepository {

	@Override
	public Optional<User> findByUsername(String username) {

		List<User> users = index("users_username", "username", username);
		if (users.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(users.get(0));
	}

	@Override
	public Optional<User> findByEmail(String email) {

		List<User> users = index("users_email", "email", email);
		if (users.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(users.get(0));
	}

}
