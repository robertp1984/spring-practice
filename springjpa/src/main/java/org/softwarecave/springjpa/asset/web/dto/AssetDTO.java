package org.softwarecave.springjpa.asset.web.dto;

import java.util.List;

public record AssetDTO(String id, String name, String description, AssetClassDTO assetClass,
                       List<AssetReferenceDTO> references) {
}
