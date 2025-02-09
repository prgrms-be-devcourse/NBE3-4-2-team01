'use client'

import Navigation from '@/components/navigation/Navigation';
import HotelReviews from '@/components/ReviewWithComment/HotelReviews';
import { useSearchParams, useParams } from 'next/navigation';

const BusinessHotelReviewsPage = () => {

   const searchParams = useSearchParams();
   const params = useParams();
   const hotelId: number = Number(params.hotelId);
   const page = Number(searchParams.get('page')) || 1;

  return (
    <>
      <Navigation/>
      <div className="content-wrapper container mx-auto p-4">
        <h1 className="text-xl font-bold mb-4">호텔 리뷰 목록</h1>
        <HotelReviews hotelId={hotelId} page={page}/>
      </div>
    </>
  );
};

export default BusinessHotelReviewsPage;