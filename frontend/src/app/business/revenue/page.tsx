import HotelRevenue from "@/components/Booking/Revenue/Revenue";


const RevenuePage = async ({id} : {id : string}) => {
    const hotelId = Number(id);
    return <HotelRevenue hotelId={2}/>;
};

export default RevenuePage;