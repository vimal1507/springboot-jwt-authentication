package com.employee.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private long expiration;
	
	private SecretKey getSignKey() {
		System.out.println("Secret = " + secret);
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
	
	public String generateAccessToken(String username, String role) {

	    return Jwts.builder()
	            .subject(username)
	            .claim("role", role)
	            .issuedAt(new Date(System.currentTimeMillis()))
	            .expiration(new Date(System.currentTimeMillis() + expiration))
	            .signWith(getSignKey())
	            .compact();
	}
	
	public String extractUsername(String token) {

	    Claims claims = Jwts.parser()
	            .verifyWith(getSignKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();

	    return claims.getSubject();
	}
	
	public String extractRole(String token) {

	    Claims claims = Jwts.parser()
	            .verifyWith(getSignKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();

	    return claims.get("role", String.class);
	}
	
	public boolean isTokenExpired(String token) {

	    Claims claims = Jwts.parser()
	            .verifyWith(getSignKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();

	    return claims.getExpiration().before(new Date());
	}
	
	public boolean validateToken(String token, String username) {

	    String extractedUsername = extractUsername(token);

	    return extractedUsername.equals(username)
	            && !isTokenExpired(token);
	}
}
