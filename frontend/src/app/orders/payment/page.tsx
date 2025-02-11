import BookingForm from "@/components/Booking/BookingForm";
import Navigation from "@/components/navigation/Navigation";

const BookingDetailsPage = async () => {
    return (
        <div>
            <Navigation/>
            <div className="content-wrapper container mx-auto p-4">
                <BookingForm hotelId={2} roomId={1} checkInDate="2025-01-01" checkOutDate="2025-01-02"/>;
            </div>
        </div>
    );
};

export default BookingDetailsPage;