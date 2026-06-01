package org.softwarecave.springjpa.asset.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softwarecave.springjpa.reference.model.AssetReference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asset")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "asset_class_id")
    @NotNull
    private AssetClass assetClass;

    @OneToMany(mappedBy = "asset")
    private List<AssetReference> references = new ArrayList<>();

}
