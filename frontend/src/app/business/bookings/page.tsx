import BookingList from "@/components/Booking/BookingList";
import { View } from "@/lib/types/Booking/BookingProps";

const HotelBookingsPage = () => {
    return (
        <div>
            <h1>호텔 예약 목록</h1>
            <BookingList view={View.Hotel} />
        </div>
    );
};

export default HotelBookingsPage;