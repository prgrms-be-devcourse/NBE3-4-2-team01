'use client'

import BookingList from "@/components/Booking/BookingList";
import Navigation from "@/components/navigation/Navigation";
import { View } from "@/lib/types/Booking/BookingProps";
import { useSearchParams } from "next/navigation";

const HotelBookingsPage = () => {
    const searchParams = useSearchParams();
    const page = Number(searchParams.get('page')) || 1;
    
    return (
        <div>
            <Navigation/>
            <div className="content-wrapper container mx-auto p-4">
                <BookingList view={View.Hotel} page={page} pageSize={7}/>
            </div>
        </div>
    );
};

export default HotelBookingsPage;