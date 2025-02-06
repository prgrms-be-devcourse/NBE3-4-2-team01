import { Dayjs } from 'dayjs'

export interface BookingRequest {
    roomId : number;
    hotelId : number;
    checkInDate : Dayjs;
    checkOutDate : Dayjs;
    
    // PaymentRequest 생성에 필요
    merchantUid : string;
    amoumt : number;
    paidAtTimestamp : number;
}