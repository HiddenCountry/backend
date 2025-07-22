package com.example.hiddencountry.user.service;

import com.example.hiddencountry.global.jwt.JwtTokenProvider;
import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.user.domain.User;
import com.example.hiddencountry.user.model.response.AuthorizationToken;
import com.example.hiddencountry.user.model.response.KakaoTokenResponseDto;
import com.example.hiddencountry.user.model.response.KakaoUserInfoResponseDto;
import com.example.hiddencountry.user.repository.UserRepository;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client_id}")
    private String clientId;

    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    public String getAccessTokenFromKakao(String code) {

        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(ErrorStatus.INVALID_PARAMETER.serviceException()))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(ErrorStatus.INTERNAL_SERVER_ERROR.serviceException()))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();


        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {

        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(ErrorStatus.INVALID_PARAMETER.serviceException()))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(ErrorStatus.INTERNAL_SERVER_ERROR.serviceException()))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }

    @Transactional
    public AuthorizationToken generateTokenAfterKakaoAuth(KakaoUserInfoResponseDto userInfo) {
        Long kakaoId = userInfo.getId();

        // 카카오 ID로 기존 유저 조회
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    // 없으면 회원가입
                    KakaoUserInfoResponseDto.KakaoAccount account = userInfo.getKakaoAccount();
                    KakaoUserInfoResponseDto.KakaoAccount.Profile profile = account.getProfile();

                    User newUser = User.builder()
                            .kakaoId(kakaoId)
                            .nickname("hiddencountry-new-kakao-user")
                            .profileImage(profile.getProfileImageUrl())
                            .build();

                    return userRepository.save(newUser);
                });

        return jwtTokenProvider.createTokenInfo(user);
    }
}