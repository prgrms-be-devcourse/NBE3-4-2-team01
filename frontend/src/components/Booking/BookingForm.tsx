'use client';

import { BookingProps } from '@/lib/types/Booking/BookingProps';
import BookingFormLeft from './BookingFormLeft';
import BookingFormRight from './BookingFormRight';
import { useEffect, useState } from 'react';
import { GetRoomDetailResponse } from '@/lib/types/room/GetRoomDetailResponse';
import { GetHotelDetailResponse } from '@/lib/types/hotel/GetHotelDetailResponse';
import { findHotelDetail } from '@/lib/api/BusinessHotelApi';
import { findRoomDetail } from '@/lib/api/BusinessRoomApi';

const BookingForm = ({hotelId, roomId, checkInDate, checkOutDate}: BookingProps) => {
  const [hotelDetails, setHotelDetails] = useState<GetHotelDetailResponse | null>(null);
  const [roomDetails, setRoomDetails] = useState<GetRoomDetailResponse | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  
  useEffect(() => {
          const fetchBooking = async () => {
              setIsLoading(true);
              let hotelData : GetHotelDetailResponse | null = null;
              let roomData : GetRoomDetailResponse | null = null;
              try {
                  hotelData = await findHotelDetail(hotelId);
                  roomData = await findRoomDetail(hotelId, roomId);
              } catch (error) {
                  console.error(error);
              } finally {
                  setHotelDetails(hotelData);
                  setRoomDetails(roomData);
                  setIsLoading(false);
              }
          };
          fetchBooking();
      }, []);
  
      if (isLoading) {
          return <div>호텔/객실 정보를 불러오는 중입니다...</div>
      }
  
      if (!hotelDetails || !roomDetails) {
          return <div>호텔/객실 정보를 불러올 수 없습니다.</div>
      }
  
  return (
    <div className="w-full max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-[1.5fr,1fr] gap-6">
      <BookingFormLeft hotelDetails={hotelDetails} roomDetails={roomDetails}></BookingFormLeft>
      <BookingFormRight 
      hotelName={hotelDetails.hotelDetailDto.hotelName} 
      roomName={roomDetails.roomDto.roomName} 
      amount={roomDetails.roomDto.basePrice} 
      bookingProps={{hotelId, roomId, checkInDate, checkOutDate}}>
      </BookingFormRight>
    </div>
  );
};

export default BookingForm;