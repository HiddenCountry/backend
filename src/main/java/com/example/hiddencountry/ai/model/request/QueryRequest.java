package com.example.hiddencountry.ai.model.request;

import jakarta.validation.constraints.NotBlank;

public record QueryRequest(
        @NotBlank String query,
        @NotBlank String sessionId
) {}
