package org.softwarecave.springjpa.asset.service;

import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetShortRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findByNameLikeOrDescriptionLike(String nameLikeExpr, String descriptionLikeExpr);

    Optional<Asset> findByName(String name);

    List<Asset> findByAssetClassName(String assetClassName);

    List<AssetShortRef> findShortRefByAssetClassName(String assetClassName);
}
