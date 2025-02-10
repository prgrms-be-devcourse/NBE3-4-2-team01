import { BookingHotelDto } from "./BookingHotelDto";
import { BookingRoomDto } from "./BookingRoomDto";
import { PaymentResponse } from "./Payment/PaymentResponse";

export interface BookingResponseSummary {
    bookingId : number;
    room : BookingRoomDto;
    hotel : BookingHotelDto;
    payment : PaymentResponse;
    bookNumber : string;
    bookingStatus : string;
    checkInDate : string;
    checkOutDate : string;
}