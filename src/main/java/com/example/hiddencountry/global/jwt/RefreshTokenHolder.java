package com.example.hiddencountry.global.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshTokenHolder {
	protected static final Map<String, Long> refreshTokens = new HashMap<>();

	public static Long getRefreshToken(String refreshToken) {
		return refreshTokens.get(refreshToken);
	}

	public static void setRefreshToken(String refreshToken, Long userId) {
		refreshTokens.put(refreshToken, userId);
	}

	public static void removeUserRefreshToken(String refreshToken) {
		refreshTokens.remove(refreshToken);
	}
}
