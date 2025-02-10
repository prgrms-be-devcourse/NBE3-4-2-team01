"use client";

import { approveHotel, getHotelForAdmin } from "@/lib/api/Admin/AdminHotelApi";
import { AdminHotelRequest } from "@/lib/types/admin/request/AdminHotelRequest";
import { HotelStatus } from "@/lib/types/hotel/HotelStatus";
import { HotelDto } from "@/lib/types/HotelDto";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import Link from "next/link";
import { HotelApprovalResult } from "@/lib/types/admin/response/AdminHotelResponse";
import { Button } from "@/components/ui/button";
import Navigation from "@/components/navigation/Navigation";
import Loading from "@/components/hotellist/Loading";

export default function AdminHotelDetailPage() {
  const { hotelId } = useParams();
  const [hotel, setHotel] = useState<HotelDto | null>(null);
  const [status, setStatus] = useState<HotelStatus | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchHotel = async () => {
      if (!hotelId) return;
      try {
        const response = await getHotelForAdmin(Number(hotelId));
        setHotel(response.hotelDto);
        setStatus(response.hotelDto.hotelStatus as HotelStatus);
      } catch (error) {
        console.error("호텔 상세 정보를 불러오는 중 오류 발생:", error);
        setError("호텔 데이터를 불러오는 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchHotel();
  }, [hotelId]);

  const handleSave = async () => {
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
      setError("호텔 상태를 저장하는 중 오류가 발생했습니다.");
    }
  };

  if (!hotelId)
    return (
      <p className="text-red-500 text-center">
        Error: Hotel ID is missing from the URL.
      </p>
    );
  if (loading) return <Loading />;
  if (error) return <p className="text-center text-red-500">Error: {error}</p>;
  if (!hotel)
    return <p className="text-center text-gray-500">No hotel found</p>;

  return (
    <div className="min-h-screen bg-gray-100">
      <Navigation />

      <div className="max-w-3xl mx-auto pt-24 p-6">
        <h1 className="text-2xl font-bold mb-6 text-center">
          🏨 호텔 상세 정보
        </h1>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <p>
            <span className="font-bold">이름:</span> {hotel.hotelName}
          </p>
          <p>
            <span className="font-bold">주소:</span> {hotel.streetAddress},{" "}
            {hotel.zipCode}
          </p>
          <p>
            <span className="font-bold">등급:</span> {hotel.hotelGrade}등급
          </p>
          <p>
            <span className="font-bold">체크인:</span> {hotel.checkInTime},
            <span className="ml-2 font-bold">체크아웃:</span>{" "}
            {hotel.checkOutTime}
          </p>
          <p>
            <span className="font-bold">설명:</span> {hotel.hotelExplainContent}
          </p>
          <p>
            <span className="font-bold">이메일:</span> {hotel.hotelEmail}
          </p>
          <p>
            <span className="font-bold">전화번호:</span>{" "}
            {hotel.hotelPhoneNumber}
          </p>

          {/* 호텔 상태 변경 */}
          <div className="mt-4">
            <label className="font-bold">상태 변경:</label>
            <select
              value={status || ""}
              onChange={(e) => setStatus(e.target.value as HotelStatus)}
              className="border p-2 rounded w-full mt-1"
            >
              <option value="AVAILABLE">승인</option>
              <option value="PENDING">대기</option>
              <option value="UNAVAILABLE">거절</option>
            </select>
          </div>

          {/* 저장 및 목록으로 이동 버튼 */}
          <div className="mt-4 flex gap-2">
            <Button onClick={handleSave} variant="default">
              저장
            </Button>
            <Link href="/admin/hotels">
              <Button variant="outline">Back to List</Button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
