import React from 'react';
import MyReviewWithComment from './MyReviewWithComment';
import HotelReviewWithComment from './HotelReviewWithComment';
import { ReviewResponseType } from './MyReviewWithComment';
import { isHotelReview } from './MyReviewWithComment';
import { ReviewDto } from '@/lib/types/ReviewDto';

interface ReviewListProps {
    reviews: ReviewResponseType[];
    isBusinessUser?: boolean;
    onCommentUpdate?: () => void;
  }
  
  const ReviewList: React.FC<ReviewListProps> = ({ reviews, isBusinessUser=false, onCommentUpdate }) => {
    console.log(reviews);
    if (reviews.length === 0) {
      return (
        <div className="max-w-3xl mx-auto p-4 text-center text-gray-500">
          작성된 리뷰가 없습니다.
        </div>
      );
    }
  
    return (
      <div className="max-w-3xl mx-auto p-4">
        {reviews.map((review) => {
          let reviewDto : ReviewDto;
          if(isHotelReview(review)) {
            reviewDto = review.hotelReviewWithCommentDto.reviewDto;
            return (
              <HotelReviewWithComment
                key={reviewDto.reviewId}
                review={review}
                isBusinessUser={isBusinessUser}
                onCommentUpdate={onCommentUpdate}
              />);
          } else {
            reviewDto = review.myReviewWithCommentDto.reviewDto;
            return (
              <MyReviewWithComment
                key={reviewDto.reviewId}
                review={review}
              />);
          }
        })}
      </div>
    );
  };
  
  export default ReviewList;