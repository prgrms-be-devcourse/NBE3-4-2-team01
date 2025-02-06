import { Dayjs } from 'dayjs';

export interface BookingResponseSummary {
    bookingId : number;
    roomId : number;
    hotelId : number;
    bookNumber : string;
    bookingStatus : string;
    checkInDate : Dayjs;
    checkOutDate : Dayjs;
}