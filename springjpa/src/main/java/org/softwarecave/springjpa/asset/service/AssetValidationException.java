package org.softwarecave.springjpa.asset.service;

import org.softwarecave.springjpa.service.DataValidationException;

public class AssetValidationException extends DataValidationException {
    public AssetValidationException(String message) {
        super(message);
    }
}
