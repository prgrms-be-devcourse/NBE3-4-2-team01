'use client'

import HotelReviews from '@/components/ReviewWithComment/HotelReviews';
import { useSearchParams, useParams } from 'next/navigation';


const HotelReviewsPage = () => {

  const searchParams = useSearchParams();
  const params = useParams();
  const hotelId: number = Number(params.hotelId);
  const page = Number(searchParams.get('page')) || 0;

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-xl font-bold mb-4">호텔 리뷰 목록</h1>
      <HotelReviews hotelId={hotelId} page={page} isBusinessUser={true}/>
    </div>
  );
};

export default HotelReviewsPage;