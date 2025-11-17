package com.tiffino.reviewservice.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Transactional
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reviews")

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rating;
    private String comment;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime reviewDate;

    // If single URL
    private String photoUrls;

    // Or if multiple URLs:
    // @ElementCollection
    // private List<String> photoUrls;

    private Integer likes;  //positive
    private Integer dislikes;  //negative

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    @JsonIgnore
    private List<ReviewComment> reviewComments;

    private Long userId;
    private Long mealId;
    private Long orderId;
}

