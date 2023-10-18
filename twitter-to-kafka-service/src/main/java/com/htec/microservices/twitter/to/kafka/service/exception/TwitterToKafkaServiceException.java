package com.htec.microservices.twitter.to.kafka.service.exception;

public class TwitterTOKafkaServiceException extends RuntimeException {

    public TwitterTOKafkaServiceException() {
        super();
    }
    public TwitterTOKafkaServiceException(String message) {
        super(message);
    }

    public TwitterTOKafkaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
