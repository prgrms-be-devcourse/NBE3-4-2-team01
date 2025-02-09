import { Empty } from "../types/Empty";
import { GetAllHotelOptionResponse } from "../types/hotel/GetAllHotelOptionResponse";
import { GetHotelDetailResponse } from "../types/hotel/GetHotelDetailResponse";
import { GetHotelRevenueResponse } from "../types/hotel/GetHotelRevenueResponse";
import { PostHotelResponse } from "../types/hotel/PostHotelResponse";
import { PutHotelRequest } from "../types/hotel/PutHotelRequest";
import { PutHotelResponse } from "../types/hotel/PutHotelResponse";
import { PostHotelRequest } from "./../types/hotel/PostHotelRequest";
import { RsData } from "./../types/RsData";

const BASE_URL = "http://localhost:8080/api/hotels";

// 호텔 생성
export const createHotel = async (
  postHotelRequest: PostHotelRequest
): Promise<PostHotelResponse> => {
  try {
    const response = await fetch(`${BASE_URL}`, {
      credentials: "include",
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(postHotelRequest),
    });

    const rsData: RsData<PostHotelResponse> = await response.json();
    if (rsData.resultCode !== "201-1") {
      throw new Error(rsData.msg);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};

// 호텔 이미지 URL 저장
export const saveHotelImageUrls = async (
  hotelId: number,
  urls: string[]
): Promise<void> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}/urls`, {
      credentials: "include",
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(urls),
    });

    const rsData: RsData<Empty> = await response.json();
    if (rsData.resultCode !== "201-1") {
      throw new Error(rsData.msg);
    }
  } catch (error) {
    throw error;
  }
};

// 호텔 상세 조회
export const findHotelDetail = async (
  hotelId: number
): Promise<GetHotelDetailResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}`);
    const rsData: RsData<GetHotelDetailResponse> = await response.json();

    if (rsData.resultCode !== "200-1") {
      throw new Error(rsData.msg);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};

// 호텔 수정
export const modifyHotel = async (
  hotelId: number,
  request: PutHotelRequest
): Promise<PutHotelResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}`, {
      credentials: "include",
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    });

    const rsData: RsData<PutHotelResponse> = await response.json();
    if (rsData.resultCode !== "200-1") {
      throw new Error(rsData.msg);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};

// 호텔 삭제
export const deleteHotel = async (hotelId: number): Promise<void> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}`, {
      credentials: "include",
      method: "DELETE",
    });

    const rsData: RsData<Empty> = await response.json();
    if (rsData.resultCode !== "200-1") {
      throw new Error(rsData.msg);
    }
  } catch (error) {
    throw error;
  }
};

// 호텔 매출 조회
export const findHotelRevenue = async (
  hotelId: number
): Promise<GetHotelRevenueResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}/revenue`);
    const rsData: RsData<GetHotelRevenueResponse> = await response.json();

    if (rsData.resultCode !== "200-1") {
      throw new Error(rsData.msg);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};

// 전체 호텔 옵션 조회
export const findAllHotelOptions =
  async (): Promise<GetAllHotelOptionResponse> => {
    try {
      const response = await fetch(`${BASE_URL}/hotel-option`, {
        credentials: "include",
      });
      const rsData: RsData<GetAllHotelOptionResponse> = await response.json();

      if (rsData.resultCode !== "200-1") {
        throw new Error(rsData.msg);
      }

      return rsData.data;
    } catch (error) {
      throw error;
    }
  };
