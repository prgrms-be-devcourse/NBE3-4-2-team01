import { OptionResponse } from "@/lib/types/admin/response/OptionResponse";
import { fetchAPI } from "../global/FetchApi";
import { OptionRequest } from "@/lib/types/admin/request/OptionRequest";
import { FetchOptions } from "@/lib/types/global/FetchOption";

export const addHotelOption = async (
  formData: OptionRequest
): Promise<OptionResponse> => {
  const options: FetchOptions = {
    method: "POST",
    body: formData,
  };

  return fetchAPI<OptionResponse>(
    `http://localhost:8080/api/admin/hotel-options`,
    options
  );
};

export const getAllHotelOptions = async (): Promise<OptionResponse[]> => {
  return fetchAPI<OptionResponse[]>(
    "http://localhost:8080/api/admin/hotel-options"
  );
};

export const getHotelOption = async (
  hotelOptionId: number
): Promise<OptionResponse> => {
  return fetchAPI<OptionResponse>(
    `http://localhost:8080/api/admin/hotel-options/${hotelOptionId}`
  );
};

export const modifyHotelOption = async (
  hotelOptionId: number,
  formData: OptionRequest
): Promise<OptionResponse> => {
  const options: FetchOptions = {
    method: "PATCH",
    body: formData,
  };

  return fetchAPI<OptionResponse>(
    `http://localhost:8080/api/admin/hotel-options/${hotelOptionId}`,
    options
  );
};

export const deleteHotelOption = async (
  hotelOptionId: number
): Promise<void> => {
  const options: FetchOptions = {
    method: "DELETE",
  };

  return fetchAPI<void>(
    `http://localhost:8080/api/admin/hotel-options/${hotelOptionId}`
  );
};
