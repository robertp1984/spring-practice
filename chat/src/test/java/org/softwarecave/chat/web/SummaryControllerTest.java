package org.softwarecave.chat.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.softwarecave.chat.model.Summary;
import org.softwarecave.chat.service.summary.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SummaryController.class)
@DisplayName("SummaryController Integration Tests")
class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SummaryService summaryService;

    @Test
    void testSummarizeSuccess() throws Exception {
        // Arrange
        String inputMessage = "This is a test message to summarize";
        Summary expectedSummary = new Summary(1L, inputMessage, "Summary: Test message");

        when(summaryService.summarize(inputMessage)).thenReturn(expectedSummary);

        // Act & Assert
        mockMvc.perform(post("/api/v1/summarization")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(inputMessage))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Summary: Test message")))
                .andExpect(header().exists("Location"));

        verify(summaryService, times(1)).summarize(inputMessage);
    }

    @Test
    void testSummarizeWithEmptyInput() throws Exception {
        // Arrange
        String inputMessage = "";
        Summary emptySummary = new Summary(1L, inputMessage, "");

        when(summaryService.summarize(inputMessage)).thenReturn(emptySummary);

        // Act & Assert
        mockMvc.perform(post("/api/v1/summarization")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(inputMessage))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testSummarizeReturnsLocationHeader() throws Exception {
        // Arrange
        String inputMessage = "Test message";
        Summary expectedSummary = new Summary(42L, inputMessage, "Summarized");

        when(summaryService.summarize(inputMessage)).thenReturn(expectedSummary);

        // Act & Assert
        mockMvc.perform(post("/api/v1/summarization")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(inputMessage))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("42")));
    }

    @Test
    void testSummarizeStreamSuccess() throws Exception {
        // Arrange
        String inputMessage = "Stream test message";
        Flux<String> streamedResponse = Flux.just("Short", "content");

        when(summaryService.summarizeStream(inputMessage)).thenReturn(streamedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/summarization/stream")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(inputMessage))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM_VALUE));

        verify(summaryService, times(1)).summarizeStream(inputMessage);
    }

    @Test
    @DisplayName("POST /api/v1/summarization/stream should handle empty stream")
    void testSummarizeStreamEmpty() throws Exception {
        // Arrange
        String inputMessage = "Empty stream test";
        Flux<String> emptyStream = Flux.empty();

        when(summaryService.summarizeStream(inputMessage)).thenReturn(emptyStream);

        // Act & Assert
        mockMvc.perform(post("/api/v1/summarization/stream")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(inputMessage))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM_VALUE));
    }

    @Test
    void testSummarizeWithMultilineMessage() throws Exception {
        // Arrange
        String multilineMessage = "Line 1: Important task\nLine 2: Review needed\nLine 3: Deploy changes";
        String summary = "• Important task\nReview needed\nDeploy changes";
        Summary expectedSummary = new Summary(4L, multilineMessage, summary);

        when(summaryService.summarize(multilineMessage)).thenReturn(expectedSummary);

        // Act & Assert
        mockMvc.perform(post("/api/v1/summarization")
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(multilineMessage))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Important task")))
                .andExpect(content().string(containsString("Review needed")))
                .andExpect(content().string(containsString("Deploy changes")));
    }
}
