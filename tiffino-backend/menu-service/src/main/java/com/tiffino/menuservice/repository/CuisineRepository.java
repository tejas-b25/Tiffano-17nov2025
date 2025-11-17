package com.tiffino.menuservice.repository;

import com.tiffino.menuservice.entity.Cuisine;
import com.tiffino.menuservice.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
    List<Cuisine> findByState(State state);
}
