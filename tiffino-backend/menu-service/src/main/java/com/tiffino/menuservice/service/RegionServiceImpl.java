package com.tiffino.menuservice.service;

import com.tiffino.menuservice.dto.RegionDTO;
import com.tiffino.menuservice.entity.Region;
import com.tiffino.menuservice.exception.ResourceNotFoundException;
import com.tiffino.menuservice.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public RegionDTO createRegion(RegionDTO dto) {
        Region region = new Region();
        BeanUtils.copyProperties(dto, region);
        Region saved = regionRepository.save(region);
        return toDTO(saved);
    }

    @Override
    public RegionDTO getRegionById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + id));
        return toDTO(region);
    }

    @Override
    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RegionDTO updateRegion(Long id, RegionDTO dto) {
        Region existing = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + id));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        return toDTO(regionRepository.save(existing));
    }

    @Override
    public void deleteRegion(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Region not found with ID: " + id);
        }
        regionRepository.deleteById(id);
    }

    private RegionDTO toDTO(Region entity) {
        RegionDTO dto = new RegionDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
