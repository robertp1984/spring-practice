package org.softwarecave.springjpa.reference.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwarecave.springjpa.asset.model.Asset;

@Entity
@DiscriminatorValue("assetref")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetReference extends Reference {

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    public AssetReference(String id, String name, String valueString, Asset asset) {
        super(id, name, valueString);
        this.asset = asset;
    }

}
