import { FetchOptions } from "../../types/global/FetchOption";

export const fetchAPI = async <T>(
  url: string,
  options: FetchOptions = {}
): Promise<T> => {
  try {
    const response = await fetch(url, {
      method: options.method || "GET",
      headers: {
        "Content-Type": "application/json",
        ...options.headers, // 추가 헤더 포함 가능
      },
      credentials: "include",
      body: options.body ? JSON.stringify(options.body) : null,
    });

    if (![200, 201].includes(response.status)) {
      throw new Error(`HTTP 오류: ${response.status} ${response.statusText}`);
    }

    const rsData = await response.json();
    if (!rsData || !["200", "201"].includes(rsData.resultCode)) {
      throw new Error(rsData?.msg || "서버에서 올바른 응답을 받지 못했습니다.");
    }

    return rsData.data as T;
  } catch (error) {
    console.error("API 요청 오류:", error);
    throw error;
  }
};
