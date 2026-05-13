package org.softwarecave.springjpa.asset.converters;

import org.softwarecave.common.avro.AssetAction;
import org.softwarecave.common.avro.AssetEvent;
import org.softwarecave.springjpa.asset.model.Asset;
import org.springframework.stereotype.Component;

@Component
public class AssetAvroConverter {
    public AssetEvent toAssetAvro(Asset savedAsset) {
        return AssetEvent.newBuilder()
                .setAsset(org.softwarecave.common.avro.Asset.newBuilder()
                        .setId(savedAsset.getId())
                        .setName(savedAsset.getName())
                        .setDescription(savedAsset.getDescription())
                        .build())
                .setAction(AssetAction.ADD)
                .build();
    }

    public Asset toAsset(org.softwarecave.common.avro.Asset asset) {
        var result = new Asset(asset.getId(), asset.getName(), asset.getDescription(), null);
        return result;
    }
}
