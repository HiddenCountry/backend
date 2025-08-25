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
            "/place"
    );
}
