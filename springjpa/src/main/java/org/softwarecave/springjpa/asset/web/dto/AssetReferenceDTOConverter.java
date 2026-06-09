package org.softwarecave.springjpa.asset.web.dto;

import io.micrometer.common.util.StringUtils;
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
        if (StringUtils.isBlank(dto.name())) {
            throw new AssetReferenceValidationException("Name of the asset reference must not be blank");
        }
        if (StringUtils.isBlank(dto.valueString())) {
            throw new AssetReferenceValidationException("Value of the asset reference must not be blank");
        }

        return new AssetReference(dto.id(), dto.name(), dto.valueString(), asset);
    }
}
