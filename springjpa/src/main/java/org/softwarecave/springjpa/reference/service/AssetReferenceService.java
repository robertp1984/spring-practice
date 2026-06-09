package org.softwarecave.springjpa.reference.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.softwarecave.springjpa.reference.model.AssetReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(value = "transactionManager")
@RequiredArgsConstructor
public class AssetReferenceService {

    private final AssetReferenceRepository assetReferenceRepository;

    public List<AssetReference> saveAll(List<AssetReference> references) {
        validateReferences(references);

        return assetReferenceRepository.saveAll(references);
    }

    private void validateReferences(List<AssetReference> references) {
        for (var ref : references) {
            if (ref.getId() != null) {
                throw new AssetReferenceValidationException("Asset reference id must be null");
            }
            if (StringUtils.isBlank(ref.getName())) {
                throw new AssetReferenceValidationException("Asset reference name must not be null");
            }
            if (StringUtils.isBlank(ref.getValueString())) {
                throw new AssetReferenceValidationException("Asset reference value must not be null");
            }
        }
    }

    public Optional<AssetReference> findByNameAndValueString(String name, String valueString) {
        return assetReferenceRepository.findTopByNameAndValueString(name, valueString);
    }
}
