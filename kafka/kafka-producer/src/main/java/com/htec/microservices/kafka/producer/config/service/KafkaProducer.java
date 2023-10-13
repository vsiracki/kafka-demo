package com.htec.microservices.kafka.producer.config.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;
import java.util.Objects;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    void send(String topicName, K key, V message);

    default void onSuccess(SendResult<Long, V> result) {
        RecordMetadata metadata = result.getRecordMetadata();
        log.debug("Received metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}; at time {} ",
                metadata.topic(),
                metadata.partition(),
                metadata.offset(),
                metadata.timestamp(),
                System.nanoTime());
    }

    default void onFailure(V message, String topicName, Throwable throwable) {
        log.error("Error while sending message {} to topic {}", message, topicName, throwable);
    }

    default void closeKafkaTemplate(KafkaTemplate<Long, V> kafkaTemplate) {
        if (Objects.nonNull(kafkaTemplate)) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
