package org.softwarecave.chat.service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final ValidatingAdvisor validatingAdvisor;

    @Bean
    @ConditionalOnProperty(prefix = "app.ai", name = "model", havingValue = "openai")
    ChatClient openAIChatClient(OpenAiChatModel model) {
        return ChatClient.builder(model)
                .defaultAdvisors(new SimpleLoggerAdvisor(), validatingAdvisor)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.ai", name = "model", havingValue = "ollama")
    ChatClient llamaChatClient(OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultAdvisors(new SimpleLoggerAdvisor(), validatingAdvisor)
                .build();
    }
}
