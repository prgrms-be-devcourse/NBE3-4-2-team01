'use client'

import { useEffect, useState } from "react";
import { getBookingDetails } from "@/lib/api/Booking/BookingApi";
import { BookingResponseDetails } from "@/lib/types/Booking/BookingResponseDetails";

const BookingDetails = function({bookingId} : {bookingId : number}) {
    const [bookingDetails, setBookingDetails] = useState<BookingResponseDetails | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchBooking = async () => {
            setIsLoading(true);
            let data : BookingResponseDetails | null = null;
            try {
                data = await getBookingDetails(bookingId);
            } catch (error) {
                console.error(error);
            } finally {
                setBookingDetails(data);
                setIsLoading(false);
            }
        };
        fetchBooking();
    }, []);

    // 로딩 중일때
    if (isLoading) {
        return <div>예약을 불러오는 중입니다...</div>
    }

    // data를 받아오지 못했을 때
    if (!bookingDetails) {
        return <div>예약을 불러올 수 없습니다.</div>
    }

    return (
        <div>
          <h2>예약 상세 정보</h2>
          <ul>
            <li><strong>예약 ID:</strong> {bookingDetails.bookingId}</li>
            <li><strong>방 ID:</strong> {bookingDetails.roomId}</li>
            <li><strong>호텔 ID:</strong> {bookingDetails.hotelId}</li>
            <li><strong>예약 번호:</strong> {bookingDetails.bookNumber}</li>
            <li><strong>예약 상태:</strong> {bookingDetails.bookingStatus}</li>
            <li><strong>예약 생성일:</strong> {bookingDetails.createdAt}</li>
            <li><strong>체크인 날짜:</strong> {bookingDetails.checkInDate}</li>
            <li><strong>체크아웃 날짜:</strong> {bookingDetails.checkOutDate}</li>
          </ul>

          <h3>결제 정보</h3>
          <ul>
            <li><strong>결제 ID:</strong> {bookingDetails.payment.paymentId}</li>
            <li><strong>Merchant UID:</strong> {bookingDetails.payment.merchantUid}</li>
            <li><strong>결제 금액:</strong> {bookingDetails.payment.amount}원</li>
            <li><strong>결제 상태:</strong> {bookingDetails.payment.paymentStatus}</li>
            <li><strong>결제 일시:</strong> {bookingDetails.payment.paidAt}</li>
          </ul>
        </div>
        
    );
}

export default BookingDetails;