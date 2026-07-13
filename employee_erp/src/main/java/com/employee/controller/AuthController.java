package com.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.dto.AuthResponse;
import com.employee.dto.LoginRequest;
import com.employee.dto.LogoutRequest;
import com.employee.dto.RefreshTokenRequest;
import com.employee.dto.RefreshTokenResponse;
import com.employee.dto.RegisterRequest;
import com.employee.entity.RefreshToken;
import com.employee.entity.User;
import com.employee.security.JwtService;
import com.employee.service.RefreshTokenService;
import com.employee.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
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

	    refreshTokenService.findByUser(user)
	            .ifPresent(refreshTokenService::delete);

	    RefreshToken refreshToken =
	            refreshTokenService.createRefreshToken(user);

	    String accessToken = jwtService.generateAccessToken(
	            user.getUsername(),
	            user.getRole());

	    return ResponseEntity.ok(
	            new AuthResponse(
	                    accessToken,
	                    refreshToken.getToken()));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<RefreshTokenResponse> refreshToken(
	        @RequestBody RefreshTokenRequest request) {

	    RefreshToken refreshToken = refreshTokenService
	            .findByToken(request.getRefreshToken())
	            .orElseThrow(() ->
	                    new RuntimeException("Refresh Token Not Found"));

	    refreshTokenService.verifyExpiration(refreshToken);

	    RefreshToken newRefreshToken =
	            refreshTokenService.rotateRefreshToken(refreshToken);

	    User user = newRefreshToken.getUser();

	    String accessToken = jwtService.generateAccessToken(
	            user.getUsername(),
	            user.getRole());

	    return ResponseEntity.ok(
	            new RefreshTokenResponse(
	                    accessToken,
	                    newRefreshToken.getToken()));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(
	        @RequestBody LogoutRequest request) {

	    User user = userService.findByUsername(request.getUsername());

	    RefreshToken refreshToken = refreshTokenService
	            .findByUser(user)
	            .orElseThrow(() ->
	                    new RuntimeException("Refresh Token Not Found"));

	    refreshTokenService.delete(refreshToken);

	    return ResponseEntity.ok("Logout Successful");
	}

}
