package com.tiffino.reviewservice.repo;

import com.tiffino.reviewservice.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    Optional<ReviewComment> findById(Long id);

//    Optional<User> findByEmail(String email);
//
//    Optional<Review> findByTitle(String title);
//    // Additional methods can go here if needed, e.g.:
//     List<ReviewComment> findByReviewId(Long reviewId);
}
