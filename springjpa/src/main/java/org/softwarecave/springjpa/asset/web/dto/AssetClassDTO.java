package org.softwarecave.springjpa.asset.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AssetClassDTO(String id,
                            @NotBlank String name,
                            @NotBlank String description) {
}
