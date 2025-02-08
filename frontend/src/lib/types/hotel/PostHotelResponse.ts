import { PresignedUrlsResponse } from "../PresignedUrlsResponse";

export interface PostHotelResponse {
  businessId: number;
  hotelId: number;
  hotelName: string;
  createdAt: string;
  urlsResponse: PresignedUrlsResponse;
}
