import { HotelRevenueResponse } from "@/lib/types/Booking/Revenue/HotelRevenueResponse";

// 예약 상세 조회
export const getHotelRevenue = async function (
  hotelId: number
): Promise<HotelRevenueResponse> {
  try {
    const response = await fetch(
      `http://localhost:8080/api/hotels/${hotelId}/revenue`,
      {
        credentials: "include",
      }
    );
    const rsData = await response.json();

    if (!response.ok) {
      throw new Error(`${rsData.msg}`);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};
