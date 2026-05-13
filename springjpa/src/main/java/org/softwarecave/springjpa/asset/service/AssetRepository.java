package org.softwarecave.springjpa.asset.service;

import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetShortRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, String> {
    List<Asset> findByNameLikeOrDescriptionLike(String nameLikeExpr, String descriptionLikeExpr);

    Optional<Asset> findByName(String name);

    List<Asset> findByAssetClassName(String assetClassName);

    List<AssetShortRef> findShortRefByAssetClassName(String assetClassName);
}
