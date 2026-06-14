package org.softwarecave.chat.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app.ai", name = "model", havingValue = "openai")
    ChatClient openAIChatClient(OpenAiChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.ai", name = "model", havingValue = "ollama")
    ChatClient llamaChatClient(OllamaChatModel model) {
        return ChatClient.builder(model).build();
    }
}
