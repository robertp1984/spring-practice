package org.softwarecave.springjpa.asset.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.service.AssetService;
import org.softwarecave.springjpa.asset.web.dto.AssetDTO;
import org.softwarecave.springjpa.asset.web.dto.AssetDTOConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final AssetDTOConverter assetDTOConverter;

    @GetMapping
    public List<AssetDTO> getAssets(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "assetClassName", required = false) String assetClassName) {
        List<Asset> assets = assetService.findFiltered(name, assetClassName);
        return assets.stream().map(assetDTOConverter::convertToDto).toList();
    }

    @PostMapping
    public ResponseEntity<String> addAsset(@RequestBody @Valid AssetDTO assetDTO) {
        var asset = assetDTOConverter.convertToEntity(assetDTO);

        var savedAsset = assetService.addAsset(asset);

        var uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(asset.getId()).toUri();
        return ResponseEntity.created(uri).body("");
    }
}
