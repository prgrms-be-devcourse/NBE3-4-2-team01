package com.ll.hotel.domain.review.comment.controller;

import com.ll.hotel.domain.review.comment.dto.ReviewCommentContentRequest;
import com.ll.hotel.domain.review.comment.dto.ReviewCommentDto;
import com.ll.hotel.domain.review.comment.service.ReviewCommentService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews/{reviewId}/comments")
@RequiredArgsConstructor
@Tag(name = "ReviewCommentController")
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;

    @PostMapping("")
    @Operation(summary = "리뷰 답변 생성")
    public RsData<Empty> createReviewComment(
            @PathVariable("reviewId") long reviewId,
            @RequestBody ReviewCommentContentRequest contentRequest
    ) {
        // 인증 로직 (로그인 여부 체크)

        // 권한 로직 (해당 리뷰의 호텔 관리자인지 체크)

        reviewCommentService.createReviewComment(reviewId, contentRequest.content());
        return RsData.OK; // 201 vs 200
    }

    @GetMapping("/{commentId}")
    @Operation(summary = "리뷰 답변 조회")
    public RsData<ReviewCommentDto> getReviewComment(
            @PathVariable("reviewId") long reviewId,
            @PathVariable("commentId") long commentId
    ) {

        return new RsData<>(
                "200-1",
                "리뷰 답변 조회 성공",
                reviewCommentService.getReviewComment(commentId)
        );
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "리뷰 답변 수정")
    public RsData<Empty> updateReviewComment(
            @PathVariable("reviewId") long reviewId,
            @PathVariable("commentId") long commentId,
            @RequestBody ReviewCommentContentRequest contentRequest
    ) {
        // 인증 로직 (로그인 여부 체크)

        // 권한 로직 (작성자인지 체크)

        reviewCommentService.updateReviewComment(commentId, contentRequest.content());
        return RsData.OK;
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "리뷰 답변 삭제")
    public RsData<Empty> deleteReviewComment(
            @PathVariable("reviewId") long reviewId,
            @PathVariable("commentId") long commentId
    ) {
        // 인증 로직 (로그인 여부 체크)

        // 권한 로직 (작성자인지 체크)

        reviewCommentService.deleteReviewComment(commentId);
        return RsData.OK;
    }
}
