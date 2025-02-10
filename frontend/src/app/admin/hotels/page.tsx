"use client";

import Loading from "@/components/hotellist/Loading";
import Navigation from "@/components/navigation/Navigation";
import Pagination from "@/components/pagination/Pagination";
import { Button } from "@/components/ui/button";
import { getAllHotelsForAdmin } from "@/lib/api/Admin/AdminHotelApi";
import { AdminHotelSummaryReponse } from "@/lib/types/admin/response/AdminHotelResponse";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";

export default function AdminHotelsPage() {
  const searchParams = useSearchParams();
  const pageParam = searchParams.get("page");
  const currentPage =
    pageParam !== null && !isNaN(Number(pageParam)) ? Number(pageParam) - 1 : 0;

  const [hotels, setHotels] = useState<AdminHotelSummaryReponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    console.log("현재 페이지:", currentPage);

    const fetchHotels = async () => {
      try {
        const data = await getAllHotelsForAdmin(currentPage);
        console.log("API에서 가져온 데이터:", data);
        setHotels(data.items ?? []);
      } catch (err) {
        console.error("호텔 데이터를 불러오는 중 오류 발생:", err);
        setError("호텔 데이터를 불러오는 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchHotels();
  }, [currentPage]);

  if (loading) return <Loading />;
  if (error) return <p className="text-center text-red-500">Error: {error}</p>;

  return (
    <div className="min-h-screen bg-gray-100">
      <Navigation />

      <div className="max-w-4xl mx-auto pt-24 p-6">
        <h1 className="text-2xl font-bold mb-6 text-center">🏨 호텔 목록</h1>

        <div className="bg-white p-6 rounded-lg shadow-md">
          {hotels.length === 0 ? (
            <p className="text-center text-gray-500">등록된 호텔이 없습니다.</p>
          ) : (
            <ul className="space-y-4">
              {hotels.map((hotel) => (
                <li
                  key={hotel.id}
                  className="p-4 bg-gray-50 rounded-lg shadow-sm border"
                >
                  <p>
                    <span className="font-bold">이름:</span> {hotel.name}
                  </p>
                  <p>
                    <span className="font-bold">주소:</span>{" "}
                    {hotel.streetAddress}, {hotel.zipCode}
                  </p>
                  <p>
                    <span className="font-bold">등급:</span> {hotel.grade}등급
                  </p>
                  <p>
                    <span className="font-bold">평균 평점:</span>{" "}
                    {hotel.averageRating}점
                  </p>

                  <div className="mt-2">
                    <Link href={`/admin/hotels/${hotel.id}`}>
                      <Button variant="default">세부 내역 보기</Button>
                    </Link>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>

        {/* 페이지네이션 추가 */}
        {hotels.length > 0 && (
          <Pagination
            currentPage={currentPage + 1}
            totalPages={Math.ceil(hotels.length / 10)}
            basePath="/admin/hotels"
          />
        )}
      </div>
    </div>
  );
}
