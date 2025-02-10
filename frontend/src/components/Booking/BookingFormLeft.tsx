'use client'

import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
  } from "@/components/ui/card";
import { BedDouble, Utensils, Wifi } from 'lucide-react';

const BookingFormLeft = function({hotelId, roomId} : {hotelId : number, roomId : number}) {
    return (
    <Card>
        <CardHeader>
          <CardTitle>호텔/객실 정보</CardTitle>
          <CardDescription>선택하신 호텔/객실의 정보입니다.</CardDescription>
        </CardHeader>
        <CardContent className="space-y-6">
          {/* 이미지 갤러리 */}
          <div className="grid grid-cols-2 gap-4 mb-6">
            <div className="overflow-hidden rounded-lg">
              <img 
                src="https://cdn.pixabay.com/photo/2016/11/18/13/47/apple-1834639_1280.jpg" 
                alt="호텔 전경" 
                className="w-full h-64 object-cover"
              />
            </div>
            <div className="overflow-hidden rounded-lg">
              <img 
                src="https://cdn.pixabay.com/photo/2016/11/18/13/47/apple-1834639_1280.jpg" 
                alt="객실 전경" 
                className="w-full h-64 object-cover"
              />
            </div>
          </div>

          {/* 호텔 정보 */}
          <div>
            <div className="flex items-center gap-2 mb-4">
              <h3 className="text-lg font-semibold">그랜드 호텔</h3>
              <span className="text-sm text-gray-500">⭐⭐⭐⭐⭐</span>
            </div>
            <p className="text-sm text-gray-600">서울특별시 강남구 테헤란로 123</p>
            <p className="text-sm text-gray-600">02-1234-5678</p>
            <p className="text-sm text-gray-600 mt-2">
              체크인: 15:00 ~ 22:00<br/>
              체크아웃: ~ 11:00
            </p>
          </div>

          {/* 객실 정보 */}
          <div className="pt-6 border-t">
            <h3 className="text-lg font-semibold mb-2">디럭스 더블룸</h3>
            <div className="grid grid-cols-2 gap-4">
              <div className="flex items-center gap-2 text-sm text-gray-600">
                <BedDouble className="w-4 h-4" />
                <span>더블 베드 1개</span>
              </div>
              <div className="flex items-center gap-2 text-sm text-gray-600">
                <Utensils className="w-4 h-4" />
                <span>조식 포함</span>
              </div>
              <div className="flex items-center gap-2 text-sm text-gray-600">
                <Wifi className="w-4 h-4" />
                <span>무료 Wi-Fi</span>
              </div>
            </div>
          </div>
        </CardContent>
    </Card>
    );
}

export default BookingFormLeft;