package com.tiffino.menuservice.service;

import com.tiffino.menuservice.dto.CuisineDTO;
import java.util.List;

public interface CuisineService {
    CuisineDTO createCuisine(CuisineDTO dto);
    CuisineDTO getCuisineById(Long id);
    List<CuisineDTO> getAllCuisines();
    List<CuisineDTO> getCuisinesByStateId(Long stateId);
    CuisineDTO updateCuisine(Long id, CuisineDTO dto);
    void deleteCuisine(Long id);
}
