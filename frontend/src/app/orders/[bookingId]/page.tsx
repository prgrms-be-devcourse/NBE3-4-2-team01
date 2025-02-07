import BookingDetails from "@/components/Booking/BookingDetails";

const BookingDetailsPage = async ({params} : {params : {bookingId : string}}) => {
    const id = Number((await params).bookingId);
    return <BookingDetails bookingId={id}/>;
};

export default BookingDetailsPage;