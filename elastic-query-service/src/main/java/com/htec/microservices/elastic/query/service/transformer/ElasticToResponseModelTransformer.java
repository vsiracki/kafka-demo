package com.htec.microservices.elastic.query.service.transformer;

import com.htec.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.htec.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel model) {
        return ElasticQueryServiceResponseModel.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .text(model.getText())
                .createdAt(model.getCreatedAt())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
