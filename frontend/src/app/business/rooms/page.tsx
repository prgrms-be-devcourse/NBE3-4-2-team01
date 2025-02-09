"use client";

import React, { useEffect, useState } from "react";
import { createRoom, findAllRoomOptions } from "@/lib/api/BusinessRoomApi";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { BED_TYPES, BedTypeNumber } from "@/lib/types/Room/BedTypeNumber";
import { PostRoomResponse } from "@/lib/types/Room/PostRoomResponse";
import { PostRoomRequest } from "@/lib/types/Room/PostRoomRequest";
import { GetAllRoomOptionsResponse } from "@/lib/types/Room/GetAllRoomOptionsResponse";
import { uploadImagesToS3 } from "@/lib/api/AwsS3Api";
import { uploadImageUrls } from "@/lib/api/ReviewApi";

export default function CreateRoomPage() {
  const [roomName, setRoomName] = useState("");
  const [roomNumber, setRoomNumber] = useState(1);
  const [basePrice, setBasePrice] = useState(50000);
  const [standardNumber, setStandardNumber] = useState(1);
  const [maxNumber, setMaxNumber] = useState(1);
  const [bedTypeNumber, setBedTypeNumber] = useState<BedTypeNumber>({
    single: 0,
    double: 0,
    queen: 0,
    king: 0,
    twin: 0,
    triple: 0,
  });
  const [imageExtensions, setImageExtensions] = useState<string[]>([]);
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [presigendUrls, setPresignedUrls] = useState<string[]>([]);
  const [roomId, setRoomId] = useState(0);
  const [roomOptions, setRoomOptions] = useState<Set<string>>(new Set());
  const [availableRoomOptions, setAvailableRoomOptions] = useState<Set<string>>(
    new Set()
  );

  const handleBedTypeChange = (type: keyof BedTypeNumber, value: string) => {
    setBedTypeNumber((prev) => ({
      ...prev,
      [type]: value ? parseInt(value, 10) : 0,
    }));
  };

  // 객실 옵션 전체 리스트 가져오기
  //   useEffect(() => {
  //     const loadRoomOptions = async () => {
  //       try {
  //         const options: GetAllRoomOptionsResponse = await findAllRoomOptions();
  //         setAvailableRoomOptions(new Set(options.roomOptions));
  //       } catch (error) {
  //         throw error;
  //       }
  //     };
  //     loadRoomOptions();
  //   }, []);

  //   const handleOptionChange = (option: string) => {
  //     setRoomOptions((prev) => {
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

  const handleNumberInputChange =
    (setter: (value: number) => void) =>
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const value = Math.max(1, Number(e.target.value));
      setter(value);
    };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const requestBody: PostRoomRequest = {
      roomName,
      roomNumber: Number(roomNumber),
      basePrice: Number(basePrice),
      standardNumber: Number(standardNumber),
      maxNumber: Number(maxNumber),
      bedTypeNumber,
      imageExtensions: images.map(
        (file) => file.name.split(".").pop()?.toLowerCase() || ""
      ),
      roomOptions: Array.from(roomOptions),
    };

    try {
      const response: PostRoomResponse = await createRoom(1, requestBody);
      alert("객실이 성공적으로 등록되었습니다.");
    } catch (error) {
      console.error(error);
      alert("객실 생성 중 오류가 발생했습니다.");
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
      await uploadImageUrls(roomId, viewUrls);
      console.log("객실실 이미지가 성공적으로 저장되었습니다.");
    } catch (error) {
      console.error("이미지 URL 저장 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="p-4">
      <Card>
        <CardHeader>
          <CardTitle>객실 생성</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <Label htmlFor="roomName">객실 이름</Label>
              <Input
                id="roomName"
                value={roomName}
                onChange={(e) => setRoomName(e.target.value)}
                required
              />
            </div>

            <div>
              <Label htmlFor="roomNumber">객실 수</Label>
              <Input
                id="roomNumber"
                type="number"
                min="0"
                value={roomNumber}
                onChange={handleNumberInputChange(setRoomNumber)}
                required
              />
            </div>

            <div>
              <Label htmlFor="basePrice">인당 가격</Label>
              <Input
                id="basePrice"
                type="number"
                min="0"
                value={basePrice}
                onChange={handleNumberInputChange(setBasePrice)}
                required
              />
            </div>
            <div>
              <Label htmlFor="standardNumber">최소 인원</Label>
              <Input
                id="standardNumber"
                type="number"
                min="0"
                value={standardNumber}
                onChange={handleNumberInputChange(setStandardNumber)}
                required
              />
            </div>
            <div>
              <Label htmlFor="maxNumber">최대 인원</Label>
              <Input
                id="maxNumber"
                type="number"
                min="0"
                value={maxNumber}
                onChange={handleNumberInputChange(setMaxNumber)}
                required
              />
            </div>

            <div>
              <Label>침대 유형 및 개수</Label>
              {BED_TYPES.map((type) => (
                <div key={type} className="flex items-center gap-2">
                  <Label>{type.toUpperCase()}</Label>
                  <Input
                    type="number"
                    min="0"
                    className="w-16"
                    value={bedTypeNumber[type] || ""}
                    onChange={(e) => handleBedTypeChange(type, e.target.value)}
                  />
                </div>
              ))}
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
                {[...availableRoomOptions].map((option) => (
                  <label key={option} className="flex items-center">
                    <input
                      type="checkbox"
                      value={option}
                      checked={roomOptions.has(option)}
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
