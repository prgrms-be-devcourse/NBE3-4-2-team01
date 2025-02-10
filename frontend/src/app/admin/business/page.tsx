"use client";

import { getAllBusinesses } from "@/lib/api/Admin/AdminBusinessApi";
import { AdminBusinessSummaryReponse } from "@/lib/types/admin/response/AdminBusinessResponse";
import { PageDto } from "@/lib/types/PageDto";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function AdminBusinessesPage() {
  const [businesses, setBusinesses] =
    useState<PageDto<AdminBusinessSummaryReponse> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getAllBusinesses()
      .then((data) => setBusinesses(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <h1>Business List</h1>
      <ul>
        {businesses?.items.map((business) => (
          <li key={business.businessId}>
            <p>ID: {business.businessId}</p>
            <p>Owner Name: {business.ownerName}</p>
            <p>Approval Status: {business.approvalStatus}</p>
            <p>Hotel: {business.hotelName || "N/A"}</p>
            <Link href={`/admin/business/${business.businessId}`}>
              View Details
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
