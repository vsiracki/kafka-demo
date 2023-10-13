package com.htec.microservices.twitter.to.kafka.service.listener;

import com.htec.microservices.config.KafkaConfigData;
import com.htec.microservices.kafka.avro.model.TwitterAvroModel;
import com.htec.microservices.kafka.producer.config.service.KafkaProducer;
import com.htec.microservices.twitter.to.kafka.service.transformer.TwitterStatusToAvroTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterToKafkaListener extends StatusAdapter {

    private static final Logger log = LoggerFactory.getLogger(TwitterToKafkaListener.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;
    private final TwitterStatusToAvroTransformer transformer;

    TwitterToKafkaListener(KafkaConfigData kafkaConfigData,
                           KafkaProducer<Long, TwitterAvroModel> kafkaProducer,
                           TwitterStatusToAvroTransformer transformer) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.transformer = transformer;
    }

    @Override
    public void onStatus(Status status) {
        log.info("Twitter status with text {} ", status.getText());
        TwitterAvroModel avroModel = transformer.getTwitterAvroModelFromStatus(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(), avroModel.getUserId(), avroModel);
    }
}
