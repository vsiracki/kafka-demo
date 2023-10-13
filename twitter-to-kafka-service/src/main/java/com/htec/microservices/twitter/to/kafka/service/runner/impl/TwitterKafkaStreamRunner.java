package com.htec.microservices.twitter.to.kafka.service.runner.impl;

import com.htec.microservices.config.TwitterToKafkaServiceConfigData;
import com.htec.microservices.twitter.to.kafka.service.listener.TwitterToKafkaListener;
import com.htec.microservices.twitter.to.kafka.service.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Arrays;
import java.util.Objects;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false")
public class TwitterKafkaStreamRunner implements StreamRunner {

    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);

    private final TwitterToKafkaServiceConfigData configData;
    private final TwitterToKafkaListener kafkaListener;

    private TwitterStream twitterStream;

    TwitterKafkaStreamRunner(TwitterToKafkaServiceConfigData configData,
                             TwitterToKafkaListener kafkaListener) {
        this.configData = configData;
        this.kafkaListener = kafkaListener;
    }


    @Override
    public void start() throws TwitterException {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(kafkaListener);
        addFilter();
    }

    @PreDestroy
    private void shutdown() {
        if (Objects.nonNull(twitterStream)) {
            log.info("Closing filter stream!");
            twitterStream.shutdown();
        }
    }

    private void addFilter() {
        String[] keywords = configData.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        log.info("Started filtering twitter stream for keywords {} ", Arrays.toString(keywords));
    }
}
