'use client'

import { useEffect, useState } from "react";
import { getHotelRevenue } from "@/lib/api/Booking/Revenue/RevenueApi";
import { HotelRevenueResponse } from "@/lib/types/Booking/Revenue/HotelRevenueResponse";

const HotelRevenue = function({hotelId} : {hotelId : number}) {
    const [hotelRevenue, setHotelRevenue] = useState<HotelRevenueResponse | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchBooking = async () => {
            setIsLoading(true);
            let data : HotelRevenueResponse | null = null;
            try {
                data = await getHotelRevenue(hotelId);
            } catch (error) {
                console.error(error);
            } finally {
                setHotelRevenue(data);
                setIsLoading(false);
            }
        };
        fetchBooking();
    }, []);

    // 로딩 중일때
    if (isLoading) {
        return <div>매출 통계를 불러오는 중입니다...</div>
    }

    // data를 받아오지 못했을 때
    if (!hotelRevenue) {
        return <div>매출 통계를 불러올 수 없습니다.</div>
    }

    return (
        <div style={{ textAlign: "center" }}>
          <h2>총 매출: {hotelRevenue.revenue.toLocaleString()}원</h2>
          <table style={{ width: "100%", marginTop: "20px", borderCollapse: "collapse", border: "1px solid black" }}>
            <thead>
              <tr style={{ borderBottom: "2px solid black" }}>
                <th style={{ border: "1px solid black", padding: "8px" }}>객실 ID</th>
                <th style={{ border: "1px solid black", padding: "8px" }}>객실 유형</th>
                <th style={{ border: "1px solid black", padding: "8px" }}>기본 가격</th>
                <th style={{ border: "1px solid black", padding: "8px" }}>객실 매출</th>
              </tr>
            </thead>
            <tbody>
              {hotelRevenue.roomRevenueResponse.map((room) => (
                <tr key={room.roomId}>
                  <td style={{ border: "1px solid black", padding: "8px" }}>{room.roomId}</td>
                  <td style={{ border: "1px solid black", padding: "8px" }}>{room.roomName}</td>
                  <td style={{ border: "1px solid black", padding: "8px" }}>{room.basePrice.toLocaleString()}원</td>
                  <td style={{ border: "1px solid black", padding: "8px" }}>{room.roomRevenue.toLocaleString()}원</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      );
}

export default HotelRevenue;