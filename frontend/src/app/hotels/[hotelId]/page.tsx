"use client";

import HotelDetail from "@/components/business/hotel/HotelDetail";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { findHotelDetail } from "@/lib/api/BusinessHotelApi";
import { GetHotelDetailResponse } from "@/lib/types/hotel/GetHotelDetailResponse";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import RoomList from "@/components/business/room/RoomList";
import HotelImages from "@/components/business/hotel/HotelImages";

const HotelDetailPage: React.FC = () => {
  const { hotelId } = useParams();
  const [hotelDetail, setHotelDetail] = useState<GetHotelDetailResponse | null>(
    null
  );
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchHotelDetail = async () => {
      try {
        const response = await findHotelDetail(Number(hotelId));
        console.log(response);
        setHotelDetail(response);
      } catch (error) {
        throw error;
      } finally {
        setIsLoading(false);
      }
    };

    fetchHotelDetail();
  }, [hotelId]);

  if (isLoading) {
    return <div className="text-center">로딩 중...</div>;
  }

  if (!hotelDetail) {
    return <div className="text-center">호텔 정보를 불러올 수가 없습니다.</div>;
  }

  return (
    <div className="p-4">
      <Card>
        <CardHeader>
          <CardTitle>{hotelDetail.hotelDetailDto.hotelName}</CardTitle>
        </CardHeader>
        <CardContent>
          <HotelImages images={hotelDetail.hotelImageUrls} />
          <HotelDetail hotel={hotelDetail.hotelDetailDto} />
          <RoomList rooms={hotelDetail.hotelDetailDto.rooms} />
        </CardContent>
      </Card>
    </div>
  );
};

export default HotelDetailPage;
