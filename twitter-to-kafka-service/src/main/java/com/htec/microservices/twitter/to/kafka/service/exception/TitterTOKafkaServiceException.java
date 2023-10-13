package com.htec.microservices.twitter.to.kafka.service.exception;

public class TitterTOKafkaServiceException extends RuntimeException {

    public TitterTOKafkaServiceException() {
        super();
    }
    public TitterTOKafkaServiceException(String message) {
        super(message);
    }

    public TitterTOKafkaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
