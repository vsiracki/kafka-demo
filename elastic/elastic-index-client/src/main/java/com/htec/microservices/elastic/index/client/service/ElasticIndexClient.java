package com.htec.microservices.elastic.index.client.service;

import java.util.List;
import com.htec.microservices.elastic.model.index.IndexModel;

public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
