import { FetchOptions } from "@/lib/types/global/FetchOption";
import { AdminHotelRequest } from "@/lib/types/admin/request/AdminHotelRequest";
import { fetchAPI } from "../global/FetchApi";
import {
  AdminHotelDetailResponse,
  AdminHotelSummaryReponse,
  HotelApprovalResult,
} from "@/lib/types/admin/response/AdminHotelResponse";
import { PageDto } from "@/lib/types/PageDto";

export const getAllHotelsForAdmin = async (): Promise<
  PageDto<AdminHotelSummaryReponse>
> => {
  return fetchAPI<PageDto<AdminHotelSummaryReponse>>(
    "http://localhost:8080/api/admin/hotels"
  );
};

export const getHotelForAdmin = async (
  hotelId: number
): Promise<AdminHotelDetailResponse> => {
  return fetchAPI<AdminHotelDetailResponse>(
    `http://localhost:8080/api/admin/hotels/${hotelId}`
  );
};

export const approveHotel = async (
  hotelId: number,
  formData: AdminHotelRequest
): Promise<HotelApprovalResult> => {
  const options: FetchOptions = {
    method: "PATCH",
    body: formData,
  };

  return fetchAPI<HotelApprovalResult>(
    `http://localhost:8080/api/admin/hotels/${hotelId}`,
    options
  );
};
