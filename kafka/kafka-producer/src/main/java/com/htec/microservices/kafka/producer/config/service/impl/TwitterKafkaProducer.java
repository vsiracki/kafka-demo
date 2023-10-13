package com.htec.microservices.kafka.producer.config.service.impl;

import com.htec.microservices.kafka.avro.model.TwitterAvroModel;
import com.htec.microservices.kafka.producer.config.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {

    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaProducer.class);

    private final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

    TwitterKafkaProducer(KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void send(String topicName, Long key, TwitterAvroModel message) {
        log.info("Sending message=' {}' to topic='{}'", message, topicName);
        var completableFuture = kafkaTemplate.send(topicName, key, message);

       completableFuture.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                onFailure(message, topicName, throwable);
            } else {
                onSuccess(sendResult);
            }
        });
    }

    @PreDestroy
    public void close() {
        closeKafkaTemplate(kafkaTemplate);
    }
}
