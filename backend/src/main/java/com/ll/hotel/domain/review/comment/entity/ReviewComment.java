package com.ll.hotel.domain.review.comment.entity;

import com.ll.hotel.domain.review.comment.type.ReviewCommentStatus;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "리뷰 정보는 필수입니다.")
    private Review review;

    @Column(length = 1000)
    @Setter
    @Size(min = 5, max = 200, message = "답변 내용은 필수입니다.")
    private String content;

    @Enumerated(EnumType.STRING)
    @Setter
    @NotNull(message = "리뷰 답변 상태는 필수입니다.")
    private ReviewCommentStatus reviewCommentStatus;

    @Builder
    private ReviewComment(Review review, String content, ReviewCommentStatus reviewCommentStatus) {
        this.review = review;
        this.content = content;
        this.reviewCommentStatus = reviewCommentStatus;
    }
}