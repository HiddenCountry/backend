package com.example.hiddencountry.ai.controller;

import com.example.hiddencountry.ai.model.request.QueryRequest;
import com.example.hiddencountry.ai.model.response.QueryResponse;
import com.example.hiddencountry.ai.service.RagService;
import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.user.domain.User;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final RagService ragService;
    private final ChatMemory chatMemory;

    /**
     * 사용자 질의에 대해 관련 문서를 검색하고 RAG 기반 응답을 생성합니다.
     */
    @Operation(
            summary = "RAG 질의 수행",
            description = "사용자 질문에 대해 관련 문서를 검색하고 RAG 기반 응답을 생성합니다."
    )
    @PostMapping("/query")
    public ApiResponse<QueryResponse> queryWithRag(
            @Parameter(hidden = true) @HiddenCountryUser User user,
            @Parameter(description = "질의 요청 객체", required = true) @Valid @RequestBody QueryRequest request
    ) {
        List<Document> relevantDocs = ragService.retrieve(request.query());
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                ragService.generateAnswerWithContexts(request.query(), relevantDocs, request.sessionId())
        );
    }

    @Operation(
            summary = "sessionId 발급",
            description = "챗봇 채팅 메모리 기억에 필요한 sessionId를 발급하는 API입니다."
    )
    @GetMapping("/session")
    public ApiResponse<String> create() {
        String sessionId = java.util.UUID.randomUUID().toString();
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                sessionId
        );
    }
}