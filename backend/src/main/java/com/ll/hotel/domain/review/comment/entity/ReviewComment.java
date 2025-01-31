package com.ll.hotel.domain.review.comment.entity;

import com.ll.hotel.domain.review.comment.type.ReviewCommentStatus;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "review_comments")
public class ReviewComment extends BaseTime {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Review review;

    @Column(length = 1000)
    @Setter
    private String content;

    @Enumerated(EnumType.STRING)
    @Setter
    private ReviewCommentStatus reviewCommentStatus;

    @Builder
    private ReviewComment(Review review, String content, ReviewCommentStatus reviewCommentStatus) {
        this.review = review;
        this.content = content;
        this.reviewCommentStatus = reviewCommentStatus;
    }
}