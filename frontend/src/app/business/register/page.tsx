"use client";

import { useForm } from "react-hook-form";
import { registerBusiness } from "@/lib/api/Business/BusinessRegisterApi";
import { BusinessRegistrationForm } from "@/lib/types/business/BusinessRequest";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import Loading from "@/components/hotellist/Loading";
import { Building2, Calendar, User } from "lucide-react";
import Navigation from "@/components/navigation/Navigation";
import { Card, CardContent } from "@/components/ui/card";

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

  const inputStyle =
    "bg-white h-[42px] px-10 text-lg placeholder:text-lg [&::-webkit-calendar-picker-indicator]:hidden w-full";

  return (
    <div className="relative min-h-screen bg-background">
      {/* Background gradient */}
      <div className="absolute inset-0 bg-gradient-to-b from-blue-100 to-white" />

      {/* Decorative circles */}
      <div className="absolute top-20 right-20 w-64 h-64 bg-blue-200 rounded-full blur-3xl opacity-20" />
      <div className="absolute bottom-20 left-20 w-96 h-96 bg-blue-300 rounded-full blur-3xl opacity-10" />

      <div className="relative z-10">
        <Navigation />

        <div className="container mx-auto px-4 pt-32">
          <div className="text-center mb-16">
            <h1 className="text-4xl font-bold text-gray-800 mb-4">
              사업자 등록
            </h1>
            <p className="text-lg text-gray-600">
              호텔 등록을 위한 첫 단계를 시작해보세요
            </p>
          </div>

          <div className="max-w-xl mx-auto">
            {isSubmitting ? (
              <div className="flex justify-center">
                <Loading />
              </div>
            ) : (
              <Card className="bg-white/50 shadow-lg">
                <CardContent className="p-10">
                  <form onSubmit={handleSubmit(onSubmit)} className="space-y-8">
                    {/* 사업자 등록번호 입력 */}
                    <div className="space-y-3">
                      <div className="flex items-center gap-3">
                        <Building2 className="w-5 h-5 text-blue-500" />
                        <label className="block font-medium text-gray-800 text-lg">
                          사업자 등록번호
                        </label>
                      </div>
                      <div className="relative">
                        <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500 pointer-events-none">
                          <Building2 size={20} className="text-gray-500" />
                        </div>
                        <Input
                          {...register("businessRegistrationNumber", {
                            required: "사업자 등록 번호는 필수입니다.",
                            pattern: {
                              value: /^[0-9]{10}$/,
                              message:
                                "사업자 등록 번호는 10자리 숫자여야 합니다.",
                            },
                          })}
                          placeholder="사업자 등록번호 (10자리)"
                          className={`${inputStyle} ${
                            errors?.businessRegistrationNumber
                              ? "border-red-500"
                              : ""
                          }`}
                        />
                        {errors.businessRegistrationNumber && (
                          <div className="absolute z-20 w-full -top-12">
                            <div className="relative bg-black/80 rounded-lg p-2 text-white">
                              <div
                                className="absolute bottom-[-6px] left-5 
                                border-l-[6px] border-r-[6px] border-t-[6px] 
                                border-l-transparent border-r-transparent border-t-black/80"
                              ></div>
                              <div className="relative z-10 flex items-center gap-2 text-sm">
                                <Building2 size={16} className="text-white" />
                                {errors.businessRegistrationNumber.message}
                              </div>
                            </div>
                          </div>
                        )}
                      </div>
                    </div>

                    {/* 개업 일자 입력 */}
                    <div className="space-y-3">
                      <div className="flex items-center gap-3">
                        <Calendar className="w-5 h-5 text-blue-500" />
                        <label className="block font-medium text-gray-800 text-lg">
                          개업 일자
                        </label>
                      </div>
                      <div className="relative">
                        <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500 pointer-events-none">
                          <Calendar size={20} className="text-gray-500" />
                        </div>
                        <Input
                          type="date"
                          {...register("startDate", {
                            required: "개업 일자는 필수입니다.",
                          })}
                          className={`${inputStyle} ${
                            errors?.startDate ? "border-red-500" : ""
                          }`}
                        />
                        {errors.startDate && (
                          <div className="absolute z-20 w-full -top-12">
                            <div className="relative bg-black/80 rounded-lg p-2 text-white">
                              <div
                                className="absolute bottom-[-6px] left-5 
                                border-l-[6px] border-r-[6px] border-t-[6px] 
                                border-l-transparent border-r-transparent border-t-black/80"
                              ></div>
                              <div className="relative z-10 flex items-center gap-2 text-sm">
                                <Calendar size={16} className="text-white" />
                                {errors.startDate.message}
                              </div>
                            </div>
                          </div>
                        )}
                      </div>
                    </div>

                    {/* 대표자명 입력 */}
                    <div className="space-y-3">
                      <div className="flex items-center gap-3">
                        <User className="w-5 h-5 text-blue-500" />
                        <label className="block font-medium text-gray-800 text-lg">
                          대표자명
                        </label>
                      </div>
                      <div className="relative">
                        <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500 pointer-events-none">
                          <User size={20} className="text-gray-500" />
                        </div>
                        <Input
                          {...register("ownerName", {
                            required: "대표자명은 필수입니다.",
                            maxLength: {
                              value: 30,
                              message: "최대 30자까지 가능합니다.",
                            },
                          })}
                          placeholder="대표자명을 입력해주세요"
                          className={`${inputStyle} ${
                            errors?.ownerName ? "border-red-500" : ""
                          }`}
                        />
                        {errors.ownerName && (
                          <div className="absolute z-20 w-full -top-12">
                            <div className="relative bg-black/80 rounded-lg p-2 text-white">
                              <div
                                className="absolute bottom-[-6px] left-5 
                                border-l-[6px] border-r-[6px] border-t-[6px] 
                                border-l-transparent border-r-transparent border-t-black/80"
                              ></div>
                              <div className="relative z-10 flex items-center gap-2 text-sm">
                                <User size={16} className="text-white" />
                                {errors.ownerName.message}
                              </div>
                            </div>
                          </div>
                        )}
                      </div>
                    </div>

                    {/* 등록 버튼 */}
                    <div className="pt-4">
                      <Button
                        type="submit"
                        className="w-full bg-blue-500 hover:bg-blue-600 text-white h-[42px] text-lg"
                        disabled={isSubmitting}
                      >
                        {isSubmitting ? "등록 중..." : "사업자 등록하기"}
                      </Button>
                    </div>
                  </form>
                </CardContent>
              </Card>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
