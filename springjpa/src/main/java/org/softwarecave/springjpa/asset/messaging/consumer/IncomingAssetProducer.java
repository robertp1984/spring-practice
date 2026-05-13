package org.softwarecave.springjpa.asset.messaging.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.common.avro.Asset;
import org.softwarecave.common.avro.AssetAction;
import org.softwarecave.common.avro.AssetEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.random.RandomGeneratorFactory;


@Service
@Slf4j
public class IncomingAssetProducer {

    private static final int BOUND = 100000;

    private final KafkaTemplate<String, AssetEvent> kafkaTemplate;
    private final String topicName;
    private final String nameFormat;
    private final String descriptionFormat;

    public IncomingAssetProducer(KafkaTemplate<String, AssetEvent> kafkaTemplate,
                                 @Value("${app.asset.incoming.topic-name}") String topicName,
                                 @Value("${app.asset.incoming.name-format}") String nameFormat,
                                 @Value("${app.asset.incoming.description-format}") String descriptionFormat) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
        this.nameFormat = nameFormat;
        this.descriptionFormat = descriptionFormat;
    }

    @Scheduled(initialDelay = 10000, fixedRate = 5000)
    public void generateNewAsset() {
        var rand = RandomGeneratorFactory.getDefault().create();

        int number = rand.nextInt(BOUND);
        String name = nameFormat.formatted(number);
        String desc = descriptionFormat.formatted(number);
        var assetEvent = AssetEvent.newBuilder()
                .setAsset(Asset.newBuilder()
                        .setId(Integer.toHexString(number))
                        .setName(name)
                        .setDescription(name)
                        .build())
                .setAction(AssetAction.ADD)
                .build();

        kafkaTemplate.send(topicName, assetEvent.getAsset().getId(), assetEvent);
        log.debug("Sent incoming asset {} to topic {}", assetEvent, topicName);
    }
}
