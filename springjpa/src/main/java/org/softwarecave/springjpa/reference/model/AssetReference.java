package org.softwarecave.springjpa.reference.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwarecave.springjpa.asset.model.Asset;

import java.util.UUID;

@Entity
@DiscriminatorValue("assetref")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetReference extends Reference {

    @ManyToOne
    @JoinColumn(name = "asset_id")
    @NotNull
    private Asset asset;

    public AssetReference(UUID id, String name, String valueString, Asset asset) {
        super(id, name, valueString);
        this.asset = asset;
    }

}
