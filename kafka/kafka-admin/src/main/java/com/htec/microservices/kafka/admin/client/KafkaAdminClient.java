package com.htec.microservices.kafka.admin.client;

import com.htec.microservices.config.KafkaConfigData;
import com.htec.microservices.config.RetryConfigData;
import com.htec.microservices.kafka.admin.exception.KafkaClientException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaAdminClient {

    private static final Logger log = LoggerFactory.getLogger(KafkaAdminClient.class);
    private final KafkaConfigData kafkaConfigurationData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    KafkaAdminClient(KafkaConfigData kafkaConfigurationData,
                     RetryConfigData retryConfigData,
                     AdminClient adminClient,
                     RetryTemplate retryTemplate,
                     WebClient webClient) {
        this.kafkaConfigurationData = kafkaConfigurationData;
        this.retryConfigData = retryConfigData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }


    public void createTopics() {
        try {
           retryTemplate.execute(this::doCreateTopics);
        } catch (Throwable e) {
           throw new KafkaClientException("Reached max number of retry for creating kafka topic(s)!");
        }
        checkTopicsCreated();
    }

    private void checkTopicsCreated() {
        var topics = getTopics();
        var retryCount = 1;
        var maxRetry = retryConfigData.getMaxAttempts();
        var multiplier = retryConfigData.getMultiplier().intValue();
        var sleepMs = retryConfigData.getSleepTimeMs();

        for (String topic: kafkaConfigurationData.getTopicNamesToCreate()) {
            while(!isTopicCreated(topics, topic)) {
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleepMs);
                sleepMs *= multiplier;
                topics = getTopics();
            }
        }
    }

    public void checkSchemaRegistry() {
        var retryCount = 1;
        var maxRetry = retryConfigData.getMaxAttempts();
        var multiplier = retryConfigData.getMultiplier().intValue();
        var sleepTimeMs = retryConfigData.getSleepTimeMs();

        while (!getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
        }
    }

    private HttpStatus getSchemaRegistryStatus() {
        try {
            return HttpStatus.valueOf(Objects.requireNonNull(webClient
                    .method(HttpMethod.GET)
                    .uri(kafkaConfigurationData.getSchemaRegistryUrl())
                    .exchange()
                    .map(ClientResponse::statusCode)
                    .block()).value());
        } catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private void sleep(Long sleepMs) {
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while sleeping for waiting new created topics!");
        }
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
        if (retry > maxRetry) {
            throw new KafkaClientException("Reached max number of retry for reading kafka topic(s)!");
        }
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if (Objects.isNull(topics)) {
            return false;
        }
        return topics.stream().anyMatch(topic -> topic.name().equalsIgnoreCase(topicName));
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        var topicNames = kafkaConfigurationData.getTopicNamesToCreate();
        log.info("Creating {} topics(s), attempts{} ", topicNames.size(), retryContext.getRetryCount());
        var kafkaTopics = topicNames.stream().map(topic ->
                new NewTopic(
                    topic.trim(),
                    kafkaConfigurationData.getNumOfPartitions(),
                    kafkaConfigurationData.getReplicationFactor()
                )
        ).toList();

        return adminClient.createTopics(kafkaTopics);
    }

    private Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch (Exception e) {
            throw new KafkaClientException("Reached max number of retry for creating kafka topic(s)!");
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        log.info("Reading kafka topic {}, attempt {}", kafkaConfigurationData.getTopicNamesToCreate().toArray(),
                retryContext.getRetryCount());
        var topics = adminClient.listTopics().listings().get();
        if (Objects.nonNull(topics)) {
            topics.forEach(topic ->
                    log.debug("Topic with name {} ", topic.name()));
        }

        return topics;
    }
}
