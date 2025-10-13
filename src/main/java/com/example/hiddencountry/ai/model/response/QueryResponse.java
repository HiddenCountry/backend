package com.example.hiddencountry.ai.model.response;

import org.springframework.ai.document.Document;
import java.util.List;

public record QueryResponse(
        String query, // 원본 질의
        String answer, // 생성된 답변
        List<Document> relevantDocuments // 관련 문서 목록
) {
    public static QueryResponse from(String query, String answer, List<Document> relevantDocuments) {
        return new QueryResponse(
                query,
                answer,
                relevantDocuments
        );
    }
}
