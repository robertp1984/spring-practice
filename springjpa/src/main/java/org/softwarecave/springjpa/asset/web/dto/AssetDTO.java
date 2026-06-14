package org.softwarecave.springjpa.asset.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record AssetDTO(UUID id,
                       @NotBlank String name,
                       @NotBlank String description,
                       AssetClassDTO assetClass,
                       List<AssetReferenceDTO> references) {
}
