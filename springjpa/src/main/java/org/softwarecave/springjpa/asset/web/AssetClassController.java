package org.softwarecave.springjpa.asset.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.softwarecave.springjpa.asset.service.AssetClassService;
import org.softwarecave.springjpa.asset.web.dto.AssetClassDTO;
import org.softwarecave.springjpa.asset.web.dto.AssetClassDTOConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/assetClasses")

public class AssetClassController {

    private final AssetClassService assetClassService;
    private final AssetClassDTOConverter assetClassDTOConverter;

    @PostMapping
    @Operation(summary = "Adds new asset class", description = "Adds new asset class based on the request body",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Body of  the asset class with null id", required = true),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully added new asset")
            })
    public ResponseEntity<String> addAssetClass(@RequestBody @Valid AssetClassDTO assetClassDTO) {
        var assetClass = assetClassDTOConverter.toEntity(assetClassDTO);

        var savedAssetClass = assetClassService.add(assetClass);

        var uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(savedAssetClass.getId()).toUri();
        return ResponseEntity.created(uri).body("");
    }
}

