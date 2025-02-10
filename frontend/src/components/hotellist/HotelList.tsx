import HotelInfo from './HotelInfo';
import { GetHotelResponse } from '@/lib/types/hotel/GetHotelResponse';

interface HotelListProps {
  hotels: GetHotelResponse[];
}

export default function HotelList({ hotels }: HotelListProps) {
  return (
    <div className="flex flex-col items-center space-y-4 w-full">
      {hotels.map((hotel) => (
        <HotelInfo key={hotel.hotelId} {...hotel}/>
      ))}
    </div>
  );
}