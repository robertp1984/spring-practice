package org.softwarecave.springjpa.asset.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.service.AssetService;
import org.softwarecave.springjpa.asset.web.dto.AssetDTO;
import org.softwarecave.springjpa.asset.web.dto.AssetDTOConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final AssetDTOConverter assetDTOConverter;

    @GetMapping
    public Page<AssetDTO> getAssets(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "assetClassName", required = false) String assetClassName,
                                    Pageable pageable) {
        Page<Asset> page = assetService.findFiltered(name, assetClassName, pageable);
        return page.map(assetDTOConverter::convertToDto);
    }

    @PostMapping
    public ResponseEntity<String> addAsset(@RequestBody @Valid AssetDTO assetDTO) {
        var asset = assetDTOConverter.convertToEntity(assetDTO);

        var savedAsset = assetService.addAsset(asset);

        var uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(savedAsset.getId()).toUri();
        return ResponseEntity.created(uri).body("");
    }
}
