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

    let rsData;

    try {
      rsData = await response.json();
    } catch {
      throw new Error(
        `JSON 파싱 오류: ${response.status} ${response.statusText}`
      );
    }

    if (
      !response.ok ||
      !rsData ||
      !["200", "201"].includes(rsData.resultCode)
    ) {
      throw { response: { status: response.status, data: rsData } };
    }

    return rsData.data as T;
  } catch (error) {
    console.error("API 요청 오류:", error);
    throw error;
  }
};
