package org.softwarecave.chat.config;

import lombok.RequiredArgsConstructor;
import org.softwarecave.chat.config.advisors.ValidatingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final ValidatingAdvisor validatingAdvisor;

    @Bean
    @ConditionalOnProperty(prefix = "app.ai", name = "model", havingValue = "openai")
    ChatClient openAIChatClient(OpenAiChatModel model, SafeGuardAdvisor safeGuardAdvisor) {
        return ChatClient.builder(model)
                .defaultAdvisors(new SimpleLoggerAdvisor(), validatingAdvisor, safeGuardAdvisor)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.ai", name = "model", havingValue = "ollama")
    ChatClient llamaChatClient(OllamaChatModel model, SafeGuardAdvisor safeGuardAdvisor) {
        return ChatClient.builder(model)
                .defaultAdvisors(new SimpleLoggerAdvisor(), validatingAdvisor, safeGuardAdvisor)
                .build();
    }

    @Bean
    SafeGuardAdvisor customSafeGuardAdvisor() {
        return new SafeGuardAdvisor(List.of("ignore instruction", "password", "api_key", "system prompt"));
    }
}
