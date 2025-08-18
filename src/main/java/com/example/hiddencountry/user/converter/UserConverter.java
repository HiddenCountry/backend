package com.example.hiddencountry.user.converter;

import com.example.hiddencountry.user.domain.User;
import com.example.hiddencountry.user.model.response.KakaoUserInfoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {
    public static User userOf(KakaoUserInfoResponseDto userInfo) {
        return User.builder()
                .kakaoId(userInfo.getId())
                .nickname("hiddencountry-new-kakao-user")
                .profileImage(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                .build();
    }
}
