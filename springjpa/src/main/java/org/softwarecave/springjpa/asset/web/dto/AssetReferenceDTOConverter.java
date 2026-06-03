package org.softwarecave.springjpa.asset.web.dto;

import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.reference.model.AssetReference;
import org.softwarecave.springjpa.reference.service.AssetReferenceValidationException;
import org.springframework.stereotype.Service;

@Service
public class AssetReferenceDTOConverter {

    public AssetReference toEntity(AssetReferenceDTO dto, Asset asset) {
        if (dto == null) {
            throw new AssetReferenceValidationException("Asset reference must not be null");
        }

        return new AssetReference(dto.id(), dto.name(), dto.valueString(), asset);
    }
}
