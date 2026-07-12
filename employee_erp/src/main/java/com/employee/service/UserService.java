package com.employee.service;

import com.employee.dto.RegisterRequest;
import com.employee.entity.User;

public interface UserService {

	User register(RegisterRequest request);
	
	User findByUsername(String username);
}
