import { GetRoomRevenueResponse } from "../Room/GetRoomRevenueResponse";

export interface GetHotelRevenueResponse {
  roomRevenueResponse: GetRoomRevenueResponse[];
  revenue: number;
}
