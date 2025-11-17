package com.tiffino.menuservice.service;

import com.tiffino.menuservice.dto.CityDTO;

import java.util.List;

public interface CityService
{
    CityDTO createCity(CityDTO cityDTO);
    CityDTO getCityById(Long cityId);
    List<CityDTO> getAllCities();
    List<CityDTO> getCitiesByState(Long stateId);
    CityDTO updateCity(Long cityId, CityDTO cityDTO);
    void deleteCity(Long cityId);
}
