package org.softwarecave.springjpa.asset.converters;

import org.softwarecave.common.avro.AssetAction;
import org.softwarecave.common.avro.AssetEvent;
import org.softwarecave.common.avro.AssetReference;
import org.softwarecave.springjpa.asset.model.Asset;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssetAvroConverter {
    public AssetEvent toAssetAvro(Asset savedAsset) {
        List<AssetReference> references = savedAsset.getReferences().stream()
                .map(r -> AssetReference.newBuilder()
                        .setId(r.getId())
                        .setName(r.getName())
                        .setValue(r.getValueString())
                        .build())
                .toList();

        return AssetEvent.newBuilder()
                .setAsset(org.softwarecave.common.avro.Asset.newBuilder()
                        .setId(savedAsset.getId())
                        .setName(savedAsset.getName())
                        .setDescription(savedAsset.getDescription())
                        .setReferences(references)
                        .build())
                .setAction(AssetAction.ADD)
                .build();
    }

    public Asset toAsset(org.softwarecave.common.avro.Asset asset) {
//TODO:
        var result = new Asset(asset.getId(), asset.getName(), asset.getDescription(), null, List.of());
        return result;
    }
}
