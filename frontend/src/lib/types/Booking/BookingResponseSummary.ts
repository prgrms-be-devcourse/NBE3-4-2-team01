export interface BookingResponseSummary {
    bookingId : number;
    hotelId : number;
    roomId : number;
    hotelName : string;
    roomName : string;
    thumbnailUrl : string;
    bookingStatus : string;
    amount : number;
    checkInDate : string;
    checkOutDate : string;
}