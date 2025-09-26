package com.example.hiddencountry.user.domain;

import com.example.hiddencountry.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "kakao_id", nullable = false, unique = true)
    private Long kakaoId;

    @NotNull
    @Column(name = "nickname", nullable = false, length = 50, unique = true)
    private String nickname;

    @Column(name = "profile_image", length = 200)
    private String profileImage;

    public String updateNickname(String nickname) {
        this.nickname = nickname;
        return this.nickname;
    }

    public String updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
        return this.profileImage;
    }
}