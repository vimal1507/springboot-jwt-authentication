package com.employee.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.employee.entity.RefreshToken;
import com.employee.entity.User;
import com.employee.exception.RefreshTokenExpiredException;
import com.employee.repo.RefreshTokenRepository;
import com.employee.service.RefreshTokenService;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
	@Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Value("${refresh.token.expiration}")
    private Long refreshTokenDuration;

	@Override
	public RefreshToken createRefreshToken(User user) {
		RefreshToken rt = new RefreshToken();
		rt.setToken(UUID.randomUUID().toString());
		rt.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDuration/1000));
		rt.setUser(user);
		return refreshTokenRepository.save(rt);
	}

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().isBefore(LocalDateTime.now())) {

	        refreshTokenRepository.delete(token);

	        throw new RefreshTokenExpiredException(
	                "Refresh Token Expired. Please login again.");
	    }

	    return token;
	}

	@Override
	public void delete(RefreshToken refreshToken) {
	    refreshTokenRepository.delete(refreshToken);
	}
	
	@Override
	public Optional<RefreshToken> findByUser(User user) {
	    return refreshTokenRepository.findByUser(user);
	}
	
	@Override
	@Transactional
	public RefreshToken rotateRefreshToken(RefreshToken oldToken) {

	    User user = oldToken.getUser();

	    refreshTokenRepository.delete(oldToken);

	    return createRefreshToken(user);
	}

}
