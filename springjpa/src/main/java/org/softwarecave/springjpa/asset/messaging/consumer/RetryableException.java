package org.softwarecave.springjpa.asset.messaging.consumer;

public class RetryableException extends RuntimeException{
    public RetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryableException(String message) {
        super(message);
    }
}
