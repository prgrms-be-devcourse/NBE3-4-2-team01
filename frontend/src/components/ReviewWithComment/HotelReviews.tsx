import { HotelReviewResponse } from '@/lib/types/HotelReviewResponse';
import { useEffect, useState } from 'react';
import ReviewList from './ReviewList';
import { getHotelReviews } from '@/lib/api/ReviewApi';
import { HotelReviewListResponse } from '@/lib/types/HotelReviewListResponse';
import { PageDto } from '@/lib/types/PageDto';

// 호텔 리뷰 조회
const HotelReviews: React.FC<{ hotelId: number }> = ({ hotelId }) => {
  const [reviews, setReviews] = useState<HotelReviewResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchReviews = async () => {
      try {
        setIsLoading(true);
        const response : HotelReviewListResponse = await getHotelReviews(hotelId);
        const reviewPage : PageDto<HotelReviewResponse> = response.hotelReviewPage;
        setReviews(reviewPage.items);
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

  return <>
  // 호텔 평점 표시
    <ReviewList reviews={reviews} />
  // 페이징 번호, 이전, 다음 등 클릭 시 새로운 페이지 요청
  </>;
};

export default HotelReviews;