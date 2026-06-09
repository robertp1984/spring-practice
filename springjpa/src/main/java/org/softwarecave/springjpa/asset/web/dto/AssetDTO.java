package org.softwarecave.springjpa.asset.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record AssetDTO(String id,
                       @NotBlank String name,
                       @NotBlank String description,
                       AssetClassDTO assetClass,
                       List<AssetReferenceDTO> references) {
}
