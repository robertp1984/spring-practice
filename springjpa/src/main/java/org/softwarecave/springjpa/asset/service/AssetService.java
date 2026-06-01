package org.softwarecave.springjpa.asset.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.softwarecave.springjpa.asset.messaging.AssetKafkaPublisher;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetShortRef;
import org.softwarecave.springjpa.reference.model.AssetReference;
import org.softwarecave.springjpa.reference.service.AssetReferenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetReferenceService assetReferenceService;

    private final AssetKafkaPublisher assetKafkaPublisher;

    public Asset addAsset(Asset asset) {
        validateNewAsset(asset);
        var savedAsset = assetRepository.save(asset);
        assetKafkaPublisher.sendAdded(savedAsset);
        return savedAsset;
    }

    public Asset addAssetWithReferences(Asset asset) {
        validateNewAsset(asset);
        var savedAsset = assetRepository.save(asset);

        List<AssetReference> savedAssetReferences = assetReferenceService.saveAll(asset.getReferences());
        savedAsset.setReferences(savedAssetReferences);

        assetKafkaPublisher.sendAdded(savedAsset);
        return savedAsset;
    }

    @Transactional(readOnly = true)
    public Asset findById(String id) {
        validateAssetId(id);
        return assetRepository.findById(id)
                .orElseThrow(() -> new NoSuchAssetException("Asset with id %s was not found".formatted(id)));
    }

    private void validateAssetId(String id) {
        if (id == null) {
            throw new AssetValidationException("Asset id must not be null");
        }
    }

    @Transactional(readOnly = true)
    public List<Asset> findByNameOrDescriptionLike(String likeExpr) {
        return assetRepository.findByNameLikeOrDescriptionLike(likeExpr, likeExpr);
    }

    @Transactional(readOnly = true)
    public Optional<Asset> findByName(String name) {
        validateAssetName(name);
        return assetRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Asset> findByAssetClassName(String assetClassName) {
        validateAssetClassName(assetClassName);
        return assetRepository.findByAssetClassName(assetClassName);
    }

    @Transactional(readOnly = true)
    public List<AssetShortRef> findShortRefByAssetClassName(String assetClassName) {
        validateAssetClassName(assetClassName);
        return assetRepository.findShortRefByAssetClassName(assetClassName);
    }

    private void validateAssetName(String name) {
        if (name == null) {
            throw new AssetValidationException("Asset name must not be null");
        }
    }

    private static void validateAssetClassName(String assetClassName) {
        if (assetClassName == null) {
            throw new AssetValidationException("Asset class name must not be null");
        }
    }

    private static void validateNewAsset(Asset asset) {
        if (asset.getId() != null) {
            throw new AssetValidationException("Asset being added must have null key");
        }
    }

    @Transactional(readOnly = true)
    public List<Asset> findFiltered(String name, String assetClassName) {
        if (!StringUtils.isBlank(name)) {
            return findByName(name).stream().toList();
        } else if (!StringUtils.isBlank(assetClassName)) {
            return findByAssetClassName(assetClassName);
        } else {
            return assetRepository.findAll();
        }
    }
}
