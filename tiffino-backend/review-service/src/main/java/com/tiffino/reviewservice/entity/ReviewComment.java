package com.tiffino.reviewservice.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Transactional
@NoArgsConstructor
@Getter
@Setter
@Table(name = "review_comments")
public class ReviewComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @CreationTimestamp                      // ✅ Auto-sets current time when saved
    @Column(updatable = false, nullable = false)
    private LocalDateTime commentDate;

    @ManyToOne
    @JoinColumn(name = "review_id")
    @JsonBackReference // Matches Review.reviewComments
    private Review review;

    private Long userId;


}
