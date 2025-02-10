'use client';

import { BookingProps } from '@/lib/types/Booking/BookingProps';
import BookingFormLeft from './BookingFormLeft';
import BookingFormRight from './BookingFormRight';

const BookingForm = ({hotelId, roomId, checkInDate, checkOutDate}: BookingProps) => {
  return (
    <div className="w-full max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-[1.5fr,1fr] gap-6">
      <BookingFormLeft hotelId={hotelId} roomId={roomId}></BookingFormLeft>
      <BookingFormRight hotelId={hotelId} roomId={roomId}
      checkInDate={checkInDate} checkOutDate={checkOutDate}></BookingFormRight>
    </div>
  );
};

export default BookingForm;