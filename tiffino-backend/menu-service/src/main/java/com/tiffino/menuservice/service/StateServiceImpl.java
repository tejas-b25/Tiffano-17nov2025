package com.tiffino.menuservice.service;

import com.tiffino.menuservice.dto.StateDTO;
import com.tiffino.menuservice.entity.Region;
import com.tiffino.menuservice.entity.State;
import com.tiffino.menuservice.exception.ResourceNotFoundException;
import com.tiffino.menuservice.repository.RegionRepository;
import com.tiffino.menuservice.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final RegionRepository regionRepository;

    @Override
    public StateDTO createState(StateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("StateDTO cannot be null");

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + dto.getRegionId()));

        State state = new State();
        state.setName(dto.getName());
        state.setRegion(region);
        State saved = stateRepository.save(state);
        return toDTO(saved);
    }

    @Override
    public StateDTO getStateById(Long id) {
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State not found with ID: " + id));
        return toDTO(state);
    }

    @Override
    public List<StateDTO> getAllStates() {
        return stateRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StateDTO updateState(Long id, StateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("StateDTO cannot be null");

        State existing = stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State not found with ID: " + id));

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + dto.getRegionId()));

        existing.setName(dto.getName());
        existing.setRegion(region);

        State updated = stateRepository.save(existing);
        return toDTO(updated);
    }

    @Override
    public void deleteState(Long id) {
        if (!stateRepository.existsById(id)) {
            throw new ResourceNotFoundException("State not found with ID: " + id);
        }
        stateRepository.deleteById(id);
    }

    @Override
    public List<StateDTO> getStatesByRegionId(Long regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + regionId));

        List<State> states = stateRepository.findByRegion(region);
        return states.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private StateDTO toDTO(State entity) {
        StateDTO dto = new StateDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setRegionId(entity.getRegion() != null ? entity.getRegion().getId() : null);
        return dto;
    }
}
