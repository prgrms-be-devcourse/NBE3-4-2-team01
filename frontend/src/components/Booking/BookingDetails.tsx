'use client'

import { useEffect, useState } from "react";
import { getBookingDetails } from "@/lib/api/Booking/BookingApi";
import { BookingResponseDetails } from "@/lib/types/Booking/BookingResponseDetails";
import { BookingDetailsProps, View } from "@/lib/types/Booking/BookingProps";
import UserBookingDetails from "./UserBookingDetails";
import HotelBookingDetails from "./HotelBookingDetails";

const BookingDetails = function({view, bookingId} : BookingDetailsProps) {
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

    switch (view) {
        case View.User: return <UserBookingDetails bookingDetails={bookingDetails}/>;
        case View.Hotel: return <HotelBookingDetails bookingDetails={bookingDetails}/>;
        default: return <div>예약을 불러올 수 없습니다.</div>
    }
}

export default BookingDetails;