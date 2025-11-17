package com.tiffino.menuservice.controller;

import com.tiffino.menuservice.dto.RegionDTO;
import com.tiffino.menuservice.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/getall")
    public ResponseEntity<List<RegionDTO>> getAll() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }
}
