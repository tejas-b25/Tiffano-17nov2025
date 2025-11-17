package com.tiffino.menuservice.service;

import com.tiffino.menuservice.dto.CuisineDTO;
import com.tiffino.menuservice.entity.Cuisine;
import com.tiffino.menuservice.entity.State;
import com.tiffino.menuservice.exception.ResourceNotFoundException;
import com.tiffino.menuservice.repository.CuisineRepository;
import com.tiffino.menuservice.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;
    private final StateRepository stateRepository;

    @Override
    public CuisineDTO createCuisine(CuisineDTO dto) {
        Cuisine cuisine = new Cuisine();
        BeanUtils.copyProperties(dto, cuisine);
        cuisine.setState(getState(dto.getStateId()));
        return toDTO(cuisineRepository.save(cuisine));
    }

    @Override
    public CuisineDTO getCuisineById(Long id) {
        return toDTO(findCuisine(id));
    }

    @Override
    public List<CuisineDTO> getAllCuisines() {
        return cuisineRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CuisineDTO> getCuisinesByStateId(Long stateId) {
        State state = getState(stateId);
        return cuisineRepository.findByState(state).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CuisineDTO updateCuisine(Long id, CuisineDTO dto) {
        Cuisine cuisine = findCuisine(id);
        cuisine.setName(dto.getName());
        cuisine.setImageUrl(dto.getImageUrl());
        cuisine.setState(getState(dto.getStateId()));
        return toDTO(cuisineRepository.save(cuisine));
    }

    @Override
    public void deleteCuisine(Long id) {
        cuisineRepository.deleteById(id);
    }

    private Cuisine findCuisine(Long id) {
        return cuisineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuisine not found: " + id));
    }

    private State getState(Long id) {
        return stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State not found: " + id));
    }

    private CuisineDTO toDTO(Cuisine entity) {
        CuisineDTO dto = new CuisineDTO();
        // Copy all matching properties automatically
        org.springframework.beans.BeanUtils.copyProperties(entity, dto);

        // Manually set stateId because DTO has Long stateId, entity has State object
        dto.setStateId(entity.getState() != null ? entity.getState().getId() : null);

        return dto;
    }


}
