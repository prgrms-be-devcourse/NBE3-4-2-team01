'use client'

import MyReviews from '@/components/ReviewWithComment/MyReviews';
import { useSearchParams } from 'next/navigation';

const MyReviewsPage = () => {
  const searchParams = useSearchParams();
  const page = Number(searchParams.get('page')) || 1;

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-xl font-bold mb-4">내 리뷰 목록</h1>
      <MyReviews page={page} />
    </div>
  );
};

export default MyReviewsPage;
