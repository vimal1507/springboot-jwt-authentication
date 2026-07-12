package com.employee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.employee.dto.RegisterRequest;
import com.employee.entity.User;
import com.employee.repo.UserRepository;
import com.employee.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;

	public User register(RegisterRequest request) {
		User user = new User();
		 user.setUsername(request.getUsername());
	     user.setPassword(encoder.encode(request.getPassword()));
	     user.setRole(request.getRole());
	     return userRepo.save(user);
	}
	

	public User findByUsername(String username) {

	    return userRepo.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User Not Found"));

	}

}
