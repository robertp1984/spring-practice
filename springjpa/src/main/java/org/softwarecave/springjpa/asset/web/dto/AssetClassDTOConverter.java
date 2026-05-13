package org.softwarecave.springjpa.asset.web.dto;

import org.softwarecave.springjpa.asset.model.AssetClass;
import org.softwarecave.springjpa.asset.service.AssetValidationException;
import org.springframework.stereotype.Component;

@Component
public class AssetClassDTOConverter {
    public AssetClassDTO convertToDto(AssetClass asset) {
        if (asset == null) {
            throw new AssetValidationException("Asset must not be null");
        }
        return new AssetClassDTO(asset.getId(), asset.getName(), asset.getDescription());
    }

    public AssetClass toEntity(AssetClassDTO assetClassDTO) {
        if (assetClassDTO == null) {
            throw new AssetValidationException("Asset class must not be null");
        }
        return new AssetClass(assetClassDTO.id(), assetClassDTO.name(), assetClassDTO.description());
    }
}
