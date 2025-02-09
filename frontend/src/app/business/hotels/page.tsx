"use client";

import { PostHotelRequest } from "@/lib/types/hotel/PostHotelRequest";
import React, { useEffect, useState } from "react";
import { PresignedUrlsResponse } from "./../../../lib/types/PresignedUrlsResponse";
import { createHotel, findAllHotelOptions } from "@/lib/api/BusinessHotelApi";
import { PostHotelResponse } from "@/lib/types/hotel/PostHotelResponse";
import { uploadImagesToS3 } from "@/lib/api/AwsS3Api";
import { uploadImageUrls } from "@/lib/api/ReviewApi";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { GetAllHotelOptionResponse } from "@/lib/types/hotel/GetAllHotelOptionResponse";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

export default function CreateHotelPage() {
  const [hotelName, setHotelName] = useState("");
  const [hotelEmail, setHotelEmail] = useState("");
  const [hotelPhoneNumber, setHotelPhoneNumber] = useState("");
  const [streetAddress, setStreetAddress] = useState("");
  const [zipCode, setZipCode] = useState("");
  const [hotelGrade, setHotelGrade] = useState(1);
  const [checkInTime, setCheckInTime] = useState<string>("");
  const [checkOutTime, setCheckOutTime] = useState<string>("");
  const [hotelExplainContent, setHotelExplainContent] = useState("");
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [presigendUrls, setPresignedUrls] = useState<string[]>([]);
  const [hotelId, setHotelId] = useState(0);
  const [hotelOptions, setHotelOptions] = useState<Set<string>>(new Set());
  const [availableHotelOptions, setAvailableHotelOptions] = useState<
    Set<string>
  >(new Set());

  // 호텔 옵션 전체 리스트 가져오기
  //   useEffect(() => {
  //     const loadHotelOptions = async () => {
  //       try {
  //         const options: GetAllHotelOptionResponse = await findAllHotelOptions();
  //         setAvailableHotelOptions(new Set(options.hotelOptions));
  //       } catch (error) {
  //         throw error;
  //       }
  //     };
  //     loadHotelOptions();
  //   }, []);

  //   const handleOptionChange = (option: string) => {
  //     setHotelOptions((prev) => {
  //       const newOptions = new Set(prev);
  //       if (newOptions.has(option)) {
  //         newOptions.delete(option);
  //       } else {
  //         newOptions.add(option);
  //       }

  //       return newOptions;
  //     });
  //   };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const selectedFiles = Array.from(e.target.files);
      setImages(selectedFiles);

      // 미리보기 업데이트
      setImagePreviews(selectedFiles.map((file) => URL.createObjectURL(file)));
    }
  };

  const parseTimeStringToDate = (time: string): Date | null => {
    if (!time) return null; // 빈 값 처리
    const [hours, minutes] = time.split(":").map(Number);
    if (isNaN(hours) || isNaN(minutes)) return null; // 숫자로 변환 실패 시 예외 처리
    const date = new Date();
    date.setHours(hours, minutes, 0, 0); // 현재 날짜의 시간만 설정
    return date;
  };

  const handleTimeChange = (
    date: Date | null,
    setter: (time: string) => void
  ) => {
    if (!date) return setter("");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    setter(`${hours}:${minutes}`);
  };

  const formatPhoneNumber = (value: string) => {
    value = value.replace(/\D/g, "");
    if (value.length <= 3) return value;
    if (value.length <= 7) return `${value.slice(0, 3)}-${value.slice(3)}`;
    return `${value.slice(0, 3)}-${value.slice(3, 7)}-${value.slice(7, 11)}`;
  };

  const handlePhoneNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setHotelPhoneNumber(formatPhoneNumber(e.target.value));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const requestBody: PostHotelRequest = {
      hotelName,
      hotelEmail,
      hotelPhoneNumber,
      streetAddress,
      zipCode: Number(zipCode),
      hotelGrade,
      checkInTime,
      checkOutTime,
      hotelExplainContent,
      imageExtensions: images.map(
        (file) => file.name.split(".").pop()?.toLowerCase() || ""
      ),
      hotelOptions: Array.from(hotelOptions),
    };

    try {
      const response: PostHotelResponse = await createHotel(requestBody);
      const presignedUrlResponse: PresignedUrlsResponse = response.urlsResponse;
      setPresignedUrls(presignedUrlResponse.presignedUrls);
      setHotelId(presignedUrlResponse.reviewId);
      await submitImages();
      alert("호텔이 성공적으로 등록되었습니다.");
    } catch (error) {
      console.error(error);
      alert("호텔 생성 또는 이미지 업로드 중 오류가 발생했습니다.");
    }
  };

  // PresignedUrls 를 사용하여 이미지 업로드
  const submitImages = async () => {
    if (presigendUrls.length === 0) {
      alert("이미지 업로드 URL을 가져오지 못했습니다.");
      return;
    }

    try {
      await uploadImagesToS3(presigendUrls, images);
      await saveImageUrls();
      console.log("이미지가 성공적으로 업로드 되었습니다.");
    } catch (error) {
      console.error("Error:", error);
      alert("이미지 업로드 중 오류가 발생했습니다.");
    }
  };

  // 사진 조회용 URL들 서버로 전달
  const saveImageUrls = async () => {
    const viewUrls = presigendUrls.map((presigendUrls) => {
      return presigendUrls.split("?")[0];
    });

    try {
      await uploadImageUrls(hotelId, viewUrls);
      console.log("호텔 이미지가 성공적으로 저장되었습니다.");
    } catch (error) {
      console.error("이미지 URL 저장 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="p-4">
      <Card>
        <CardHeader>
          <CardTitle>호텔 등록</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <Label htmlFor="hotelName">호텔 이름</Label>
              <Input
                id="hotelName"
                value={hotelName}
                onChange={(e) => setHotelName(e.target.value)}
                required
              />
            </div>

            <div>
              <Label htmlFor="hotelEmail">이메일</Label>
              <Input
                id="hotelEmail"
                value={hotelEmail}
                onChange={(e) => setHotelEmail(e.target.value)}
                required
              />
            </div>

            <div>
              <Label htmlFor="hotelPhoneNumber">전화번호</Label>
              <Input
                id="hotelPhoneNumber"
                value={hotelPhoneNumber}
                onChange={handlePhoneNumberChange}
                placeholder="010-1234-5678"
                maxLength={13}
                required
              />
            </div>

            <div>
              <Label htmlFor="streetAddress">주소</Label>
              <Input
                id="streetAddress"
                value={streetAddress}
                onChange={(e) => setStreetAddress(e.target.value)}
                required
              />
            </div>

            <div>
              <Label htmlFor="zipCode">우편번호</Label>
              <Input
                id="zipCode"
                value={zipCode}
                onChange={(e) => setZipCode(e.target.value)}
                required
              />
            </div>

            <div>
              <Label htmlFor="hotelGrade">호텔 등급</Label>
              <Input
                id="hotelGrade"
                value={hotelGrade}
                onChange={(e) => setHotelGrade(Number(e.target.value))}
                required
              />
            </div>

            <div>
              <Label htmlFor="checkInTime">체크인 시간 : </Label>
              <DatePicker
                selected={
                  checkInTime ? parseTimeStringToDate(checkInTime) : null
                }
                onChange={(date) => {
                  handleTimeChange(date, setCheckInTime);
                }}
                showTimeSelect
                showTimeSelectOnly
                timeIntervals={30}
                timeFormat="HH:mm"
                dateFormat="HH:mm"
                className="border p-2 w-full"
              />
            </div>

            <div>
              <Label htmlFor="checkOutTime">체크아웃 시간 : </Label>
              <DatePicker
                selected={
                  checkOutTime ? parseTimeStringToDate(checkOutTime) : null
                }
                onChange={(date) => handleTimeChange(date, setCheckOutTime)}
                showTimeSelect
                showTimeSelectOnly
                timeIntervals={30}
                timeFormat="HH:mm"
                dateFormat="HH:mm"
                className="border p-2 w-full"
              />
            </div>

            <div>
              <Label htmlFor="hotelExplainContent">호텔 설명</Label>
              <Input
                id="hotelExplainContent"
                value={hotelExplainContent}
                onChange={(e) => setHotelExplainContent(e.target.value)}
                required
              />
            </div>

            <div>
              <Label htmlFor="images" className="block mb-2">
                이미지
              </Label>
              <Input
                id="images"
                type="file"
                multiple
                accept="image/*"
                onChange={handleImageUpload}
                className="cursor-pointer border p-2 w-full"
              />
              {imagePreviews.length > 0 && (
                <div className="mt-4 grid grid-cols-3 gap-4">
                  {imagePreviews.map((preview, index) => (
                    <div key={index} className="relative">
                      <img
                        src={preview}
                        alt={`미리보기 이미지 ${index + 1}`}
                        className="w-full h-32 object-cover rounded-md"
                      />
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* <div>
              <Label htmlFor="hotelOptions">호텔 옵션</Label>
              <div className="grid grid-cols-2 gap-2">
                {[...availableHotelOptions].map((option) => (
                  <label key={option} className="flex items-center">
                    <input
                      type="checkbox"
                      value={option}
                      checked={hotelOptions.has(option)}
                      onChange={() => handleOptionChange(option)}
                    />
                    {option}
                  </label>
                ))}
              </div>
            </div> */}

            <Button
              type="submit"
              className="w-full bg-blue-500 text-white mt-4"
            >
              등록
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
