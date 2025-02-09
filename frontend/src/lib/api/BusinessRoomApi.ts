import { Empty } from "../types/Empty";
import { GetAllRoomOptionsResponse } from "../types/Room/GetAllRoomOptionsResponse";
import { GetRoomDetailResponse } from "../types/Room/GetRoomDetailResponse";
import { GetRoomResponse } from "../types/Room/GetRoomResponse";
import { PostRoomResponse } from "../types/Room/PostRoomResponse";
import { PutRoomRequest } from "../types/Room/PutRoomRequest";
import { PutRoomResponse } from "../types/Room/PutRoomResponse";
import { RsData } from "../types/RsData";
import { PostRoomRequest } from "./../types/Room/PostRoomRequest";

const BASE_URL = "http://localhost:8080/api/hotels";

// 객실 생성
export const createRoom = async (
  hotelId: number,
  postRoomRequest: PostRoomRequest
): Promise<PostRoomResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}/rooms`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(postRoomRequest),
    });

    const rsData: RsData<PostRoomResponse> = await response.json();
    if (rsData.resultCode !== "201-1") {
      throw new Error(rsData.msg);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};

// 객실 이미지 저장
export const saveRoomImageUrls = async (
  hotelId: number,
  roomId: number,
  urls: string[]
): Promise<void> => {
  try {
    const response = await fetch(
      `${BASE_URL}/${hotelId}/rooms/${roomId}/urls`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(urls),
      }
    );

    const rsData: RsData<Empty> = await response.json();
    if (rsData.resultCode !== "200-1") {
      throw new Error(rsData.msg);
    }
  } catch (error) {
    throw error;
  }
};

// 객실 상세 조회
export const findRoomDetail = async (
  hotelId: number,
  roomId: number
): Promise<GetRoomDetailResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}/rooms/${roomId}`);
    const rsData: RsData<GetRoomDetailResponse> = await response.json();

    if (rsData.resultCode !== "200-1") {
      throw new Error(rsData.msg);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};

// 객실 수정
export const modifyRoom = async (
  hotelId: number,
  roomId: number,
  request: PutRoomRequest
): Promise<PutRoomResponse> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}/rooms/${roomId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    });

    const rsData: RsData<PutRoomResponse> = await response.json();
    if (rsData.resultCode !== "200-1") {
      throw new Error(rsData.msg);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};

// 객실 삭제
export const deletRoom = async (
  hotelId: number,
  roomId: number
): Promise<void> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}/rooms/${roomId}`, {
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

// 전체 객실 조회
export const findAllRooms = async (
  hotelId: number
): Promise<GetRoomResponse[]> => {
  try {
    const response = await fetch(`${BASE_URL}/${hotelId}/rooms`);
    const rsData: RsData<GetRoomResponse[]> = await response.json();

    if (rsData.resultCode !== "200-1") {
      throw new Error(rsData.msg);
    }

    return rsData.data;
  } catch (error) {
    throw error;
  }
};

// 전체 객실 옵션 조회
export const findAllRoomOptions =
  async (): Promise<GetAllRoomOptionsResponse> => {
    try {
      const response = await fetch(`${BASE_URL}/hotel-option`);
      const rsData: RsData<GetAllRoomOptionsResponse> = await response.json();

      if (rsData.resultCode !== "200-1") {
        throw new Error(rsData.msg);
      }

      return rsData.data;
    } catch (error) {
      throw error;
    }
  };
