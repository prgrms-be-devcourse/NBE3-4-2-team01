import { HotelStatus } from "../../hotel/HotelStatus";
import { HotelDto } from "../../HotelDto";

export interface HotelApprovalResult {
  name: string;
  status: HotelStatus;
}

export interface AdminHotelSummaryReponse {
  id: number;
  name: string;
  streetAddress: string;
  zipCode: number;
  grade: number;
  status: HotelStatus;
  averageRating: number;
  totalReviewRatingSum: number;
  totalReviewCount: number;
  ownerName: string;
  ownerEmail: string;
  ownerPhoneNumber: string;
}

export interface AdminHotelDetailResponse {
  hotelDto: HotelDto;

  ownerId: number;
  ownerName: string;
  ownerEmail: string;
  ownerPhoneNumber: string;

  averageRating: number;
  totalReviewRatingSum: number;
  totalReviewCount: number;
}
