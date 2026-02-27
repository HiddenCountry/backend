package com.example.hiddencountry.user.model.response;

import jakarta.validation.constraints.NotEmpty;

public record AccessTokenResponse(
    @NotEmpty String accessToken,
    boolean isFirstLogin
) {
}
