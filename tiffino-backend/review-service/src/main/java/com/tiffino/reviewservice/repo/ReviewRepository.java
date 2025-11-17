package com.tiffino.reviewservice.repo;

import com.tiffino.reviewservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;       // ✅ import List
import java.util.Optional;   // ✅ import Optional


import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long id);
    List<Review> findByUserId(Long userId);
}