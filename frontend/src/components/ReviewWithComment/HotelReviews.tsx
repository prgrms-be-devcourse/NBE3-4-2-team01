import { HotelReviewResponse } from '@/lib/types/HotelReviewResponse';
import { useEffect, useState } from 'react';
import ReviewList from './ReviewList';
import { getHotelReviews } from '@/lib/api/ReviewApi';

// 호텔 리뷰 조회
const HotelReviews: React.FC<{ hotelId: number }> = ({ hotelId }) => {
  const [reviews, setReviews] = useState<HotelReviewResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchReviews = async () => {
      try {
        setIsLoading(true);
        const response : HotelReviewResponse[] = await getHotelReviews(hotelId);
        setReviews(response);
        alert('호텔 리뷰 목록 조회 성공');   
      } catch (error) {
        if (error instanceof Error) {
          alert(error.message);
        }
      } finally {
        setIsLoading(false);
      }
    };
    
    fetchReviews();
  }, [hotelId]);

  if (isLoading) {
    return <div className="text-center">로딩 중...</div>;
  }

  return <ReviewList reviews={reviews} />;
};

export default HotelReviews;