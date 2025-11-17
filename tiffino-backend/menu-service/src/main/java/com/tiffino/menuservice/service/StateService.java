package com.tiffino.menuservice.service;

import com.tiffino.menuservice.dto.StateDTO;
import java.util.List;

public interface StateService {
    StateDTO createState(StateDTO dto);
    StateDTO getStateById(Long id);
    List<StateDTO> getAllStates();
    StateDTO updateState(Long id, StateDTO dto);
    void deleteState(Long id);
    List<StateDTO> getStatesByRegionId(Long regionId);
}
