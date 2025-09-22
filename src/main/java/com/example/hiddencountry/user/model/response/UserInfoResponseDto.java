package com.example.hiddencountry.user.model.response;

import com.example.hiddencountry.user.domain.User;
import jakarta.validation.constraints.NotEmpty;

public record UserInfoResponseDto(
        @NotEmpty String nickname,
        String profileImage) {
    public static UserInfoResponseDto from(User user) {
        return new UserInfoResponseDto(
                user.getNickname(),
                user.getProfileImage()
        );
    }
}
