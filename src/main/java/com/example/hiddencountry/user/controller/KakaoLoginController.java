package com.example.hiddencountry.user.controller;

import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.user.domain.User;
import com.example.hiddencountry.user.model.response.AuthorizationToken;
import com.example.hiddencountry.user.model.response.KakaoUserInfoResponseDto;
import com.example.hiddencountry.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final UserService userService;

    @GetMapping("/callback")
    public ApiResponse<AuthorizationToken> callback(@RequestParam("code") String code) {
        String accessToken = userService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto userInfo = userService.getUserInfo(accessToken);

        return ApiResponse.onSuccess(
                SuccessStatus.USER_KAKAO_LOGIN_SUCCESS,
                userService.generateTokenAfterKakaoAuth(userInfo));
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test(@HiddenCountryUser User user) {
        return user.getNickname();
    }
}