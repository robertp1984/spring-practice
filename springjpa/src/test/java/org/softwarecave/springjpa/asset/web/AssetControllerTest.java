package org.softwarecave.springjpa.asset.web;

import org.junit.jupiter.api.Test;
import org.softwarecave.springjpa.asset.model.Asset;
import org.softwarecave.springjpa.asset.model.AssetClass;
import org.softwarecave.springjpa.asset.service.AssetService;
import org.softwarecave.springjpa.asset.web.dto.AssetClassDTOConverter;
import org.softwarecave.springjpa.asset.web.dto.AssetDTO;
import org.softwarecave.springjpa.asset.web.dto.AssetDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssetController.class)
public class AssetControllerTest {

    private static final String EXTERNAL = "ext";

    @MockitoBean
    private AssetService assetService;

    @MockitoBean
    private AssetDTOConverter assetDTOConverter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAssetsByFilter_name() throws Exception {
        // given
        var assetClasses = createAssetClasses();
        var assets = createAssets(assetClasses);
        when(assetService.findFiltered(anyString(), isNull())).thenReturn(List.of(assets.getFirst()));
        when(assetDTOConverter.convertToDto(any())).thenReturn(convertEntityToDTO(assets.getFirst()));

        // when
        mockMvc.perform(get("/api/v1/assets")
                        .queryParam("name", "Google"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("id1"))
                .andExpect(jsonPath("$[0].name").value("Google"))
                .andExpect(jsonPath("$[0].description").value("Google shares"));

        verify(assetService).findFiltered(anyString(), isNull());
    }

    private AssetDTO convertEntityToDTO(Asset asset) {
        AssetDTOConverter converter = new AssetDTOConverter(new AssetClassDTOConverter());
        return converter.convertToDto(asset);
    }

    @Test
    public void testGetAssetsByFilter_assetClassName() throws Exception {
        // given
        var assetClasses = createAssetClasses();
        var assets = createAssets(assetClasses);
        when(assetService.findFiltered(isNull(), anyString())).thenReturn(List.of(assets.getLast()));
        when(assetDTOConverter.convertToDto(any())).thenReturn(convertEntityToDTO(assets.getLast()));

        // when
        mockMvc.perform(get("/api/v1/assets")
                        .queryParam("assetClassName", "Microsoft"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("id2"))
                .andExpect(jsonPath("$[0].name").value("Microsoft"))
                .andExpect(jsonPath("$[0].description").value("Microsoft bonds"))
                .andExpect(jsonPath("$[0].assetClass.name").value(EXTERNAL));

        verify(assetService).findFiltered(isNull(), anyString());
    }

    private static List<Asset> createAssets(List<AssetClass> assetClasses) {
        return List.of(new Asset("id1", "Google", "Google shares", assetClasses.getFirst(), List.of()),
                new Asset("id2", "Microsoft", "Microsoft bonds", assetClasses.getFirst(), List.of()));
    }

    private static List<AssetClass> createAssetClasses() {
        return List.of(new AssetClass("id3", EXTERNAL, "desc"));
    }
}
