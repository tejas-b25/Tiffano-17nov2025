package com.tiffino.reviewservice.controller;

import com.tiffino.reviewservice.dto.ReviewDTO;
import com.tiffino.reviewservice.entity.Review;
import com.tiffino.reviewservice.repo.ReviewRepository;
import com.tiffino.reviewservice.serviceImpl.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Controller {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewService reviewService;

    // Create review
    @PostMapping("/reviewsSave")
    public ResponseEntity<?> createReview(@RequestBody ReviewDTO dto)
    {
        try {
            Review savedReview = reviewService.createReview(dto);
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new EntityNotFoundException("Error saving review: " + e.getMessage());
        }
    }

    // Get all reviews
    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews()
    {
        List<Review> reviews = reviewRepository.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Get reviews by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Update review
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO dto) {
        try {
            Review updatedReview = reviewService.updateReview(reviewId, dto);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
        }
    }

    // Delete review
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok().build(); // 204 with no body
        } catch (EntityNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 with no body
        }
    }

}
