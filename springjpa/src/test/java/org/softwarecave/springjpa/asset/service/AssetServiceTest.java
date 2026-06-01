package org.softwarecave.springjpa.asset.service;

import antlr.collections.impl.LList;
import org.junit.jupiter.api.Test;
import org.softwarecave.springjpa.asset.messaging.AssetKafkaPublisher;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetClass;
import org.softwarecave.springjpa.asset.model.AssetShortRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
public class AssetServiceTest {

    public static final String EXTERNAL = "External";
    public static final String INTERNAL = "Internal";

    @Autowired
    private AssetService assetService;

    @MockitoBean
    private AssetKafkaPublisher assetKafkaPublisher;

    @Autowired
    private AssetClassService assetClassService;

    @Test
    public void testSaveAndCountMatchingName() {
        createSampleAssets();

        List<Asset> foundAssets = assetService.findByNameOrDescriptionLike("H%");
        assertThat(foundAssets)
                .extracting(Asset::getName)
                .hasSize(2)
                .containsExactlyInAnyOrder("HBO", "HTC");
    }

    @Test
    public void testSaveAndGetByName() {
        createSampleAssets();
        Optional<Asset> foundAsset = assetService.findByName("Disney");
        assertThat(foundAsset.get())
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Disney");
    }

    @Test
    public void testSaveAndGetByAssetClassName_External() {
        createSampleAssets();
        List<Asset> foundAssets = assetService.findByAssetClassName(EXTERNAL);
        assertThat(foundAssets)
                .hasSize(3)
                .extracting(Asset::getName)
                .containsExactlyInAnyOrder("Netflix", "HBO", "HTC");
    }

    @Test
    public void testSaveAndGetByAssetClassName_Internal() {
        createSampleAssets();
        List<Asset> foundAssets = assetService.findByAssetClassName(INTERNAL);
        assertThat(foundAssets)
                .hasSize(2)
                .extracting(Asset::getName)
                .containsExactlyInAnyOrder("Disney", "CircleK");
    }

    @Test
    public void testSaveAndGetAssetShortRefByAssetClassName_existingClassName() {
        // given
        createSampleAssets();

        // when
        var shortRefByAssetClassName = assetService.findShortRefByAssetClassName(EXTERNAL)
                .stream().sorted(Comparator.comparing(AssetShortRef::name)).toList();

        // then
        assertThat(shortRefByAssetClassName).hasSize(3);
        assertThat(shortRefByAssetClassName.get(0))
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "HBO");
        assertThat(shortRefByAssetClassName.get(1))
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "HTC");
        assertThat(shortRefByAssetClassName.get(2))
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "Netflix");
    }

    @Test
    public void testSaveAndGetFiltered_existingName() {
        // given
        createSampleAssets();

        // when
        List<Asset> assets = assetService.findFiltered("HBO", null);

        // then
        assertThat(assets)
                .hasSize(1)
                .anyMatch(a -> "HBO".equals(a.getName()));
    }

    @Test
    public void testSaveAndGetFiltered_missingName() {
        // given
        createSampleAssets();

        // when
        List<Asset> assets = assetService.findFiltered("notexisting", null);

        // then
        assertThat(assets)
                .hasSize(0);
    }

    @Test
    public void testSaveAndGetFiltered_existingAssetClassName() {
        // given
        createSampleAssets();

        // when
        List<Asset> assets = assetService.findFiltered(null, INTERNAL);

        // then
        assertThat(assets)
                .hasSize(2)
                .extracting(Asset::getName)
                .containsExactly("Disney", "CircleK");
    }

    private void createSampleAssets() {
        var externalAssetClass = assetClassService.add(new AssetClass(null, EXTERNAL, "External asset"));
        var internalAssetClass = assetClassService.add(new AssetClass(null, INTERNAL, "Internal asset"));

        assetService.addAsset(new Asset(null, "Netflix", "Netflix shares D", externalAssetClass, List.of()));
        assetService.addAsset(new Asset(null, "HBO", "HBO shares C", externalAssetClass, List.of()));
        assetService.addAsset(new Asset(null, "Disney", "Disney shares A", internalAssetClass, List.of()));
        assetService.addAsset(new Asset(null, "CircleK", "CircleK shares B", internalAssetClass, List.of()));
        assetService.addAsset(new Asset(null, "HTC", "HTC shares A", externalAssetClass, List.of()));
    }
}
