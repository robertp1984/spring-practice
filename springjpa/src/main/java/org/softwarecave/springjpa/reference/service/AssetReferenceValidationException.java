package org.softwarecave.springjpa.reference.service;

import org.softwarecave.springjpa.service.DataValidationException;

public class AssetReferenceValidationException extends DataValidationException {
    public AssetReferenceValidationException(String message) {
        super(message);
    }
}
