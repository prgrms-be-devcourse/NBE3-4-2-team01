import { MyReviewResponse } from '@/lib/types/MyReviewResponse';
import { useEffect, useState } from 'react';
import ReviewList from './ReviewList';
import { getMyReviews } from '@/lib/api/ReviewApi';

const MyReviews: React.FC<{ memberId: number }> = ({ memberId }) => {
    const [reviews, setReviews] = useState<MyReviewResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
  
    useEffect(() => {
      const fetchReviews = async () => {
        try {
          setIsLoading(true);
          const response = await getMyReviews(memberId);
          setReviews(response);
          alert('내 리뷰 목록 조회 성공');
        } catch (error) {
          console.error('리뷰를 불러오는 중 오류가 발생했습니다:', error);
        } finally {
          setIsLoading(false);
        }
      };
      
      fetchReviews();
    }, [memberId]);
  
    if (isLoading) {
      return <div className="text-center">로딩 중...</div>;
    }
  
    return <ReviewList reviews={reviews} />;
  };

  export default MyReviews;