import BookingForm from "@/components/Booking/BookingForm";

const BookingDetailsPage = async () => {
    return <BookingForm hotelId={2} roomId={1} checkInDate="2025-01-01" checkOutDate="2025-01-02"/>;
};

export default BookingDetailsPage;