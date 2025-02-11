'use client'

import BookingForm from "@/components/Booking/BookingForm";
import Navigation from "@/components/navigation/Navigation";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";

const BookingDetailsPage = () => {
    const searchParams = useSearchParams();
    // 초기값을 searchParams에서 바로 가져오기
    const [hotelId, setHotelId] = useState<number>(0);
    const [roomId, setRoomId] = useState<number>(0);
    const [checkInDate, setCheckInDate] = useState<string>('');
    const [checkOutDate, setCheckOutDate] = useState<string>('');

    // 혹은 로딩 상태를 추가
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        setHotelId(Number(searchParams.get('hotelId')));
        setRoomId(Number(searchParams.get('roomId')));
        setCheckInDate(searchParams.get('checkInDate') ?? '');
        setCheckOutDate(searchParams.get('checkOutDate') ?? '');
        setIsLoading(false);
    }, [searchParams]);

    if (isLoading) {
        return <div>로딩 중...</div>;  // 또는 로딩 스피너
    }

    return (
        <div>
            <Navigation/>
            <div className="content-wrapper container mx-auto p-4">
                <BookingForm 
                    hotelId={hotelId} 
                    roomId={roomId} 
                    checkInDate={checkInDate} 
                    checkOutDate={checkOutDate}
                />
            </div>
        </div>
    );
};

export default BookingDetailsPage;