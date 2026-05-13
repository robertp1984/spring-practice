package org.softwarecave.springjpa.asset.service;

public class NoSuchAssetClassException extends RuntimeException {
    public NoSuchAssetClassException(String message) {
        super(message);
    }
}
