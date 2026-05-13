package org.softwarecave.springjpa.asset.web.dto;

public record AssetDTO(String id, String name, String description, AssetClassDTO assetClass) {
}
