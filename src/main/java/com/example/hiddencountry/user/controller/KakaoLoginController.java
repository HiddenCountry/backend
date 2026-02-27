package com.example.hiddencountry.user.controller;

import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.jwt.JwtTokenProvider;
import com.example.hiddencountry.global.jwt.RefreshTokenService;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.global.util.AuthCookieFactory;
import com.example.hiddencountry.user.domain.User;
import com.example.hiddencountry.user.model.request.UpdateNicknameRequest;
import com.example.hiddencountry.user.model.response.AccessTokenResponse;
import com.example.hiddencountry.user.model.response.UserInfoResponseDto;
import com.example.hiddencountry.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuthCookieFactory authCookieFactory;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    @GetMapping("/callback")
    public ApiResponse<AccessTokenResponse> callback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        User user = userService.kakaoLoginUser(code);
        String newAccessToken = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

        refreshTokenService.store(newRefreshToken, user.getId(), Duration.ofSeconds(refreshTokenValidityInSeconds));
        response.addHeader(HttpHeaders.SET_COOKIE, authCookieFactory.buildRefreshCookie(newRefreshToken, refreshTokenValidityInSeconds).toString());

        boolean isFirstLogin = "hiddencountry-new-kakao-user".equals(user.getNickname());
        return ApiResponse.onSuccess(
                SuccessStatus.USER_KAKAO_LOGIN_SUCCESS,
                new AccessTokenResponse(newAccessToken, isFirstLogin));
    }

    @Operation(
            summary = "닉네임 수정 API",
            description = ""
    )
    @PatchMapping("/nickname")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> updateNickname(
            @Parameter(hidden = true) @HiddenCountryUser User user,
            @RequestBody @Valid UpdateNicknameRequest updateNicknameRequest)
    {
        return ApiResponse.onSuccess(
                SuccessStatus.UPDATE_NICKNAME_SUCCESS,
                userService.updateNickname(user, updateNicknameRequest));
    }

    @Operation(
            summary = "회원 정보 조회",
            description = ""
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user-info")
    public ApiResponse<UserInfoResponseDto> getUserInfo(
            @Parameter(hidden = true) @HiddenCountryUser User user) {
        return ApiResponse.onSuccess(SuccessStatus.OK,UserInfoResponseDto.from(user));
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test(@HiddenCountryUser User user) {
        return user.getNickname();
    }
}
