package com.htec.microservices.elastic.query.service.business.impl;

import com.htec.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.htec.microservices.elastic.query.client.service.ElasticQueryClient;
import com.htec.microservices.elastic.query.service.business.ElasticQueryService;
import com.htec.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.htec.microservices.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private final ElasticToResponseModelTransformer transformer;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;

    TwitterElasticQueryService(ElasticToResponseModelTransformer transformer,
                               ElasticQueryClient<TwitterIndexModel> elasticQueryClient) {
        this.transformer = transformer;
        this.elasticQueryClient = elasticQueryClient;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        log.info("Querying elasticsearch by id: {}", id);
        return transformer.getResponseModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        log.info("Querying elasticsearch by text: {}", text);
        return transformer.getResponseModels(elasticQueryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> responseModels = transformer.getResponseModels(elasticQueryClient.getAllIndexModels());
        log.info("Fetched elasticsearch documents: {}", responseModels);
        return responseModels;
    }
}
