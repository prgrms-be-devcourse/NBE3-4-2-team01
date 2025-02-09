import BookingDetails from "@/components/Booking/BookingDetails";
import { View } from "@/lib/types/Booking/BookingProps";

const BookingDetailsPage = async ({params} : {params : {bookingId : string}}) => {
    const id = Number((await params).bookingId);
    return <BookingDetails view={View.Hotel} bookingId={id}/>;
};

export default BookingDetailsPage;