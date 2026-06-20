package org.softwarecave.chat.service.config;

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

    public ChatOptions.Builder<?> create(Integer maxTokens, Double temperature) {
        return switch (aiModel) {
            case "openai" -> OpenAiChatOptions.builder()
                    .maxCompletionTokens(maxTokens);
            case "ollama" -> OllamaChatOptions.builder()
                    .maxTokens(maxTokens)
                    .temperature(temperature);
            default -> throw new IllegalArgumentException("Unrecognized model " + aiModel);
        };
    }
}
