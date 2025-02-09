import { GetRoomResponse } from "@/lib/types/Room/GetRoomResponse";

interface RoomListProps {
  rooms: GetRoomResponse[];
}

const RoomList: React.FC<RoomListProps> = ({ rooms }) => {
  return (
    <div className="mb-6">
      <h2 className="text-2xl font-bold mb-4 border-b pb-2">객실 목록</h2>
      {rooms.length === 0 ? (
        <p className="text-gray-500">등록된 객실이 없습니다.</p>
      ) : (
        <ul className="space-y-6">
          {rooms.map((room) => (
            <li
              key={room.roomId}
              className="flex border rounded-lg shadow-lg overflow-hidden"
            >
              {/* 이미지 섹션 */}
              {room.thumbnailUrl && (
                <div className="w-1/3">
                  <img
                    src={room.thumbnailUrl}
                    alt={room.roomName}
                    className="w-full h-48 object-cover rounded-l-lg"
                  />
                </div>
              )}

              {/* 객실 정보 섹션 */}
              <div className="w-2/3 p-6 flex flex-col justify-center bg-white">
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  {room.roomName}
                </h3>
                <p className="text-lg text-gray-700 mb-2">
                  <strong className="text-gray-900">기본 가격 : </strong>
                  {room.basePrice}원
                </p>
                <p className="text-lg text-gray-700 mb-2">
                  <strong className="text-gray-900">수용 인원 : </strong>
                  {room.standardNumber} ~ {room.maxNumber}명
                </p>
                <p className="text-lg text-gray-700">
                  <strong className="text-gray-900">침대 타입 : </strong>{" "}
                  {Object.entries(room.bedTypeNumber)
                    .filter(([_, count]) => count > 0)
                    .map(([type, count]) => {
                      const formattedType = type
                        .replace("bed_", "")
                        .toUpperCase();
                      return `${formattedType} ${count}개`;
                    })
                    .join(", ")}
                </p>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default RoomList;
