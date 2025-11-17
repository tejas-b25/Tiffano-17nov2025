package com.tiffino.menuservice.service;

import com.tiffino.menuservice.repository.CityRepository;
import com.tiffino.menuservice.repository.StateRepository;
import com.tiffino.menuservice.dto.CityDTO;
import com.tiffino.menuservice.entity.City;
import com.tiffino.menuservice.entity.State;
import com.tiffino.menuservice.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService
{
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;

    @Override
    public CityDTO createCity(CityDTO cityDTO) {
        State state = stateRepository.findById(cityDTO.getStateId())
                .orElseThrow(() -> new RuntimeException("State not found"));

        City city = City.builder()
                .cityName(cityDTO.getCityName())
                .state(state)
                .build();

        City saved = cityRepository.save(city);

        return CityDTO.builder()
                .cityId(saved.getCityId())
                .cityName(saved.getCityName())
                .stateId(saved.getState().getId())
                .build();
    }

    @Override
    public CityDTO getCityById(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found"));

        return CityDTO.builder()
                .cityId(city.getCityId())
                .cityName(city.getCityName())
                .stateId(city.getState().getId())
                .build();
    }

    @Override
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(city -> CityDTO.builder()
                        .cityId(city.getCityId())
                        .cityName(city.getCityName())
                        .stateId(city.getState().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CityDTO> getCitiesByState(Long stateId) {
        // Option 1: Using State ID directly (recommended for this case)
        return cityRepository.findByState_Id(stateId).stream()
                .map(city -> CityDTO.builder()
                        .cityId(city.getCityId())
                        .cityName(city.getCityName())
                        .stateId(city.getState().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CityDTO updateCity(Long cityId, CityDTO cityDTO) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found"));

        State state = stateRepository.findById(cityDTO.getStateId())
                .orElseThrow(() -> new RuntimeException("State not found"));

        city.setCityName(cityDTO.getCityName());
        city.setState(state);

        City updated = cityRepository.save(city);

        return CityDTO.builder()
                .cityId(updated.getCityId())
                .cityName(updated.getCityName())
                .stateId(updated.getState().getId())
                .build();
    }

    @Override
    public void deleteCity(Long cityId) {
        if (!cityRepository.existsById(cityId)) {
            throw new RuntimeException("City not found");
        }
        cityRepository.deleteById(cityId);
    }

}