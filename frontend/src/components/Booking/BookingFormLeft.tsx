import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
  } from "@/components/ui/card";
import { GetHotelDetailResponse } from "@/lib/types/hotel/GetHotelDetailResponse";
import { GetRoomDetailResponse } from "@/lib/types/room/GetRoomDetailResponse";
import { BedDouble, BedSingle, Check, Star, User, Utensils, Wifi } from 'lucide-react';

const BookingFormLeft = function(
    {hotelDetails, roomDetails} : {hotelDetails : GetHotelDetailResponse, roomDetails : GetRoomDetailResponse}) {
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
                src={hotelDetails.hotelImageUrls[0]}
                alt="호텔 전경" 
                className="w-full h-64 object-cover"
              />
            </div>
            <div className="overflow-hidden rounded-lg">
              <img 
                src={roomDetails.roomImageUrls[0]} 
                alt="객실 전경" 
                className="w-full h-64 object-cover"
              />
            </div>
          </div>

          {/* 호텔 정보 */}
          <div>
          <div className="flex items-center gap-2 mb-4">
            <h3 className="text-lg font-semibold">{hotelDetails.hotelDetailDto.hotelName}</h3>
            <div className="flex">
                {Array(hotelDetails.hotelDetailDto.hotelGrade)
                .fill(null)
                .map((_, index) => (
                    <Star 
                    key={index} 
                    className="w-4 h-4 text-yellow-400 fill-yellow-400" 
                    />
                ))}
            </div>
            </div>
            <p className="text-sm text-gray-600">
            ({hotelDetails.hotelDetailDto.zipCode}) {hotelDetails.hotelDetailDto.streetAddress}
            </p>
            <p className="text-sm text-gray-600">{hotelDetails.hotelDetailDto.hotelPhoneNumber}</p>
            <p className="text-sm text-gray-600 mt-2">
              체크인: {hotelDetails.hotelDetailDto.checkInTime} ~<br/>
              체크아웃: ~ {hotelDetails.hotelDetailDto.checkOutTime}
            </p>
          </div>

          {/* 객실 정보 */}
          <div className="pt-6 border-t">
            <h3 className="text-lg font-semibold mb-2">{roomDetails.roomDto.roomName}</h3>
            <div className="grid grid-cols-1 gap-1">
                <div className="flex items-center gap-2 text-sm text-gray-600">
                <User className="w-4 h-4" />
                <span>최대 {roomDetails.roomDto.maxNumber}인</span>
                </div>
                <div className="flex items-center gap-2 text-sm text-gray-600">
                <BedDouble className="w-4 h-4 flex-shrink-0" />
                <span className="truncate">
                    {[
                    roomDetails.roomDto.bedTypeNumber.SINGLE > 0 && `${roomDetails.roomDto.bedTypeNumber.SINGLE} 싱글 베드`,
                    roomDetails.roomDto.bedTypeNumber.DOUBLE > 0 && `${roomDetails.roomDto.bedTypeNumber.DOUBLE} 더블 베드`,
                    roomDetails.roomDto.bedTypeNumber.KING > 0 && `${roomDetails.roomDto.bedTypeNumber.KING} 킹 베드`,
                    roomDetails.roomDto.bedTypeNumber.QUEEN > 0 && `${roomDetails.roomDto.bedTypeNumber.QUEEN} 퀸 베드`,
                    roomDetails.roomDto.bedTypeNumber.TWIN > 0 && `${roomDetails.roomDto.bedTypeNumber.TWIN} 트윈 베드`,
                    roomDetails.roomDto.bedTypeNumber.TRIPLE > 0 && `${roomDetails.roomDto.bedTypeNumber.TRIPLE} 트리플 베드`,
                    ].filter(Boolean).join(', ')}
                </span>
                </div>
                <div className="flex items-center gap-2 text-sm text-gray-600">
                <Check className="w-4 h-4" />
                <span className="truncate">
                    {Array.from(roomDetails.roomDto.roomOptions).join(', ')}
                </span>
                </div>
            </div>
          </div>
        </CardContent>
    </Card>
    );
}

export default BookingFormLeft;