package org.softwarecave.springjpa.asset.messaging.consumer;

public class IncomingAssetValidationException extends RuntimeException {
    public IncomingAssetValidationException(String message) {
        super(message);
    }

    public IncomingAssetValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
