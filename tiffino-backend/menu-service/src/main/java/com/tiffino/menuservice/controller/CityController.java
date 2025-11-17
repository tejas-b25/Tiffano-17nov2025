package com.tiffino.menuservice.controller;

import com.tiffino.menuservice.dto.CityDTO;
import com.tiffino.menuservice.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController
{
    private final CityService cityService;

    @PostMapping("/addCity")
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) {
        return ResponseEntity.ok(cityService.createCity(cityDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CityDTO>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/state/{stateId}")
    public ResponseEntity<List<CityDTO>> getCitiesByState(@PathVariable Long stateId) {
        return ResponseEntity.ok(cityService.getCitiesByState(stateId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable Long id, @RequestBody CityDTO cityDTO) {
        return ResponseEntity.ok(cityService.updateCity(id, cityDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
