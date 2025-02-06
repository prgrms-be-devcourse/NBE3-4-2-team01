import React from 'react';
import ReviewWithComment from './ReviewWithComment';
import { ReviewResponseType } from './ReviewWithComment';
import { isHotelReview } from './ReviewWithComment';
import { ReviewDto } from '@/lib/types/ReviewDto';

interface ReviewListProps {
    reviews: ReviewResponseType[];
  }
  
  const ReviewList: React.FC<ReviewListProps> = ({ reviews }) => {
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
          const reviewDto : ReviewDto = isHotelReview(review) 
            ? review.hotelReviewWithCommentDto.reviewDto 
            : review.myReviewWithCommentDto.reviewDto;
            
          return (
            <ReviewWithComment
              key={reviewDto.reviewId}
              review={review}
            />
          );
        })}
      </div>
    );
  };
  
  export default ReviewList;