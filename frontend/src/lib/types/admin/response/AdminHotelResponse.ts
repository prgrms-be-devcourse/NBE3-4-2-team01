import { HotelStatus } from "../../hotel/HotelStatus";
import { HotelDto } from "../../HotelDto";

export interface HotelApprovalResult {
  name: string;
  status: HotelStatus;
}

export interface AdminHotelSummaryReponse {
  hotelId: number;
  name: string;

  streetAddress: string;
  status: HotelStatus;

  ownerName: string;
}

export interface AdminHotelDetailResponse {
  hotelDto: HotelDto;

  ownerId: number;
  ownerName: string;
  businessRegistrationNumber: string;
  startDate: string;

  averageRating: number;
  totalReviewCount: number;
}
