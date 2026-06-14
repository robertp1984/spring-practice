package org.softwarecave.springjpa.asset.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.softwarecave.common.avro.AssetAction;
import org.softwarecave.common.avro.AssetEvent;
import org.softwarecave.springjpa.asset.converters.AssetAvroConverter;
import org.softwarecave.springjpa.asset.model.Asset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssetKafkaPublisher {

    private final String topicName;
    private final KafkaTemplate<String, AssetEvent> kafkaTemplate;
    private final AssetAvroConverter assetAvroConverter;

    public AssetKafkaPublisher(@Value("${app.asset.kafka-publisher.topic-name}") String topicName,
                               KafkaTemplate<String, AssetEvent> kafkaTemplate,
                               AssetAvroConverter assetAvroConverter) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
        this.assetAvroConverter = assetAvroConverter;
    }

    public void sendAdded(Asset asset) {
        AssetEvent event = assetAvroConverter.toAssetAvro(asset);
        log.info("Sending the event {} to topic {}", event, topicName);
        var future = kafkaTemplate.send(topicName, event.getAsset().getId().toString(), event);
        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("Sent event successfully to topic {} partition {} and offset {}", recordMetadata.topic(),
                        recordMetadata.partition(), recordMetadata.offset());
            }
        });
    }


}
