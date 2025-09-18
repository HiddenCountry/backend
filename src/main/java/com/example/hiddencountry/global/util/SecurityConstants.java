package com.example.hiddencountry.global.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {
    public static final List<String> ALLOW_URLS = List.of(
            "/login/page",
            "/callback",
            "/error",
            "/actuator/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    /// 토큰이 있을 경우 인증 절차 진행하지만, 없을 경우에도 예외를 발생시키지는 않는 GET 메서드 URL
    public static final List<String> GET__METHOD_ALLOW_URLS = List.of(
        "/places",
        "/places/map",
        "/place"
    );
}
