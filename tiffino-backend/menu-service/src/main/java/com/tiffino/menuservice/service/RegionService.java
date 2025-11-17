package com.tiffino.menuservice.service;

import com.tiffino.menuservice.dto.RegionDTO;
import java.util.List;

public interface RegionService {
    RegionDTO createRegion(RegionDTO dto);
    RegionDTO getRegionById(Long id);
    List<RegionDTO> getAllRegions();
    RegionDTO updateRegion(Long id, RegionDTO dto);
    void deleteRegion(Long id);
}
