import { HotelRevenueResponse } from "@/lib/types/Booking/Revenue/HotelRevenueResponse";

// 예약 상세 조회
export const getHotelRevenue = async function(hotelId : number) : Promise<HotelRevenueResponse> {
    try {
        const response = await fetch(`http://localhost:8080/api/hotels/${hotelId}/revenue`, {
            credentials: 'include',
        });
        const rsData = await response.json();

        if (rsData.resultCode.startsWith("2")) {
            return rsData.data;
        } else {
            throw new Error(`${rsData.msg}`);
        }
    } catch (error) {
        throw error;
    }
}