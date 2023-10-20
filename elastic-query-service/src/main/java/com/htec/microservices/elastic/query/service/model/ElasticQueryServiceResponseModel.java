package com.htec.microservices.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticQueryServiceResponseModel {
    private String id;
    private Long userId;
    private String text;
    private ZonedDateTime createdAt;
}
