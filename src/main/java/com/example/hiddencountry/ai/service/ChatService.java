package com.example.hiddencountry.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiApi openAiApi;
    private final ChatMemory chatMemory;

    /**
     * OpenAI 챗 API를 이용하여 응답을 생성합니다.
     *
     * @param userInput     사용자 입력 메시지
     * @param systemMessage 시스템 프롬프트
     * @param model         사용할 LLM 모델명 (예: "gpt-3.5-turbo", "gpt-4o-mini")
     * @return ChatResponse OpenAI 챗 응답
     */
    public ChatResponse openAiChat(String userInput, String systemMessage, String model) {
        log.debug("OpenAI 챗 호출 시작 - 모델: {}", model);
            // 메시지 구성
            List<Message> messages = List.of(
                    new SystemMessage(systemMessage),
                    new UserMessage(userInput)
            );

            // 챗 옵션 설정
            ChatOptions chatOptions = ChatOptions.builder()
                    .model(model)   // ← M3 기준
                    .build();

            // 프롬프트 생성
            Prompt prompt = new Prompt(messages, chatOptions);

            // 챗 모델 생성 및 호출
            OpenAiChatModel chatModel = OpenAiChatModel.builder()
                    .openAiApi(openAiApi)
                    .build();

            return chatModel.call(prompt);
    }

    /** 세션 (conversationId) 기반으로 메모리를 호출하여 응답을 생성합니다.
     *
     * @param userInput 사용자 입력 메시지
     * @param systemMessage 시스템 프롬프트
     * @param model 사용할 LLM 모델명 (예: "gpt-3.5-turbo", "gpt-4o-mini")
     * @param conversationId 대화 세션 id
     * @return ChatResponse OpenAI 챗 응답
     */
    public ChatResponse openAiChat(String userInput, String systemMessage, String model, String conversationId) {
        log.debug("OpenAI 챗 호출(메모리) - model: {}, convId: {}", model, conversationId);

        List<Message> history = chatMemory.get(conversationId);
        List<Message> messages = new ArrayList<>();
        if (history != null && !history.isEmpty()) messages.addAll(history);

        messages.add(new SystemMessage(systemMessage));
        messages.add(new UserMessage(userInput));

        ChatOptions chatOptions = ChatOptions.builder().model(model).build();
        Prompt prompt = new Prompt(messages, chatOptions);
        OpenAiChatModel chatModel = OpenAiChatModel.builder().openAiApi(openAiApi).build();
        ChatResponse response = chatModel.call(prompt);

        String assistantText = response.getResult().getOutput().getText();
        chatMemory.add(conversationId, List.of(
                new UserMessage(userInput),
                new org.springframework.ai.chat.messages.AssistantMessage(assistantText)
        ));
        return response;
    }
}
