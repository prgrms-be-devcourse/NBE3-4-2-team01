import { PresignedUrlsResponse } from "../PresignedUrlsResponse";

export interface PutRoomResponse {
  hotelId: number;
  roomId: number;
  roomName: string;
  roomStatus: string;
  modifiedAt: string;
  urlsResponse: PresignedUrlsResponse;
}
