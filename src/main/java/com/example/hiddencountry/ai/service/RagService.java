package com.example.hiddencountry.ai.service;

import com.example.hiddencountry.ai.model.response.QueryResponse;
import com.example.hiddencountry.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagService {

    private final ChatService chatService;
    private final VectorStore vectorStore;

    /**
     * 질의와 관련된 문서를 검색합니다.
     *
     * @param question   사용자 질문(자연어)
     * @return 유사도 순 정렬 결과
     */
    public List<Document> retrieve(String question) {
        log.debug("검색 시작: '{}', 최대 결과 수: {}", question, 5);

        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder().query(question).topK(3).build());

        log.debug("검색 결과 건수: {}", results.size());
        return results;
    }

    /**
     * rag 챗봇 기능을 수행합니다.
     *
     * @param question 질문
     * @param relevantDocs rag 문서
     * @param sessionId 세션 id
     * @return 챗 응답 결과
     */
    public QueryResponse generateAnswerWithContexts(String question, List<Document> relevantDocs, String sessionId) {

        if (relevantDocs == null || relevantDocs.isEmpty()) {
            log.debug("관련 정보를 찾을 수 없음: '$question'");
            throw ErrorStatus.RETRIEVE_NOT_FOUND.serviceException();
        }

        // 문서 번호 부여 (응답에서 출처 표시를 위해)
        List<String> numberedDocs = new ArrayList<>();
        for (int i = 0; i < relevantDocs.size(); i++) {
            Document doc = relevantDocs.get(i);
            numberedDocs.add("[" + (i + 1) + "] " + doc.getText());
        }

        // 관련 문서의 내용을 컨텍스트로 결합
        String context = String.join("\n\n", numberedDocs);
        log.debug("컨텍스트 크기: {} 문자", context.length());


        String systemPromptText =
                "당신은 여행 계획 설계를 돕는 Q&A 챗봇입니다.\n" +
                        "사용자의 질문에 대한 답변을 제공되는 정보를 바탕으로 생성해주세요.\n" +
                        "이전 질문과 답변을 참고해서 답변을 해주세요.\n" +
                        "사용자가 장소 언급을 안하면서 이전 장소에 대해 질문하는 것 같으면 이전 정보를 가지고 답변해주세요.\n" +
                        "사용자가 지역을 언급한다면 사용자가 말한 지역과 장소의 address와 시·군·구 행정구역이 일치할 때만 추천하세요.\n" +
                        "어떠한 경우에도 존댓말로 답을 하세요.\n" +
                        "사용자에게 질문으로 답변을 끝내지 말고 무조건 평서문으로 답을 하세요." +
                        "정보:\n" + context;

            ChatResponse response = chatService.openAiChat(question, systemPromptText, "gpt-4o-mini", sessionId, context);
            log.debug("AI 응답 생성: {}", response);
            String text = response.getResult().getOutput().getText();
//
//            // 참고 문서 정보 추가
//            StringBuilder sourceInfo = new StringBuilder();
//            sourceInfo.append("\n\n참고 문서:\n");
//            for (int i = 0; i < relevantDocs.size(); i++) {
//                Document doc = relevantDocs.get(i);
//                Map<String, Object> meta = doc.getMetadata() != null ? doc.getMetadata() : Collections.emptyMap();
//                String originalFilename = meta.getOrDefault("originalFilename", "Unknown file").toString();
//                sourceInfo.append("[").append(i + 1).append("] ").append(originalFilename).append("\n");
//            }

//            return QueryResponseDto.from(question,response.toString() + sourceInfo, relevantDocs);
            return QueryResponse.from(question,text, relevantDocs);
    }
}
