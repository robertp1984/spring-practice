package org.softwarecave.springjpa.asset.web.dto;

import lombok.RequiredArgsConstructor;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetClass;
import org.softwarecave.springjpa.asset.service.AssetValidationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssetDTOConverter {

    private final AssetClassDTOConverter assetClassDTOConverter;

    public AssetDTO convertToDto(Asset asset) {
        return new AssetDTO(asset.getId(), asset.getName(), asset.getDescription(),
                assetClassDTOConverter.convertToDto(asset.getAssetClass()));
    }

    public Asset convertToEntity(AssetDTO assetDTO) {
        if (assetDTO == null) {
            throw new AssetValidationException("AssetDTO must not be null");
        }
        AssetClassDTO assetClassDTO = assetDTO.assetClass();
        AssetClass assetClass = assetClassDTO != null ? assetClassDTOConverter.toEntity(assetClassDTO) : null;

        return new Asset(assetDTO.id(), assetDTO.name(), assetDTO.description(), assetClass);
    }
}
