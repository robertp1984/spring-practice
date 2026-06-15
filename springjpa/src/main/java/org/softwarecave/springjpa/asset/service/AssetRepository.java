package org.softwarecave.springjpa.asset.service;

import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetShortRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID>,
        JpaSpecificationExecutor<Asset> {
    Page<Asset> findByNameLikeOrDescriptionLike(String nameLikeExpr, String descriptionLikeExpr, Pageable pageable);

    Optional<Asset> findByName(String name);

    Page<Asset> findByAssetClassName(String assetClassName, Pageable pageable);

    Page<AssetShortRef> findShortRefByAssetClassName(String assetClassName, Pageable pageable);
}
