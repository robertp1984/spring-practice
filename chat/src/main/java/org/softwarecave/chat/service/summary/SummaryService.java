package org.softwarecave.chat.service.summary;

import lombok.extern.slf4j.Slf4j;
import org.softwarecave.chat.model.Summary;
import org.softwarecave.chat.service.config.ChatOptionsFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@Slf4j
public class SummaryService {

    private static final String THINKING = "thinking";
    private static final String SYSTEM_PROMPT = """
            You are summarizing the messages using simple and clear English.
            If possible please use the bullet points to indicate the tasks and actions.
            Please add at most 3 tags at the bottom related to the message being summarized.
            """;

    private final SummaryRepository summaryRepository;
    private final ChatOptionsFactory chatOptionsFactory;
    private final ChatClient chatClient;
    private final int streamBufferMaxSize;
    private final int streamBufferMaxTime;


    public SummaryService(SummaryRepository summaryRepository, ChatOptionsFactory chatOptionsFactory,
                          ChatClient chatClient,
                          @Value("${app.ai.stream.buffer.max-size}") int streamBufferMaxSize,
                          @Value("${app.ai.stream.buffer.max-time}") int streamBufferMaxTime) {
        this.summaryRepository = summaryRepository;
        this.chatOptionsFactory = chatOptionsFactory;
        this.chatClient = chatClient;
        this.streamBufferMaxSize = streamBufferMaxSize;
        this.streamBufferMaxTime = streamBufferMaxTime;
    }


    @Transactional
    public Summary summarize(String text) {
        var textSummary = getAISummary(text);
        log.info("The summarized text is {}", textSummary);

        return saveSummary(text, textSummary);
    }

    public Summary saveSummary(String text, String textSummary) {
        return summaryRepository.save(new Summary(text, textSummary));
    }

    private String getAISummary(String msg) {
        var chatOptions = chatOptionsFactory.create(400, 1.0);

        var response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(msg)
                .options(chatOptions)
                .call();

        var chatResponse = response.chatResponse();
        if (chatResponse != null && chatResponse.getResult() != null) {
            Generation result = chatResponse.getResult();
            String thinking = result.getMetadata().get(THINKING);
            log.info("Thinking method: {}", thinking);
        }

        return response.content();
    }

    public Flux<String> summarizeStream(String text) {
        //        log.info("The summarized text is " + textSummary);
        //        saveSummary(text, textSummary);
        return getAISummaryStream(text);
    }

    private Flux<String> getAISummaryStream(String msg) {
        var response = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(msg)
                .options(OpenAiChatOptions.builder()
                        .maxCompletionTokens(400))
                .stream();
        Flux<String> responseTokens = response.content();

        return responseTokens.bufferTimeout(streamBufferMaxSize, Duration.ofMillis(streamBufferMaxTime))
                .map(list -> String.join(" ", list));
    }

}
