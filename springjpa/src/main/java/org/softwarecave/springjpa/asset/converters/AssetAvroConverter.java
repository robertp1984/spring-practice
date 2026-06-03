package org.softwarecave.springjpa.asset.converters;

import org.softwarecave.common.avro.AssetAction;
import org.softwarecave.common.avro.AssetEvent;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.reference.model.AssetReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssetAvroConverter {
    public AssetEvent toAssetAvro(Asset savedAsset) {
        var references = savedAsset.getReferences().stream()
                .map(r -> org.softwarecave.common.avro.AssetReference.newBuilder()
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
        var result = new Asset(asset.getId(), asset.getName(), asset.getDescription(), null, new ArrayList<>());

        var references = asset.getReferences().stream()
                .map(r -> new AssetReference(r.getId(), r.getName(), r.getValue(), result))
                .collect(Collectors.toList());
        result.setReferences(references);

        return result;
    }
}
