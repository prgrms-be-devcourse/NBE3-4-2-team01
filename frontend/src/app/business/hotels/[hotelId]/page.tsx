"use client";

import {
  findAllHotelOptions,
  findHotelDetail,
  modifyHotel,
  saveHotelImageUrls,
} from "@/lib/api/BusinessHotelApi";
import { PutHotelRequest } from "@/lib/types/hotel/PutHotelRequest";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { PresignedUrlsResponse } from "@/lib/types/review/PresignedUrlsResponse";
import { PutHotelResponse } from "@/lib/types/hotel/PutHotelResponse";
import { uploadImagesToS3 } from "@/lib/api/AwsS3Api";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import DatePicker from "react-datepicker";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { XCircle } from "lucide-react";
import "react-datepicker/dist/react-datepicker.css";
import { GetAllHotelOptionResponse } from "@/lib/types/hotel/GetAllHotelOptionResponse";

export default function ModifyHotelPage() {
  const params = useParams();
  const [hotelId, setHotelId] = useState(Number(params.hotelId));
  const [hotelName, setHotelName] = useState("");
  const [hotelEmail, setHotelEmail] = useState("");
  const [hotelPhoneNumber, setHotelPhoneNumber] = useState("");
  const [streetAddress, setStreetAddress] = useState("");
  const [zipCode, setZipCode] = useState("");
  const [hotelGrade, setHotelGrade] = useState(1);
  const [checkInTime, setCheckInTime] = useState<string>("");
  const [checkOutTime, setCheckOutTime] = useState<string>("");
  const [hotelExplainContent, setHotelExplainContent] = useState("");
  const [hotelStatus, setHotelStatus] = useState("AVAILABLE");
  const [existingImages, setExistingImages] = useState<string[]>([]);
  const [deleteImageUrls, setDeleteImageUrls] = useState<string[]>([]);
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [imageExtensions, setImageExtensions] = useState<string[]>([]);
  const [presignedUrls, setPresignedUrls] = useState<string[]>([]);
  const [hotelOptions, setHotelOptions] = useState<Set<string>>(new Set());
  const [availableHotelOptions, setAvailableHotelOptions] = useState<
    Set<string>
  >(new Set());

  useEffect(() => {
    const fetchHotelData = async () => {
      try {
        const response = await findHotelDetail(Number(params.hotelId));
        setHotelId(response.hotelDetailDto.hotelId);
        setHotelName(response.hotelDetailDto.hotelName);
        setHotelEmail(response.hotelDetailDto.hotelEmail);
        setHotelPhoneNumber(response.hotelDetailDto.hotelPhoneNumber);
        setStreetAddress(response.hotelDetailDto.streetAddress);
        setZipCode(response.hotelDetailDto.zipCode.toString());
        setHotelGrade(response.hotelDetailDto.hotelGrade);
        setCheckInTime(response.hotelDetailDto.checkInTime);
        setCheckOutTime(response.hotelDetailDto.checkOutTime);
        setHotelExplainContent(response.hotelDetailDto.hotelExplainContent);
        setHotelStatus(response.hotelDetailDto.hotelStatus);
        setExistingImages(response.hotelImageUrls);
        setHotelOptions(new Set(response.hotelDetailDto.hotelOptions || []));
        console.log(response);
        console.log(response);
      } catch (error) {
        throw error;
      }
    };

    fetchHotelData();
  }, [params.hotelId]);

  // 호텔 옵션 전체 리스트 가져오기
  useEffect(() => {
    const loadHotelOptions = async () => {
      try {
        const options: GetAllHotelOptionResponse = await findAllHotelOptions();
        setAvailableHotelOptions(new Set(options.hotelOptions));
      } catch (error) {
        throw error;
      }
    };
    loadHotelOptions();
  }, []);

  const handleOptionChange = (option: string) => {
    setHotelOptions((prev) => {
      const newOptions = new Set(prev);
      if (newOptions.has(option)) {
        newOptions.delete(option);
      } else {
        newOptions.add(option);
      }

      return newOptions;
    });
  };

  const handleImageDelete = (imageUrl: string) => {
    setExistingImages(existingImages.filter((img) => img !== imageUrl));
    setDeleteImageUrls([...deleteImageUrls, imageUrl]);
  };

  const handlenewImageDelete = (index: number) => {
    setImages(images.filter((_, i) => i !== index));
    setImagePreviews(imagePreviews.filter((_, i) => i !== index));
    setImageExtensions(imageExtensions.filter((_, i) => i !== index));
  };

  const handleNewImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const files = Array.from(e.target.files);
      setImages([...images, ...files]);

      const newPreviews = files.map((file) => URL.createObjectURL(file));
      setImagePreviews([...imagePreviews, ...newPreviews]);

      const extensions = files.map((file) => {
        const ext = file.name.split(".").pop()?.toLowerCase() || "";
        return ext;
      });

      setImageExtensions([...imageExtensions, ...extensions]);
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

  useEffect(() => {
    return () => {
      imagePreviews.forEach((preview) => URL.revokeObjectURL(preview));
    };
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!hotelId) {
      alert("유효하지 않은 호텔 ID입니다.");
      console.error("유효하지 않은 호텔 ID입니다.");
      return;
    }

    const requestBody: PutHotelRequest = {
      hotelName,
      hotelEmail,
      hotelPhoneNumber,
      streetAddress,
      zipCode: Number(zipCode),
      hotelGrade,
      checkInTime,
      checkOutTime,
      hotelExplainContent,
      hotelStatus,
      deleteImageUrls,
      imageExtensions: imageExtensions,
      hotelOptions: Array.from(hotelOptions),
    };

    try {
      const response: PutHotelResponse = await modifyHotel(
        hotelId,
        requestBody
      );
      const presignedUrlsResponse: PresignedUrlsResponse =
        response.urlsResponse;
      console.log("서버 응답:", presignedUrlsResponse);

      setPresignedUrls(presignedUrlsResponse.presignedUrls);
      console.log(presignedUrls);
      console.log("호텔이 성공적으로 수정되었습니다.");
    } catch (error) {
      alert("호텔 생성 또는 이미지 업로드 중 오류가 발생했습니다.");
    }
  };

  useEffect(() => {
    if (presignedUrls.length > 0) {
      console.log("✅ presignedUrls 설정됨:", presignedUrls);
      submitImages();
    }
  }, [presignedUrls]);

  const submitImages = async () => {
    try {
      await uploadImagesToS3(presignedUrls, images);
      await saveImageUrls();
      console.log("이미지가 성공적으로 업로드되었습니다.");
    } catch (error) {
      console.error(error);
      alert("이미지 업로드 중 오류가 발생했습니다.");
    }
  };

  const saveImageUrls = async () => {
    const urls = presignedUrls.map((presignedUrls) => {
      console.log(presignedUrls);
      return presignedUrls.split("?")[0];
    });

    try {
      saveHotelImageUrls(hotelId, urls);
      alert("이미지 URL이 성공적으로 저장되었습니다.");
    } catch (error) {
      console.error(error);
      alert("이미지 URL 저장 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="p-4">
      <Card>
        <CardHeader>
          <CardTitle>호텔 수정</CardTitle>
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
              <Label htmlFor="hotelStatus">호텔 상태</Label>
              <select
                id="hotelStatus"
                value={hotelStatus}
                onChange={(e) => setHotelStatus(e.target.value)}
                className="border p-2 w-full"
              >
                <option value="AVAILABLE">사용 가능</option>
                <option value="PENDING">승인 대기 중</option>
                <option value="UNAVAILABLE">사용 불가</option>
              </select>
            </div>

            {existingImages.length > 0 && (
              <div className="space-y-2">
                <Label>기존 이미지</Label>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  {existingImages.map((img, index) => (
                    <div key={index} className="relative group">
                      <img
                        src={img}
                        alt={`호텔 이미지 ${index + 1}`}
                        className="w-full h-32 object-cover rounded-md"
                      />
                      <Button
                        type="button"
                        variant="destructive"
                        size="icon"
                        className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity"
                        onClick={() => handleImageDelete(img)}
                      >
                        <XCircle className="w-4 h-4" />
                      </Button>
                    </div>
                  ))}
                </div>
              </div>
            )}

            <div className="space-y-2">
              <Label htmlFor="images">새 이미지 추가</Label>
              <Input
                id="images"
                type="file"
                multiple
                accept="image/*"
                onChange={handleNewImageUpload}
                className="cursor-pointer"
              />

              {imagePreviews.length > 0 && (
                <div className="mt-4">
                  <Label>새 이미지 미리보기</Label>
                  <div className="mt-4 grid grid-cols-3 gap-4">
                    {imagePreviews.map((preview, index) => (
                      <div key={index} className="relative group">
                        <img
                          src={preview}
                          alt={`새 미리보기 이미지 ${index + 1}`}
                          className="w-full h-32 object-cover rounded-md"
                        />
                        <Button
                          type="button"
                          variant="destructive"
                          size="icon"
                          className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity"
                          onClick={() => handlenewImageDelete(index)}
                        >
                          <XCircle className="w-4 h-4" />
                        </Button>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>

            <div>
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
            </div>

            <Button
              type="submit"
              className="w-full bg-blue-500 text-white mt-4"
            >
              수정 완료
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
