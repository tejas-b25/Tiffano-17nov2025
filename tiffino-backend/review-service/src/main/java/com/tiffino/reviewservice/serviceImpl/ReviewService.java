package com.tiffino.reviewservice.serviceImpl;

import com.tiffino.reviewservice.dto.ReviewDTO;
import com.tiffino.reviewservice.entity.Review;
import com.tiffino.reviewservice.repo.ReviewCommentRepository;
import com.tiffino.reviewservice.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewCommentRepository commentRepository;

    // Create Review
    public Review createReview(ReviewDTO dto) {
        try {
            Review review = new Review();
            review.setRating(dto.getRating());
            review.setComment(dto.getComment());
            review.setPhotoUrls(dto.getPhotoUrls());
            review.setLikes(dto.getLikes());
            review.setDislikes(dto.getDislikes());
            review.setUserId(dto.getUserId());
            review.setMealId(dto.getMealId());
            review.setOrderId(dto.getOrderId());
            review.setReviewDate(LocalDateTime.now());

            return reviewRepository.save(review);
        } catch (Exception e) {
            throw new RuntimeException("Error saving review: " + e.getMessage(), e);
        }
    }

    // Update Review
    public Review updateReview(Long reviewId, ReviewDTO dto)
    {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setRating(dto.getRating());
            review.setComment(dto.getComment());
            review.setPhotoUrls(dto.getPhotoUrls());
            review.setLikes(dto.getLikes());
            review.setDislikes(dto.getDislikes());
            review.setReviewDate(LocalDateTime.now()); // update date
            return reviewRepository.save(review);
        } else {
            throw new RuntimeException("Review not found with id: " + reviewId);
        }
    }

    // Delete Review
    public void deleteReview(Long reviewId) {
        if (reviewRepository.existsById(reviewId))
        {
            reviewRepository.deleteById(reviewId);
        }
        else {
            throw new RuntimeException("Review not found with id: " + reviewId);
        }
    }

    // Get Review by ID
    public Review getReviewById(Long reviewId)
    {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
    }

    // Get all Reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Get Reviews by User ID
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findAll()
                .stream()
                .filter(r -> r.getUserId().equals(userId))
                .toList();
    }
}
