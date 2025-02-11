export interface BookingResponseSummary {
    bookingId : number;
    hotelName : string;
    roomName : string;
    thumbnailUrl : string;
    bookingStatus : string;
    amount : number;
    checkInDate : string;
    checkOutDate : string;
}