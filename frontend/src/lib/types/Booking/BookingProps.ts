import { GetHotelDetailResponse } from "../hotel/GetHotelDetailResponse";
import { GetRoomDetailResponse } from "../room/GetRoomDetailResponse";
import { KakaoPaymentRequest } from "./Payment/KakaoPaymentRequest";

export const enum View {
    All,
    User,
    Hotel
};

export type BookingListProps = {
    view : View;
    page? : number;
    pageSize? : number;
};

export type BookingProps = {
    hotelId : number;
    roomId : number;
    checkInDate : string;
    checkOutDate : string;
}

export type BookingFormLeftProps = {
    hotelDetails : GetHotelDetailResponse;
    roomDetails : GetRoomDetailResponse;
}

export type BookingFormRightProps = {
    hotelName : string;
    roomName : string;
    amount : number;
    bookingProps : BookingProps;
}

export type PaymentProps = {
    buyerName : string;
    buyerEmail : string;
    productName : string;
    amount : number;
    onPaymentComplete : (kakapPaymentRequest : KakaoPaymentRequest) => void;
}