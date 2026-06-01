package org.softwarecave.springjpa.asset.messaging.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.softwarecave.common.avro.AssetEvent;
import org.softwarecave.springjpa.asset.converters.AssetAvroConverter;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetClass;
import org.softwarecave.springjpa.asset.service.AssetClassService;
import org.softwarecave.springjpa.asset.service.AssetService;
import org.softwarecave.springjpa.asset.service.AssetValidationException;
import org.softwarecave.springjpa.reference.model.AssetReference;
import org.softwarecave.springjpa.reference.service.AssetReferenceRepository;
import org.softwarecave.springjpa.reference.service.AssetReferenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IncomingAssetProcessor {
    public static final String INCOMING = "Incoming";
    private final AssetAvroConverter assetAvroConverter;
    private final AssetService assetService;
    private final AssetClassService assetClassService;
    private final AssetReferenceService assetReferenceService;

    @Transactional
    public void handleIncomingAsset(AssetEvent event, String messageId) {
        validate(event);

        Optional<AssetReference> existingAssetReference = assetReferenceService.findByNameAndValueString("IncomingMessageId", messageId);
        if (existingAssetReference.isPresent()) {
            log.info("Duplicated message found by messageId={}. Skipping the processing.", messageId);
            return;
        }

        Asset asset = assetAvroConverter.toAsset(event.getAsset());
        asset.setId(null);

        AssetClass assetClass = getAssetClass(asset);
        asset.setAssetClass(assetClass);

        log.debug("Saving new asset {} into database", asset);
        assetService.addAsset(asset);
        log.debug("Saved new asset {} into database", asset);
    }

    private AssetClass getAssetClass(Asset asset) {
        Optional<AssetClass> assetClass = assetClassService.findByNameOptional(INCOMING);
        return assetClass.orElseGet(() -> assetClassService.add(new AssetClass(null, INCOMING, INCOMING)));
    }

    private static void validate(AssetEvent event) {
        if (event == null) {
            throw new AssetValidationException("Asset is null in incoming asset");
        }

        var asset = event.getAsset();
        if (asset.getName() == null || asset.getDescription() == null) {
            throw new AssetValidationException("Name and description must not be null");
        }
    }
}
