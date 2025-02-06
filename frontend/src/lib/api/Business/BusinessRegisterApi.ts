import { BusinessRegistrationForm } from "@/lib/types/business/BusinessRegistrationForm";

export const registerBusiness = async (formData: BusinessRegistrationForm) => {
  const response = await fetch(
    `http://localhost:8080/api/businesses/register`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${process.env.ACCESS_TOKEN}`, // JWT 토큰, 수정 필요
      },
      body: JSON.stringify(formData), // JSON.stringify : 객체를 JSON 으로 변환
    }
  );

  if (!response.ok) {
    throw new Error(`HTTP 오류: ${response.status} ${response.statusText}`);
  }

  const rsData = await response.json();

  if (rsData.resultCode !== "201") {
    throw new Error(rsData.msg);
  }

  return rsData;
};
