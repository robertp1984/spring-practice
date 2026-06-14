package org.softwarecave.springjpa.asset.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AssetReferenceDTO(UUID id,
                                @NotBlank String name,
                                @NotBlank String valueString) {
}
