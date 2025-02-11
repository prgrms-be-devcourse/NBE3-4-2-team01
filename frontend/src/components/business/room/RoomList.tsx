"use client";

import { Button } from "@/components/ui/button";
import { GetRoomResponse } from "@/lib/types/room/GetRoomResponse";
import { deleteRoom } from "@/lib/api/BusinessRoomApi";
import { getRoleFromCookie } from "@/lib/utils/CookieUtil";
import { useParams, useRouter, useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";

interface RoomListProps {
  rooms: GetRoomResponse[];
  checkInDate?: string;
  checkoutDate?: string;
}

const RoomList: React.FC<RoomListProps> = ({ rooms }) => {
  const cookie = getRoleFromCookie();
  const [hotelId, setHotelId] = useState(cookie?.hotelId);
  const [selectedImage, setSelectedImage] = useState<string | null>(null);
  const [isBusinessUser, setIsBusinessUser] = useState<boolean>(false);
  const [canEdit, setCanEdit] = useState<boolean>(false);
  const searchParams = useSearchParams();
  const param = useParams();
  const router = useRouter();
  const checkInDate = searchParams.get("checkInDate") || "";
  const checkoutDate = searchParams.get("checkoutDate") || "";

  useEffect(() => {
    const cookieHotelId = cookie?.hotelId ? Number(cookie.hotelId) : -1;
    const paramHotelId = param.hotelId ? Number(param.hotelId) : null;
    console.log("쿠키 호텔 ID : ", cookieHotelId);
    console.log("파람 호텔 ID : ", paramHotelId);

    setIsBusinessUser(cookie?.role == "BUSINESS");

    if (paramHotelId !== null) {
      setHotelId(paramHotelId);
      setCanEdit(isBusinessUser && cookieHotelId === paramHotelId);
    } else if (isBusinessUser) {
      setHotelId(cookieHotelId);
      setCanEdit(true);
    } else {
      setCanEdit(false);
    }
  }, [cookie, param.hotelId]);

  const handleEdit = (roomId: number) => {
    router.push(`/business/rooms/${roomId}`);
  };

  const handleDelete = async (roomId: number) => {
    if (!window.confirm("객실을 삭제하시겠습니까?")) return;

    try {
      await deleteRoom(hotelId ?? -1, roomId);
      alert("객실이 삭제되었습니다.");
      router.push("/business/hotel/management");
    } catch (error) {
      if (error instanceof Error) {
        alert(error.message);
      }
    }
  };

  const handleReservation = (roomId: number) => {
    const params = new URLSearchParams();
    params.set("hotel", (hotelId ?? "").toString());
    params.set("room", roomId.toString());
    if (checkInDate) params.set("checkInDate", checkInDate);
    if (checkoutDate) params.set("checkoutDate", checkoutDate);

    console.log(
      "예약 페이지로 이동 URL : ",
      `/orders/payment?${params.toString()}`
    );
    router.push(`/orders/payment?${params.toString()}`);
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-6">객실 목록</h2>
      {rooms.length === 0 ? (
        <p className="text-gray-500 text-center">등록된 객실이 없습니다.</p>
      ) : (
        <ul className="space-y-4">
          {rooms.map((room) => (
            <li
              key={room.roomId}
              className="flex flex-col border rounded-lg shadow-md p-8 bg-white"
            >
              {/* 이미지 & 정보 섹션 */}
              <div className="flex items-center">
                {room.thumbnailUrl && (
                  <img
                    src={room.thumbnailUrl}
                    alt={room.roomName}
                    className="w-80 h-64 object-cover rounded-lg mr-8 cursor-pointer duration-200 hover:scale-105"
                    onClick={() => setSelectedImage(room.thumbnailUrl)}
                  />
                )}

                <div className="flex-1">
                  <h3 className="text-3xl font-bold text-gray-900 mb-12">
                    {room.roomName}
                  </h3>
                  <p className="text-lg text-gray-600 mb-2">
                    🏠 수용 인원:{" "}
                    <span className="font-semibold">
                      {room.standardNumber} ~ {room.maxNumber}명
                    </span>
                  </p>
                  <p className="text-lg text-gray-600 mb-2">
                    🛏️ 침대 타입:{" "}
                    <span className="font-semibold">
                      {Object.entries(room.bedTypeNumber)
                        .filter(([_, count]) => count > 0)
                        .map(
                          ([type, count]) =>
                            `${type
                              .replace("bed_", "")
                              .toUpperCase()} ${count}개`
                        )
                        .join(", ")}
                    </span>
                  </p>

                  {/* 가격 (우측 중앙 정렬) */}
                  <p className="text-2xl font-bold text-blue-600 text-right">
                    {room.basePrice.toLocaleString()}원~
                  </p>
                </div>
              </div>

              {/* 버튼 섹션 */}
              <div className="mt-4 flex justify-end gap-2">
                {isBusinessUser && canEdit ? (
                  <>
                    <Button
                      className="bg-blue-500 text-white"
                      onClick={() => handleEdit(room.roomId)}
                    >
                      수정
                    </Button>
                    <Button
                      variant="destructive"
                      onClick={() => handleDelete(room.roomId)}
                    >
                      삭제
                    </Button>
                  </>
                ) : (
                  <Button
                    className="bg-green-500 text-white"
                    onClick={() => handleReservation(room.roomId)}
                  >
                    예약하기
                  </Button>
                )}
              </div>

              {/* 모달 - 이미지 확대 */}
              {selectedImage && (
                <div
                  className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50"
                  onClick={() => setSelectedImage(null)}
                >
                  <div className="relative p-4 bg-white rounded-lg shadow-lg max-w-3xl">
                    <button
                      className="absolute top-2 right-2 text-gray-600 hover:text-gray-900 test-2x1"
                      onClick={() => setSelectedImage(null)}
                    >
                      X
                    </button>
                    <img
                      src={selectedImage}
                      alt="확대한 이미지"
                      className="w-full max-h-[95vh] object-contain rounded-lg"
                    />
                  </div>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default RoomList;
