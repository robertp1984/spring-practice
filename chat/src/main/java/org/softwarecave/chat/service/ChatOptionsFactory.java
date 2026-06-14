package org.softwarecave.chat.service;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatOptionsFactory {

    private final String aiModel;

    public ChatOptionsFactory(@Value("${app.ai.model}") String aiModel) {
        this.aiModel = aiModel;
    }

    public ChatOptions.Builder<?> create(int maxTokens) {
        return switch (aiModel) {
            case "openai" -> OpenAiChatOptions.builder()
                    .maxCompletionTokens(maxTokens);
            case "ollama" -> OllamaChatOptions.builder()
                    .maxTokens(maxTokens);
            default -> throw new IllegalArgumentException("Unrecognized model " + aiModel);
        };
    }
}
