"use client";

import { useForm } from "react-hook-form";
import { registerBusiness } from "@/lib/api/Business/BusinessRegisterApi";
import { BusinessRegistrationForm } from "@/lib/types/business/BusinessRequest";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import Loading from "@/components/hotellist/Loading";

export default function RegisterBusiness() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<BusinessRegistrationForm>();

  const onSubmit = async (data: BusinessRegistrationForm) => {
    try {
      await registerBusiness(data);
      alert("사업자 등록이 완료되었습니다!");
    } catch (error) {
      console.error("등록 실패:", error);
      alert("사업자 등록에 실패했습니다.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="w-full max-w-md bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6 text-center">🏢 사업자 등록</h2>

        {isSubmitting ? (
          <Loading />
        ) : (
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            {/* 사업자 등록번호 입력 */}
            <div>
              <label className="block font-medium">사업자 등록번호</label>
              <Input
                {...register("businessRegistrationNumber", {
                  required: "사업자 등록 번호는 필수입니다.",
                  pattern: {
                    value: /^[0-9]{10}$/,
                    message: "사업자 등록 번호는 10자리 숫자여야 합니다.",
                  },
                })}
                placeholder="사업자 등록번호"
              />
              {errors.businessRegistrationNumber && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.businessRegistrationNumber.message}
                </p>
              )}
            </div>

            {/* 개업 일자 입력 */}
            <div>
              <label className="block font-medium">개업 일자</label>
              <Input
                type="date"
                {...register("startDate", {
                  required: "개업 일자는 필수입니다.",
                })}
              />
              {errors.startDate && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.startDate.message}
                </p>
              )}
            </div>

            {/* 대표자명 입력 */}
            <div>
              <label className="block font-medium">대표자명</label>
              <Input
                {...register("ownerName", {
                  required: "대표자명은 필수입니다.",
                  maxLength: {
                    value: 30,
                    message: "최대 30자까지 가능합니다.",
                  },
                })}
                placeholder="대표자명"
              />
              {errors.ownerName && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.ownerName.message}
                </p>
              )}
            </div>

            {/* 등록 버튼 */}
            <Button type="submit" className="w-full" disabled={isSubmitting}>
              {isSubmitting ? "등록 중..." : "등록"}
            </Button>
          </form>
        )}
      </div>
    </div>
  );
}
