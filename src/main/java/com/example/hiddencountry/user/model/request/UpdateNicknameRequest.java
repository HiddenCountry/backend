package com.example.hiddencountry.user.model.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateNicknameRequest (
        @NotBlank String nickname
){
}