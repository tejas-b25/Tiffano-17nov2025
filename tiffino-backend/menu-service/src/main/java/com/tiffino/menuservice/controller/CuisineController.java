package com.tiffino.menuservice.controller;

import com.tiffino.menuservice.dto.CuisineDTO;
import com.tiffino.menuservice.service.CuisineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cuisines")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class CuisineController {

    private final CuisineService cuisineService;

    @PostMapping("/createCuisine")
    public ResponseEntity<CuisineDTO> create(@Valid @RequestBody CuisineDTO dto) {
        return ResponseEntity.ok(cuisineService.createCuisine(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuisineDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cuisineService.getCuisineById(id));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CuisineDTO>> getAll() {
        return ResponseEntity.ok(cuisineService.getAllCuisines());
    }

    @GetMapping("/state/{stateId}")
    public ResponseEntity<List<CuisineDTO>> getByState(@PathVariable Long stateId) {
        return ResponseEntity.ok(cuisineService.getCuisinesByStateId(stateId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuisineDTO> update(@PathVariable Long id, @RequestBody CuisineDTO dto) {
        return ResponseEntity.ok(cuisineService.updateCuisine(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cuisineService.deleteCuisine(id);
        return ResponseEntity.noContent().build();
    }
}
