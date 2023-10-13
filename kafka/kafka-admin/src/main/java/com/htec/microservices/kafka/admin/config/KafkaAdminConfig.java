package com.htec.microservices.kafka.admin.config;

import com.htec.microservices.config.KafkaConfigData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

@Configuration
@EnableRetry
public class KafkaAdminConfig {

    private final KafkaConfigData configurationData;

    KafkaAdminConfig(KafkaConfigData configurationData) {
        this.configurationData = configurationData;
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                configurationData.getBoostrapServers()));
    }
}
