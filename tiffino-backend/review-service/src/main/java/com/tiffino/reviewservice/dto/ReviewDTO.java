package com.tiffino.reviewservice.dto;

import com.tiffino.reviewservice.entity.ReviewStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDTO {

    private Long id;
    private Long rating;
    private String comment;
    private LocalDateTime reviewDate;  // optional – can be set in service
    private String photoUrls;
    private Integer likes;
    private Integer dislikes;
    //  private ReviewStatus status;

    private Long userId;     // reference to existing user
    private Long mealId;     // reference to existing meal
    private Long orderId;    // reference to existing order
}
