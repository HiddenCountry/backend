package com.example.hiddencountry.user.service;

import com.example.hiddencountry.global.jwt.JwtTokenProvider;
import com.example.hiddencountry.global.jwt.RefreshTokenService;
import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.user.domain.User;
import com.example.hiddencountry.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    public User validateAndConsumeRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw ErrorStatus.INVALID_TOKEN.serviceException();
        }

        Long tokenUserId = jwtTokenProvider.parseRefreshToken(refreshToken);
        Long storedUserId = refreshTokenService.getUserId(refreshToken);
        if (storedUserId == null || !storedUserId.equals(tokenUserId)) {
            throw ErrorStatus.INVALID_TOKEN.serviceException();
        }

        User user = userRepository.findById(storedUserId)
                .orElseThrow(ErrorStatus.NOT_AUTHORIZED::serviceException);

        refreshTokenService.delete(refreshToken);
        return user;
    }

    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.delete(refreshToken);
        }
    }

    public String createAccessToken(User user) {
        return jwtTokenProvider.createAccessToken(user);
    }

    public String createRefreshTokenAndStore(User user) {
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);
        refreshTokenService.store(newRefreshToken, user.getId(), Duration.ofSeconds(refreshTokenValidityInSeconds));
        return newRefreshToken;
    }

    public long getRefreshTokenValidityInSeconds() {
        return refreshTokenValidityInSeconds;
    }

    public boolean isFirstLogin(User user) {
        return "hiddencountry-new-kakao-user".equals(user.getNickname());
    }
}
