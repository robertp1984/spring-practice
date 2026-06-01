package org.softwarecave.springjpa.asset.messaging.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.common.avro.AssetEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;

import static org.softwarecave.springjpa.asset.messaging.KafkaConsumerConfig.ASSET_CONTAINER_FACTORY;

@Service
@Slf4j
@RequiredArgsConstructor
public class IncomingAssetConsumer {

    private final IncomingAssetProcessor assetProcessor;

    @KafkaListener(topics = "${app.asset.incoming.topic-name}",
            containerFactory = ASSET_CONTAINER_FACTORY)
    public void consumeAsset(AssetEvent event) {
        log.debug("Received incoming asset {}", event);
        if (event.getAsset().getId().endsWith("5")) {
            throw new NonRetryableException("Hardcoded not to try the assets with id ending with 5");
        }
        assetProcessor.handleIncomingAsset(event);
    }


}
