package com.tiffino.menuservice.repository;

import com.tiffino.menuservice.entity.State;
import com.tiffino.menuservice.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StateRepository extends JpaRepository<State, Long> {
    List<State> findByRegion(Region region);
    Optional<State> findByName(String name);
}
