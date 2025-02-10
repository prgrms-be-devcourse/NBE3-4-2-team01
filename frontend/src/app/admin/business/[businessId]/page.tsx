"use client";

import Loading from "@/components/hotellist/Loading";
import Navigation from "@/components/navigation/Navigation";
import { Button } from "@/components/ui/button";
import { getBusiness, modifyBusiness } from "@/lib/api/Admin/AdminBusinessApi";
import { AdminBusinessRequest } from "@/lib/types/admin/request/AdminBusinessRequest";
import { AdminBusinessDetailResponse } from "@/lib/types/admin/response/AdminBusinessResponse";
import { BusinessApprovalStatus } from "@/lib/types/business/BusinessApprovalStatus";
import Link from "next/link";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";

export default function AdminBusinessDetailPage() {
  const { businessId } = useParams();
  const [business, setBusiness] = useState<AdminBusinessDetailResponse | null>(
    null
  );
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [status, setStatus] = useState<BusinessApprovalStatus | null>(null);

  useEffect(() => {
    if (!businessId) return;

    const fetchBusiness = async () => {
      try {
        const data = await getBusiness(Number(businessId));
        setBusiness(data);
        setStatus(data.approvalStatus as BusinessApprovalStatus);
      } catch (err) {
        setError((err as Error).message);
      } finally {
        setLoading(false);
      }
    };

    fetchBusiness();
  }, [businessId]);

  const handleSave = async () => {
    if (!businessId || !status) return;

    try {
      const updatedData: AdminBusinessRequest = {
        businessApprovalStatus: status,
      };

      await modifyBusiness(Number(businessId), updatedData);

      setBusiness((prev) =>
        prev ? { ...prev, approvalStatus: status } : prev
      );
      alert("승인 상태가 저장되었습니다.");
    } catch (err) {
      setError("사업자 상태를 저장하는 중 오류가 발생했습니다.");
    }
  };

  if (!businessId)
    return (
      <p className="text-red-500 text-center">
        Error: Business ID is missing from the URL.
      </p>
    );
  if (loading) return <Loading />;
  if (error) return <p className="text-center text-red-500">Error: {error}</p>;
  if (!business)
    return <p className="text-center text-gray-500">No business found</p>;

  return (
    <div className="min-h-screen bg-gray-100">
      <Navigation />

      <div className="max-w-3xl mx-auto pt-24 p-6">
        <h1 className="text-2xl font-bold mb-6 text-center">
          🏢 사업자 상세 정보
        </h1>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <p>
            <span className="font-bold">ID:</span> {business.businessId}
          </p>
          <p>
            <span className="font-bold">Owner Name:</span> {business.ownerName}
          </p>
          <p>
            <span className="font-bold">Email:</span> {business.email}
          </p>
          <p>
            <span className="font-bold">Phone:</span> {business.phoneNumber}
          </p>
          <p>
            <span className="font-bold">Hotel Name:</span>{" "}
            {business.hotelId ? business.hotelId : "N/A"}
          </p>
          <p>
            <span className="font-bold">Address:</span>{" "}
            {business.streetAddress || "N/A"}
          </p>
          <p>
            <span className="font-bold">Hotel Status:</span>{" "}
            {business.hotelStatus || "N/A"}
          </p>

          {/* 승인 상태 변경 UI (맨 아래로 이동) */}
          <div className="mt-4">
            <label className="font-bold">승인 상태 변경:</label>
            <select
              value={status ?? business.approvalStatus}
              onChange={(e) =>
                setStatus(e.target.value as BusinessApprovalStatus)
              }
              className="border p-2 rounded w-full mt-1"
            >
              <option value="PENDING">대기</option>
              <option value="APPROVED">승인</option>
              <option value="REJECTED">거절</option>
            </select>
          </div>

          {/* 저장 및 목록으로 이동 버튼 */}
          <div className="mt-4 flex gap-2">
            <Button onClick={handleSave} variant="default">
              저장
            </Button>
            <Link href="/admin/business">
              <Button variant="outline">Back to List</Button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
