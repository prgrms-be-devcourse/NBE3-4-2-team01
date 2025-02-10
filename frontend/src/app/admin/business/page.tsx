"use client";

import Loading from "@/components/hotellist/Loading";
import Navigation from "@/components/navigation/Navigation";
import Pagination from "@/components/pagination/Pagination";
import { Button } from "@/components/ui/button";
import { getAllBusinesses } from "@/lib/api/Admin/AdminBusinessApi";
import { AdminBusinessSummaryReponse } from "@/lib/types/admin/response/AdminBusinessResponse";
import { PageDto } from "@/lib/types/PageDto";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
export default function AdminBusinessesPage() {
  const searchParams = useSearchParams();
  const pageParam = searchParams.get("page");
  const currentPage =
    pageParam !== null && !isNaN(Number(pageParam)) ? Number(pageParam) - 1 : 0;

  const [businesses, setBusinesses] =
    useState<PageDto<AdminBusinessSummaryReponse> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    console.log("현재 페이지:", currentPage);

    const fetchBusinesses = async () => {
      try {
        const data = await getAllBusinesses(currentPage);
        console.log("API에서 가져온 데이터:", data);
        setBusinesses(data);
      } catch (err) {
        setError((err as Error).message);
      } finally {
        setLoading(false);
      }
    };

    fetchBusinesses();
  }, [currentPage]);

  if (loading) return <Loading />;
  if (error) return <p className="text-center text-red-500">Error: {error}</p>;

  return (
    <div className="min-h-screen bg-gray-100">
      <Navigation />

      <div className="max-w-4xl mx-auto pt-24 p-6">
        <h1 className="text-2xl font-bold mb-6 text-center">🏢 사업자 관리</h1>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <ul className="space-y-4">
            {businesses?.items.map((business) => (
              <li
                key={business.businessId}
                className="p-4 bg-gray-50 rounded-lg shadow-sm border"
              >
                <p>
                  <span className="font-bold">ID:</span> {business.businessId}
                </p>
                <p>
                  <span className="font-bold">Owner Name:</span>{" "}
                  {business.ownerName}
                </p>
                <p>
                  <span className="font-bold">Approval Status:</span>{" "}
                  {business.approvalStatus}
                </p>
                <p>
                  <span className="font-bold">Hotel:</span>{" "}
                  {business.hotelName || "N/A"}
                </p>
                <Link href={`/admin/business/${business.businessId}`}>
                  <Button variant="outline" className="mt-2">
                    View Details
                  </Button>
                </Link>
              </li>
            ))}
          </ul>
        </div>

        {/* 페이지네이션 추가 */}
        {businesses && businesses.totalPages > 1 && (
          <Pagination
            currentPage={currentPage + 1}
            totalPages={businesses.totalPages}
            basePath="/admin/businesses"
          />
        )}
      </div>
    </div>
  );
}
