package com.example.hiddencountry.user.model.response;

import jakarta.validation.constraints.NotEmpty;

public record AuthorizationToken(
	@NotEmpty String accessToken,
	@NotEmpty String refreshToken,
	@NotEmpty boolean isFirstLogin
) {
}
