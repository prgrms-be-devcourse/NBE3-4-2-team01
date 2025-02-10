"use client";

import { approveHotel, getHotelForAdmin } from "@/lib/api/Admin/AdminHotelApi";
import { AdminHotelRequest } from "@/lib/types/admin/request/AdminHotelRequest";
import { HotelStatus } from "@/lib/types/hotel/HotelStatus";
import { HotelDto } from "@/lib/types/HotelDto";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import Link from "next/link";
import { HotelApprovalResult } from "@/lib/types/admin/response/AdminHotelResponse";

export default function AdminHotelDetailPage() {
  const [hotel, setHotel] = useState<HotelDto | null>(null);
  const [status, setStatus] = useState<HotelStatus | null>(null);
  const { hotelId } = useParams();

  useEffect(() => {
    async function fetchHotel() {
      if (!hotelId) return;
      try {
        const response = await getHotelForAdmin(Number(hotelId));
        setHotel(response.hotelDto);
        setStatus(response.hotelDto.hotelStatus as HotelStatus);
      } catch (error) {
        console.error("호텔 상세 정보를 불러오는 중 오류 발생:", error);
      }
    }
    fetchHotel();
  }, [hotelId]);

  if (!hotel) return <p>로딩 중...</p>;

  async function handleSave() {
    try {
      if (!hotelId || !status) return;

      const updatedData: AdminHotelRequest = {
        hotelStatus: status,
      };

      const response: HotelApprovalResult = await approveHotel(
        Number(hotelId),
        updatedData
      );
      alert(
        `호텔 상태가 저장되었습니다: ${response.name} - ${response.status}`
      );
      window.location.href = "/admin/hotels";
    } catch (error) {
      console.error("호텔 상태 저장 중 오류 발생:", error);
    }
  }

  return (
    <div>
      <h1>{hotel.hotelName}</h1>
      <p>
        {hotel.streetAddress}, {hotel.zipCode}
      </p>
      <p>등급: {hotel.hotelGrade}</p>
      <p>
        체크인: {hotel.checkInTime}, 체크아웃: {hotel.checkOutTime}
      </p>
      <p>설명: {hotel.hotelExplainContent}</p>
      <p>
        이메일: {hotel.hotelEmail}, 전화번호: {hotel.hotelPhoneNumber}
      </p>
      <label>
        상태 변경:
        <select
          value={status || ""}
          onChange={(e) => setStatus(e.target.value as HotelStatus)}
        >
          <option value="AVAILABLE">승인</option>
          <option value="PENDING">대기</option>
          <option value="UNAVAILABLE">거절</option>
        </select>
      </label>
      <button onClick={handleSave}>저장</button>
      <Link href="/admin/hotels">Back to List</Link>
    </div>
  );
}
