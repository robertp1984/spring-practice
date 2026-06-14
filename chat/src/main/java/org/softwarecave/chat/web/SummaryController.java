package org.softwarecave.chat.web;

import lombok.RequiredArgsConstructor;
import org.softwarecave.chat.service.summary.SummaryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/summarization")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping
    public ResponseEntity<String> summary(@RequestBody String msg) {
        var summary = summaryService.summarize(msg);
        var summarizedMsg = summary.getTextSummary();

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("{id}")
                .build(summary.getId());
        return ResponseEntity.created(uri).body(summarizedMsg);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> summaryStream(@RequestBody String msg) {
        var summarizedMsg = summaryService.summarizeStream(msg);
        return ResponseEntity.ok(summarizedMsg);
    }

}
