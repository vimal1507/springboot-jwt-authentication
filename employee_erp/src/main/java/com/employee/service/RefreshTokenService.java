package com.employee.service;

import java.util.Optional;

import com.employee.entity.RefreshToken;
import com.employee.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);

    void deleteByUser(User user);

}