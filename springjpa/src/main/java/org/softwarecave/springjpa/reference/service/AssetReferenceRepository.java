package org.softwarecave.springjpa.reference.service;

import org.softwarecave.springjpa.reference.model.AssetReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssetReferenceRepository extends JpaRepository<AssetReference, UUID> {

    Optional<AssetReference> findTopByNameAndValueString(String name, String valueString);
}
