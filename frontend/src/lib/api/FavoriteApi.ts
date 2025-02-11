import { RsData } from "../types/RsData";
import { Empty } from "../types/Empty";
import { HotelDto } from "../types/HotelDto";

// 즐겨찾기 목록 조회
export const getFavorites = async (): Promise<RsData<HotelDto[]>> => {
  // 호텔 목록 배열로 반환
  try {
    const response = await fetch("http://localhost:8080/api/favorites/me", {
      credentials: "include", // 쿠키 포함
    });
    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.msg || "서버 오류가 발생했습니다.");
    }

    if (data.resultCode !== "200-1") {
      throw new Error(data.msg);
    }

    return data;
  } catch (error) {
    throw error;
  }
};

// 즐겨찾기 여부 확인
export const checkFavorite = async (hotelId: number): Promise<boolean> => {
  // 호텔 목록 배열로 반환
  try {
    const response = await fetch(
      `http://localhost:8080/api/favorites/me/${hotelId}`,
      {
        credentials: "include", // 쿠키 포함
      }
    );
    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.msg || "서버 오류가 발생했습니다.");
    }

    if (data.resultCode !== "200-1") {
      throw new Error(data.msg);
    }

    return data.data;
  } catch (error) {
    throw error;
  }
};

// 즐겨찾기 추가
export const addFavorite = async (hotelId: number): Promise<RsData<Empty>> => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/favorites/${hotelId}`,
      {
        method: "POST",
        credentials: "include",
      }
    );
    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.msg || "서버 오류가 발생했습니다.");
    }

    if (data.resultCode !== "200-1") {
      throw new Error(data.msg);
    }

    return data;
  } catch (error) {
    throw error;
  }
};

// 즐겨찾기 삭제
export const removeFavorite = async (
  hotelId: number
): Promise<RsData<Empty>> => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/favorites/${hotelId}`,
      {
        method: "DELETE",
        credentials: "include",
      }
    );
    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.msg || "서버 오류가 발생했습니다.");
    }

    if (data.resultCode !== "200-1") {
      throw new Error(data.msg);
    }

    return data;
  } catch (error) {
    throw error;
  }
};
