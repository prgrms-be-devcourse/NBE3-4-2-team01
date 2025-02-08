import { PresignedUrlsResponse } from "../PresignedUrlsResponse";

export interface PostRoomResponse {
  roomId: number;
  hotelId: number;
  roomName: string;
  basePrice: number;
  standardNumber: number;
  maxNumber: number;
  createdAt: string;
  urlsResponse: PresignedUrlsResponse;
}
