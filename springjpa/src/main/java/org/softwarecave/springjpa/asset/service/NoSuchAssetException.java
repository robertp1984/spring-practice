package org.softwarecave.springjpa.asset.service;

public class NoSuchAssetException extends RuntimeException {
    public NoSuchAssetException(String message) {
        super(message);
    }
}
