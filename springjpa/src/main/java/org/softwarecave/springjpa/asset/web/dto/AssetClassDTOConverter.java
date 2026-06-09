package org.softwarecave.springjpa.asset.web.dto;

import io.micrometer.common.util.StringUtils;
import org.softwarecave.springjpa.asset.model.AssetClass;
import org.softwarecave.springjpa.asset.service.AssetValidationException;
import org.softwarecave.springjpa.reference.service.AssetReferenceValidationException;
import org.springframework.stereotype.Component;

@Component
public class AssetClassDTOConverter {
    public AssetClassDTO convertToDto(AssetClass asset) {
        if (asset == null) {
            throw new AssetValidationException("Asset must not be null");
        }
        if (StringUtils.isBlank(asset.getName())) {
            throw new AssetValidationException("Name of the asset class must not be blank");
        }
        if (StringUtils.isBlank(asset.getDescription())) {
            throw new AssetValidationException("Description of the asset class must not be blank");
        }

        return new AssetClassDTO(asset.getId(), asset.getName(), asset.getDescription());
    }

    public AssetClass toEntity(AssetClassDTO assetClassDTO) {
        if (assetClassDTO == null) {
            throw new AssetValidationException("Asset class must not be null");
        }
        if (StringUtils.isBlank(assetClassDTO.name())) {
            throw new AssetValidationException("Name of the asset class must not be blank");
        }
        if (StringUtils.isBlank(assetClassDTO.description())) {
            throw new AssetValidationException("Description of the asset class must not be blank");
        }

        return new AssetClass(assetClassDTO.id(), assetClassDTO.name(), assetClassDTO.description());
    }
}
