import { Dayjs } from 'dayjs'
import { PaymentResponse } from './Payment/PaymentResponse';

export interface BookingResponseDetails {
    bookingId : number;
    roomId : number;
    hotelId : number;
    payment : PaymentResponse;
    bookNumber : string;
    bookingStatus : string;
    createdAt : Dayjs;
    checkInDate : Dayjs;
    checkOutDate : Dayjs;
}