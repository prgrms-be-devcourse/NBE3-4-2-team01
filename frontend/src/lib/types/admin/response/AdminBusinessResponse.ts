import { BusinessApprovalStatus } from "../../business/BusinessApprovalStatus";
import { MemberStatus } from "../../member/MemberStatus";
import { HotelStatus } from "../../hotel/HotelStatus";

export interface AdminBusinessApprovalReponse {
  businessId: number;
  businessRegistrationNumber: string;
  startDate: string; // LocalDate → ISO 8601 문자열 (예: "2025-02-07")
  ownerName: string;
  approvalStatus: BusinessApprovalStatus;
}

export interface AdminBusinessSummaryReponse {
  businessId: number;
  businessRegistrationNumber: string;
  ownerName: string;
  approvalStatus: BusinessApprovalStatus;
  hotelId?: number | null;
  hotelName?: string | null;
}

export interface AdminBusinessDetailResponse {
  businessId: number;
  businessRegistrationNumber: string;
  ownerName: string;
  approvalStatus: BusinessApprovalStatus;

  userId: number;
  name: string;
  email: string;
  phoneNumber: string;
  birth: string;
  memberStatus: MemberStatus;

  hotelId?: number | null;
  streetAddress?: string | null;
  hotelStatus?: HotelStatus | null;
}
