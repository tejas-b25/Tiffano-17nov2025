package com.tiffino.reviewservice.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class ReviewCommentDTO {
    private String comment;

    @CreationTimestamp                      // ✅ Auto-sets current time when saved
    @Column(updatable = false, nullable = false)
    private LocalDateTime commentDate;

    private Long reviewId; // Reference to associated Review
    private Long userId;   // Reference to the User who commented
}
