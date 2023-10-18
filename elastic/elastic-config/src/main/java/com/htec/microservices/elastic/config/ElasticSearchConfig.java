package com.htec.microservices.elastic.config;

import com.htec.microservices.config.ElasticConfigData;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.htec.microservices.elastic")
public class ElasticSearchConfig extends /* AbstractElasticsearchConfiguration*/ ElasticsearchConfiguration {

    private final ElasticConfigData elasticConfigData;

    ElasticSearchConfig(ElasticConfigData configData) {
        this.elasticConfigData = configData;
    }

//    @Override
//    @Bean
//    public RestHighLevelClient elasticsearchClient() {
//        UriComponents serverUri = UriComponentsBuilder.fromHttpUrl(elasticConfigData.getConnectionUrl()).build();
//        return new RestHighLevelClient(
//                RestClient.builder(new HttpHost(
//                        serverUri.getHost(),
//                        serverUri.getPort(),
//                        serverUri.getScheme()
//                )).setRequestConfigCallback(
//                        requestConfigBuilder ->
//                                requestConfigBuilder
//                                        .setConnectTimeout(elasticConfigData.getConnectTimeoutMs())
//                                        .setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
//
//                )
//        );
//    }

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .withConnectTimeout(elasticConfigData.getConnectTimeoutMs())
                .withSocketTimeout(elasticConfigData.getSocketTimeoutMs())
                .build();
    }
}
