import React from 'react';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Star } from 'lucide-react';
import { HotelReviewResponse } from '@/lib/types/HotelReviewResponse';
import { MyReviewResponse } from '@/lib/types/MyReviewResponse';


export type ReviewResponseType = HotelReviewResponse | MyReviewResponse;

// Type guard to check if the review is HotelReviewResponse
export const isHotelReview = (review: ReviewResponseType): review is HotelReviewResponse => {
  return 'hotelReviewWithCommentDto' in review;
};

interface ReviewWithCommentProps {
  review: ReviewResponseType;
}

export const ReviewWithComment: React.FC<ReviewWithCommentProps> = ({ review }) => {
  const getReviewData = (review: ReviewResponseType) => {
    if (isHotelReview(review)) {
      const { hotelReviewWithCommentDto, imageUrls } = review;
      return {
        title: hotelReviewWithCommentDto.memberEmail,
        roomTypeName: hotelReviewWithCommentDto.roomTypeName,
        reviewDto: hotelReviewWithCommentDto.reviewDto,
        reviewCommentDto: hotelReviewWithCommentDto.reviewCommentDto,
        reservedAt: hotelReviewWithCommentDto.reservedAt,
        imageUrls
      };
    } else {
      const { myReviewWithCommentDto, imageUrls } = review;
      return {
        title: myReviewWithCommentDto.hotelName,
        roomTypeName: myReviewWithCommentDto.roomTypeName,
        reviewDto: myReviewWithCommentDto.reviewDto,
        reviewCommentDto: myReviewWithCommentDto.reviewCommentDto,
        reservedAt: myReviewWithCommentDto.reservedAt,
        imageUrls
      };
    }
  };

  const {
    title,
    roomTypeName,
    reviewDto,
    reviewCommentDto,
    reservedAt,
    imageUrls
  } = getReviewData(review);

  return (
    <Card className="mb-4">
      <CardHeader>
        <div className="flex justify-between items-center">
          <div>
            <h3 className="text-lg font-semibold">{title}</h3>
            <p className="text-sm text-gray-500">
              {roomTypeName} • {new Date(reservedAt).toLocaleDateString()}
            </p>
          </div>
          <div className="flex items-center">
            {[...Array(5)].map((_, index) => (
              <Star
                key={index}
                className={`w-5 h-5 ${
                  index < reviewDto.rating
                    ? 'text-yellow-400 fill-yellow-400'
                    : 'text-gray-300'
                }`}
              />
            ))}
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {/* Review Content */}
        <div className="mb-4">
          <p className="text-gray-700">{reviewDto.content}</p>
          <p className="text-sm text-gray-500 mt-2">
            {new Date(reviewDto.createdAt).toLocaleString()}
          </p>
          
          {/* Review Images */}
          {imageUrls.length > 0 && (
            <div className="grid grid-cols-3 gap-2 mt-4">
              {imageUrls.map((url, index) => (
                <img
                  key={index}
                  src={url}
                  alt={`Review image ${index + 1}`}
                  className="w-full h-32 object-cover rounded"
                />
              ))}
            </div>
          )}
        </div>

        {/* Comment Section */}
        {reviewCommentDto && (
          <div className="mt-4 pl-4 border-l-4 border-gray-700 bg-gray-100 relative rounded-lg">
          {/* 답변 왼쪽 디자인 (테두리와 배경색을 활용) */}
          <div className="absolute left-[-20px] top-1/2 transform -translate-y-1/2 w-0 h-0 border-l-[10px] border-l-transparent border-b-[10px] border-b-gray-100"></div>
    
          {/* 답변 내용 */}
          <div className="bg-gray-200 p-4 rounded">
            <p className="text-gray-700">{reviewCommentDto.content}</p>
            <p className="text-sm text-gray-500 mt-2">
              {new Date(reviewCommentDto.createdAt).toLocaleString()}
            </p>
          </div>
        </div>
        )}
      </CardContent>
    </Card>
  );
};

export default ReviewWithComment;