package org.softwarecave.springjpa.asset.messaging.consumer;

public class IncomingAssetProcessingException extends RuntimeException {
    public IncomingAssetProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
