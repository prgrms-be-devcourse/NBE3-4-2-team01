"use client";

import { useEffect, useState } from "react";
import { GetRoomRevenueResponse } from "@/lib/types/room/GetRoomRevenueResponse";
import { findHotelRevenue } from "@/lib/api/BusinessHotelApi";
import { getRoleFromCookie } from "@/lib/utils/CookieUtil";
import { useRouter } from "next/navigation";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import Navigation from "@/components/navigation/Navigation";

interface HotelRevenueData {
  roomRevenues: GetRoomRevenueResponse[];
  hotelRevenue: number;
}

const HotelRevenue = () => {
  const cookie = getRoleFromCookie();
  const router = useRouter();
  const [revenueData, setRevenueData] = useState<HotelRevenueData | null>(null);
  const [hotelId, setHotelId] = useState(-1);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (cookie?.hotelId) {
      setHotelId(Number(cookie.hotelId));
    }
  }, [cookie]);

  useEffect(() => {
    const fetchHotelRevenue = async () => {
      try {
        const response = await findHotelRevenue(hotelId);
        const resultData: HotelRevenueData = {
          roomRevenues: response.roomRevenueResponse,
          hotelRevenue: response.revenue,
        };
        setRevenueData(resultData);
      } catch (error) {
        console.error("매출 데이터를 불러오는 중 오류 발생:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchHotelRevenue();
  }, [hotelId]);

  if (isLoading) {
    return (
      <p className="text-center text-gray-600 mt-10 text-xl font-semibold">
        🔄 매출 데이터를 불러오는 중...
      </p>
    );
  }

  if (!revenueData || revenueData.roomRevenues.length === 0) {
    return (
      <div className="p-8 pt-[100px]">
        <Navigation />
        <h2 className="text-4xl font-bold text-center text-blue-600 mb-6">
          호텔 매출 조회
        </h2>
        <p className="text-center text-red-500 mt-10 text-xl font-semibold">
          ⚠️ 매출 데이터가 없습니다.
        </p>
      </div>
    );
  }

  return (
    <div className="p-8 pt-[100px]">
      <Navigation />
      <h2 className="text-4xl font-bold text-center text-blue-600 mb-6">
        호텔 매출 조회
      </h2>

      {/* 총 매출 카드 */}
      <Card className="mb-8">
        <CardHeader>
          <CardTitle className="text-3xl text-center">총 매출</CardTitle>
        </CardHeader>
        <CardContent className="text-center">
          <p className="text-5xl font-extrabold text-green-500">
            {revenueData.hotelRevenue.toLocaleString()}원
          </p>
        </CardContent>
      </Card>

      {/* 차트 */}
      <div className="bg-white p-6 shadow-md rounded-lg mb-8">
        <h3 className="text-2xl font-semibold mb-4">객실별 매출 차트</h3>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={revenueData.roomRevenues}>
            <XAxis dataKey="roomName" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="roomRevenue" fill="#4F46E5" />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* 객실별 매출 리스트 */}
      <div className="space-y-4">
        {revenueData.roomRevenues.map((room) => (
          <Card key={room.roomId}>
            <CardContent className="flex justify-between items-center p-6">
              <div>
                <h4 className="text-xl font-semibold">{room.roomName}</h4>
                <p className="text-gray-600">
                  기본 가격: {room.basePrice.toLocaleString()}원
                </p>
              </div>
              <p className="text-2xl font-bold text-green-600">
                {room.roomRevenue.toLocaleString()}원
              </p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default HotelRevenue;
