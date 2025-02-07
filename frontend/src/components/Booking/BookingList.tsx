'use client'

import { useEffect, useState } from "react";
import HotelBookingList from "./HotelBookingList";
import UserBookingList from "./UserBookingList";
import { BookingResponseSummary } from "@/lib/types/Booking/BookingResponseSummary";
import { PageDto } from "@/lib/types/PageDto";
import { getHotelBookings, getMyBookings } from "@/lib/api/Booking/BookingApi";
import { BookingListProps, View } from "@/lib/types/Booking/BookingProps";

const BookingList = function({view, page, pageSize} : BookingListProps) {
    const [bookings, setBookings] = useState<PageDto<BookingResponseSummary> | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchBookings = async () => {
            setIsLoading(true);
            let data : PageDto<BookingResponseSummary> | null = null;
            try {
                switch (view) {
                    case View.User: data = await getMyBookings(page, pageSize); break;
                    case View.Hotel: data = await getHotelBookings(page, pageSize); break;
                    default: throw new Error('잘못된 Booking View 입니다.');
                }
            } catch (error) {
                console.error(error);
            } finally {
                setBookings(data);
                setIsLoading(false);
            }
        };
        fetchBookings();
    }, [page, pageSize]);

    // 로딩 중일때
    if (isLoading) {
        return <div>예약을 불러오는 중입니다...</div>
    }

    // data를 받아오지 못했을 때
    // view가 잘못되었을 때도 처리됨
    if (!bookings) {
        return <div>예약을 불러올 수 없습니다.</div>
    }

    switch (view) {
        case View.User: return <UserBookingList bookings={bookings}/>;
        case View.Hotel: return <HotelBookingList bookings={bookings}/>;
    }
}

export default BookingList;