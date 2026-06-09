package org.softwarecave.springjpa.asset.web.dto;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetClass;
import org.softwarecave.springjpa.asset.service.AssetValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AssetDTOConverter {

    private final AssetClassDTOConverter assetClassDTOConverter;
    private final AssetReferenceDTOConverter assetReferenceDTOConverter;

    public AssetDTO convertToDto(Asset asset) {
        var assetReferencesDTO = asset.getReferences().stream()
                .map(r -> new AssetReferenceDTO(r.getId(), r.getName(), r.getValueString()))
                .toList();
        return new AssetDTO(asset.getId(), asset.getName(), asset.getDescription(),
                assetClassDTOConverter.convertToDto(asset.getAssetClass()), assetReferencesDTO);
    }

    public Asset convertToEntity(AssetDTO assetDTO) {
        if (assetDTO == null) {
            throw new AssetValidationException("AssetDTO must not be null");
        }
        if (StringUtils.isBlank(assetDTO.name())) {
            throw new AssetValidationException("Name of the asset must not be null");
        }
        if (StringUtils.isBlank(assetDTO.description())) {
            throw new AssetValidationException("Description of the asset must not be null");
        }

        AssetClassDTO assetClassDTO = assetDTO.assetClass();
        AssetClass assetClass = assetClassDTO != null ? assetClassDTOConverter.toEntity(assetClassDTO) : null;

        var asset = new Asset(assetDTO.id(), assetDTO.name(), assetDTO.description(), assetClass, List.of());

        var assetReferencesDTO = assetDTO.references();
        var assetReferences = assetReferencesDTO.stream()
                .map(r -> assetReferenceDTOConverter.toEntity(r, asset))
                .toList();
        asset.setReferences(assetReferences);

        return asset;
    }
}
