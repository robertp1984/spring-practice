package org.softwarecave.springjpa.asset.messaging.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.common.avro.AssetEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IncomingAssetConsumer {

    private final IncomingAssetProcessor assetProcessor;

    @KafkaListener(topics = "${app.asset.incoming.topic-name}")
    public void consumeAsset(AssetEvent event) {
        log.debug("Received incoming asset {}", event);
        assetProcessor.handleIncomingAsset(event);
    }


}
