package com.htec.microservices.twitter.to.kafka.service;

import com.htec.microservices.twitter.to.kafka.service.init.StreamInitializer;
import com.htec.microservices.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.htec.microservices")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;

    public TwitterToKafkaServiceApplication(StreamRunner streamRunner,
                                            StreamInitializer streamInitializer) {
        this.streamRunner = streamRunner;
        this.streamInitializer = streamInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("App starts...");
        streamInitializer.init();
        streamRunner.start();
    }
}