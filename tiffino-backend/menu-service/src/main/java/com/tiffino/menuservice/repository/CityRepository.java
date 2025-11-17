package com.tiffino.menuservice.repository;


import com.tiffino.menuservice.entity.City;
import com.tiffino.menuservice.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CityRepository extends JpaRepository<City, Long> {
    // Method 1: Using State entity (recommended - follows your StateRepository pattern)
    List<City> findByState(State state);

    // Method 2: Using State ID directly (if you prefer this approach)
    List<City> findByState_Id(Long stateId);

}
