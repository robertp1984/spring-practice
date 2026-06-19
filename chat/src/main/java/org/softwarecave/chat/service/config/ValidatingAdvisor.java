package org.softwarecave.chat.service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Slf4j
@Component
public class ValidatingAdvisor implements CallAdvisor, StreamAdvisor {

    private final long maxInputLength;

    public ValidatingAdvisor(@Value("${app.ai.max-input-length}") long maxInputLength) {
        this.maxInputLength = maxInputLength;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        validateInput(chatClientRequest);

        return callAdvisorChain.nextCall(chatClientRequest);
    }


    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        validateInput(chatClientRequest);

        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String ifNull(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    private void validateInput(ChatClientRequest chatClientRequest) {
        var userMessages = chatClientRequest.prompt().getUserMessages();
        long sum = userMessages.stream()
                .mapToLong(m -> ifNull(m.getText(), "").length())
                .sum();

        if (sum > maxInputLength) {
            throw new ChatInputValidationException("Chat input must be maximum %s character long".formatted(maxInputLength));
        }
    }
}
