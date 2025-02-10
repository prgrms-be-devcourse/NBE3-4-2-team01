"use client";

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
  const [updatedData, setUpdatedData] = useState<Partial<AdminBusinessRequest>>(
    {}
  );

  useEffect(() => {
    if (!businessId) return;

    getBusiness(Number(businessId))
      .then(setBusiness)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [businessId]);

  const handleUpdate = async () => {
    if (!businessId) return;

    try {
      const updated = await modifyBusiness(
        Number(businessId),
        updatedData as AdminBusinessRequest
      );

      setBusiness((prev) => (prev ? { ...prev, ...updated } : prev));
      alert("승인 상태가 저장되었습니다.");
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "An unknown error occurred"
      );
    }
  };

  if (!businessId) return <p>Error: Business ID is missing from the URL.</p>;
  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!business) return <p>No business found</p>;

  return (
    <div>
      <h1>Business Details</h1>
      <p>ID: {business.businessId}</p>
      <p>Owner Name: {business.ownerName}</p>
      <p>Email: {business.email}</p>
      <p>Phone: {business.phoneNumber}</p>
      <p>
        Approval Status:
        <select
          value={updatedData.businessApprovalStatus ?? business.approvalStatus}
          onChange={(e) =>
            setUpdatedData({
              ...updatedData,
              businessApprovalStatus:
                BusinessApprovalStatus[
                  e.target.value as keyof typeof BusinessApprovalStatus
                ],
            })
          }
        >
          <option value="PENDING">대기</option>
          <option value="APPROVED">승인</option>
          <option value="REJECTED">거절</option>
        </select>
      </p>
      <p>Hotel Name: {business.hotelId ? business.hotelId : "N/A"}</p>
      <p>Address: {business.streetAddress || "N/A"}</p>
      <p>Hotel Status: {business.hotelStatus || "N/A"}</p>
      <button onClick={handleUpdate}>저장</button>
      <Link href="/admin/business">Back to List</Link>
    </div>
  );
}
