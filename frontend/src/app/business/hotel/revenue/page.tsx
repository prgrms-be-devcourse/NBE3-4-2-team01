import HotelRevenue from "@/components/Booking/Revenue/Revenue";
import Navigation from "@/components/navigation/Navigation";


const RevenuePage = async ({id} : {id : string}) => {
    const hotelId = Number(id);
    return (
        <div>
            <Navigation></Navigation>
            <div className="content-wrapper container mx-auto p-4">
                <HotelRevenue hotelId={2}/>
            </div>
        </div>
    );
};

export default RevenuePage;