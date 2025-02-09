"use client";

import { findRoomDetail, modifyRoom } from "@/lib/api/BusinessRoomApi";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { BED_TYPES, BedTypeNumber } from "@/lib/types/Room/BedTypeNumber";
import { PutRoomRequest } from "@/lib/types/Room/PutRoomRequest";
import { XCircle } from "lucide-react";
import { uploadImagesToS3 } from "@/lib/api/AwsS3Api";
import { uploadImageUrls } from "@/lib/api/ReviewApi";
import { PutRoomResponse } from "./../../../../lib/types/Room/PutRoomResponse";
import { PresignedUrlsResponse } from "@/lib/types/PresignedUrlsResponse";

export default function ModifyRoomPage() {
  const params = useParams();
  const [hotelId, setHotelId] = useState(Number(params.hotelId));
  const [roomId, setRoomId] = useState(Number(params.roomId));
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
  const [roomStatus, setRoomStatus] = useState("AVAILABLE");
  const [existingImages, setExistingImages] = useState<string[]>([]);
  const [deleteImageUrls, setDeleteImageUrls] = useState<string[]>([]);
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [imageExtensions, setImageExtensions] = useState<string[]>([]);
  const [presignedUrls, setPresignedUrls] = useState<string[]>([]);
  const [roomOptions, setRoomOptions] = useState<Set<string>>(new Set());
  const [availableRoomOptions, setAvailableRoomOptions] = useState<Set<string>>(
    new Set()
  );

  useEffect(() => {
    const fetchRoomData = async () => {
      try {
        const response = await findRoomDetail(hotelId, roomId);
        setHotelId(response.roomDto.hotelId);
        setRoomId(response.roomDto.id);
        setRoomName(response.roomDto.roomName);
        setRoomNumber(response.roomDto.roomNumber);
        setBasePrice(response.roomDto.basePrice);
        setStandardNumber(response.roomDto.standardNumber);
        setMaxNumber(response.roomDto.maxNumber);
        setBedTypeNumber(response.roomDto.bedTypeNumber);
      } catch (error) {
        throw error;
      }
    };

    fetchRoomData();
  }, [params.hotelId, params.roomId]);

  const handleNumberInputChange =
    (setter: (value: number) => void) =>
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const value = Math.max(1, Number(e.target.value));
      setter(value);
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

  const handleBedTypeChange = (type: keyof BedTypeNumber, value: string) => {
    const newValue = value ? Math.max(0, parseInt(value, 10)) : 0;
    setBedTypeNumber((prev) => ({
      ...prev,
      [type]: newValue,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!roomId) {
      alert("유효하지 않은 객실 ID입니다.");
      console.error("유효하지 않은 객실 ID입니다.");
      return;
    }

    const requestBody: PutRoomRequest = {
      roomName,
      roomNumber,
      basePrice,
      standardNumber,
      maxNumber,
      bedTypeNumber,
      roomStatus,
      deleteImageUrls,
      imageExtensions: imageExtensions,
      roomOptions: Array.from(roomOptions),
    };

    try {
      const response: PutRoomResponse = await modifyRoom(
        hotelId,
        roomId,
        requestBody
      );
      const preSigendUrlsResponse: PresignedUrlsResponse =
        response.urlsResponse;

      setPresignedUrls(preSigendUrlsResponse.presignedUrls);
      await submitImages();
      alert("객실이 성공적으로 수정되었습니다.");
    } catch (error) {
      console.error(error);
      alert("객실 수정 중 오류가 발생했습니다.");
    }
  };

  const submitImages = async () => {
    if (presignedUrls.length === 0) {
      alert("이미지 업로드 URL을 가져오지 못했습니다.");
      return;
    }

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
      return presignedUrls.split("?")[0];
    });

    try {
      uploadImageUrls(hotelId, urls);
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
          <CardTitle>객실 수정</CardTitle>
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
              <Label htmlFor="roomNumber">객실 번호</Label>
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
              <Label htmlFor="basePrice">기본 가격</Label>
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
              <Label htmlFor="standardNumber">기본 인원</Label>
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
              <Label htmlFor="roomStatus">객실 상태</Label>
              <select
                id="roomStatus"
                value={roomStatus}
                onChange={(e) => setRoomStatus(e.target.value)}
                className="border p-2 w-full"
              >
                <option value="AVAILABLE">사용 가능</option>
                <option value="IN_BOOKING">예약 중</option>
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
                          className="absolute top-2 right-2 opacity-0 group-hover:opactiy-100 transition-opacity"
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
