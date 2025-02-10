"use client";

import { getAllHotelsForAdmin } from "@/lib/api/Admin/AdminHotelApi";
import { AdminHotelSummaryReponse } from "@/lib/types/admin/response/AdminHotelResponse";
import { PageDto } from "@/lib/types/PageDto";
import { useParams, usePathname } from "next/navigation";
import { useEffect, useState } from "react";

export default function AdminHotelsPage() {
  const [hotels, setHotels] = useState<AdminHotelSummaryReponse[]>([]);
  const { hotelId } = useParams();
  const pathname = usePathname();

  useEffect(() => {
    async function fetchHotels() {
      try {
        const response: PageDto<AdminHotelSummaryReponse> | null =
          await getAllHotelsForAdmin();
        setHotels(response?.items || []);
      } catch (error) {
        console.error("호텔 데이터를 불러오는 중 오류 발생:", error);
        setHotels([]);
      }
    }
    fetchHotels();
  }, []);

  function handleHotelClick(hotelId: number) {
    window.location.href = `${pathname}/${hotelId}`;
  }

  return (
    <div>
      <h1>호텔 목록</h1>
      <ul>
        {hotels.map((hotel) => (
          <li key={hotel.id}>
            {hotel.name} - {hotel.streetAddress}, {hotel.zipCode} -{" "}
            {hotel.grade}등급 - {hotel.averageRating}점
            <button onClick={() => handleHotelClick(hotel.id)}>
              세부 내역 보기
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
