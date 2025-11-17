package com.tiffino.menuservice.repository;

import com.tiffino.menuservice.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
