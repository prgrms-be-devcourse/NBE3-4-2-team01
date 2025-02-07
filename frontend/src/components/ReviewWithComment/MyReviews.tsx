import { MyReviewResponse } from '@/lib/types/MyReviewResponse';
import { useEffect, useState } from 'react';
import ReviewList from './ReviewList';
import { getMyReviews } from '@/lib/api/ReviewApi';
import { PageDto } from '@/lib/types/PageDto';

const MyReviews: React.FC<{ memberId: number }> = ({ memberId }) => {
    const [reviews, setReviews] = useState<MyReviewResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
  
    useEffect(() => {
      const fetchReviews = async () => {
        try {
          setIsLoading(true);
          const response : PageDto<MyReviewResponse>= await getMyReviews(memberId);
          setReviews(response.items);
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
  
    return <>
      <ReviewList reviews={reviews} />
      // 페이징 번호, 이전, 다음 등 클릭 시 새로운 페이지 요청
    </>;
  };

  export default MyReviews;