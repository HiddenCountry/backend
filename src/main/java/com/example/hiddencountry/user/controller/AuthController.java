package com.example.hiddencountry.user.controller;

import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.global.util.AuthCookieFactory;
import com.example.hiddencountry.user.domain.User;
import com.example.hiddencountry.user.model.response.AccessTokenResponse;
import com.example.hiddencountry.user.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthCookieFactory authCookieFactory;

    @Operation(
            summary = "accessToken 재발급",
            description = "refreshToken으로 accessToken을 재발급 받습니다."
    )
    @PostMapping("/auth/refresh")
    public ApiResponse<AccessTokenResponse> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        User user = authService.validateAndConsumeRefreshToken(refreshToken);
        String newAccessToken = authService.createAccessToken(user);
        String newRefreshToken = authService.createRefreshTokenAndStore(user);
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                authCookieFactory.buildRefreshCookie(newRefreshToken, authService.getRefreshTokenValidityInSeconds()).toString()
        );
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                new AccessTokenResponse(newAccessToken, authService.isFirstLogin(user))
        );
    }

    @Operation(
            summary = "로그아웃"
    )
    @PostMapping("/auth/logout")
    public ApiResponse<String> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, authCookieFactory.clearRefreshCookie().toString());
        return ApiResponse.onSuccess(SuccessStatus.OK, "logout");
    }
}
