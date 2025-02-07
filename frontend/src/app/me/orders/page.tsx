import BookingList from "@/components/Booking/BookingList";
import { View } from "@/lib/types/Booking/BookingListProps";

const OrdersPage = () => {
    return (
        <div>
            <h1>내 예약 목록</h1>
            <BookingList view={View.User} />
        </div>
    );
};

export default OrdersPage;