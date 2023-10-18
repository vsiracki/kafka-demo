package com.htec.microservices.elastic.query.client.exception;

public class ElasticQueryClientException extends RuntimeException {

    public ElasticQueryClientException() {
        super();
    }

    public ElasticQueryClientException(String message) {
        super(message);
    }

    ElasticQueryClientException(String message, Throwable t) {
        super(message, t);

    }
}