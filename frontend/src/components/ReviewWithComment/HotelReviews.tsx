import { HotelReviewResponse } from '@/lib/types/HotelReviewResponse';
import { useEffect, useState } from 'react';
import ReviewList from './ReviewList';
import { getHotelReviews } from '@/lib/api/ReviewApi';
import { HotelReviewListResponse } from '@/lib/types/HotelReviewListResponse';
import { PageDto } from '@/lib/types/PageDto';
import Pagination from '../Pagination/Pagination';

interface HotelReviewsProps {
  hotelId: number;
  page: number;
  isBusinessUser?: boolean;
}

// 호텔 리뷰 조회
const HotelReviews: React.FC<HotelReviewsProps> = ({ hotelId, page, isBusinessUser=false }) => {
  const [response, setResponse] = useState<HotelReviewListResponse | null>(null);
  const [reviewPage, setReviewPage] = useState<PageDto<HotelReviewResponse> | null>(null);
  const [reviews, setReviews] = useState<HotelReviewResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const fetchReviews = async () => {
    try {
      setIsLoading(true);
      const response : HotelReviewListResponse = await getHotelReviews(hotelId, page);
      const reviewPage : PageDto<HotelReviewResponse> = response.hotelReviewPage;
      setResponse(response);
      setReviewPage(reviewPage);
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

  useEffect(() => {  
    fetchReviews();
  }, [hotelId, page]);


  if (isLoading) {
    return <div className="text-center">로딩 중...</div>;
  }

  return <>
    <div>평균 리뷰 점수 : {response?.averageRating}</div>
    <ReviewList reviews={reviews} isBusinessUser={isBusinessUser} onCommentUpdate={fetchReviews}/>
    <Pagination currentPage={page} totalPages={reviewPage?.totalPages || 1} basePath={`hotels/${hotelId}/reviews`} />
  </>;
};

export default HotelReviews;