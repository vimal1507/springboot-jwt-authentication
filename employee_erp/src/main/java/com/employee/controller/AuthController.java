package com.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.dto.AuthResponse;
import com.employee.dto.LoginRequest;
import com.employee.dto.RegisterRequest;
import com.employee.entity.User;
import com.employee.security.JwtService;
import com.employee.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody RegisterRequest request){
		User user = userService.register(request);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

	    authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    request.getUsername(),
	                    request.getPassword()));

	    User user = userService.findByUsername(request.getUsername());

	    String token = jwtService.generateToken(user.getUsername(),user.getRole());

	    return ResponseEntity.ok(new AuthResponse(token));
	}

}
