import { BookingHotelDto } from './BookingHotelDto';
import { BookingMemberDto } from './BookingMemberDto';
import { BookingRoomDto } from './BookingRoomDto';
import { PaymentResponse } from './Payment/PaymentResponse';

export interface BookingResponseDetails {
    bookingId : number;
    room : BookingRoomDto;
    hotel : BookingHotelDto;
    member : BookingMemberDto;
    payment : PaymentResponse;
    bookNumber : string;
    bookingStatus : string;
    createdAt : string;
    checkInDate : string;
    checkOutDate : string;
}