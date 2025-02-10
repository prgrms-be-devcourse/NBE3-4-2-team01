"use client";

import { useForm } from "react-hook-form";
import { registerBusiness } from "@/lib/api/Business/BusinessRegisterApi";
import { BusinessRegistrationForm } from "@/lib/types/business/BusinessRequest";

export default function RegisterBusiness() {
  const {
    register, // 입력 필드를 React Hook Form과 연결
    handleSubmit, // form 제출 시 자동 검증 & 실행
    formState: { errors }, // 폼 유효성 검사 결과 (오류 저장)
  } = useForm<BusinessRegistrationForm>(); //폼 데이터의 타입 지정

  const onSubmit = async (data: BusinessRegistrationForm) => {
    try {
      const response = await registerBusiness(data);
      console.log("등록 성공:", response);
    } catch (error) {
      console.error("등록 실패:", error);
    }
  };

  // 반드시 하나의 JSX 요소만 반환, {}를 사용하여 JavaScript 변수 사용
  return (
    // 폼 제출 시 검증 실행
    <form onSubmit={handleSubmit(onSubmit)}>
      {/* input 필드는 register()를 통해 폼 상태와 연결 */}
      <input
        {...register("businessRegistrationNumber", {
          required: "사업자 등록 번호는 필수입니다.",
          pattern: {
            value: /^[0-9]{10}$/,
            message: "사업자 등록 번호는 10자리 숫자여야 합니다.",
          },
        })}
        placeholder="사업자 등록번호"
      />
      {/* 입력값이 잘못되면 자동으로 오류 메시지 표시, && 연산자는 왼쪽 값이 true이면 오른쪽 값을 반환 */}
      {errors.businessRegistrationNumber && (
        <p>{errors.businessRegistrationNumber.message}</p>
      )}

      <input
        type="date"
        {...register("startDate", { required: "개업 일자는 필수입니다." })}
      />
      {errors.startDate && <p>{errors.startDate.message}</p>}

      <input
        {...register("ownerName", {
          required: "대표자명은 필수입니다.",
          maxLength: { value: 30, message: "최대 30자까지 가능합니다." },
        })}
        placeholder="대표자명"
      />
      {errors.ownerName && <p>{errors.ownerName.message}</p>}

      <button type="submit">등록</button>
    </form>
  );
}
