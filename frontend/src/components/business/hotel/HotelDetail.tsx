import { HotelDetailDto } from "@/lib/types/hotel/HotelDetailDto";

interface HotelDetailProps {
  hotel: HotelDetailDto;
}

const HotelDetail: React.FC<HotelDetailProps> = ({ hotel }) => {
  return (
    <div className="bg-white p-6 shadow-lg rounded-lg border border-gray-200">
      <h2 className="text-2xl font-bold text-gray-800 mb-4">호텔 정보</h2>
      <div className="grid grid-cols-2 gap-6 text-lg text-gray-700">
        <p>
          <span className="font-semibold text-gray-900">이메일 : </span>
          {hotel.hotelEmail}
        </p>

        <p>
          <span className="font-semibold text-gray-900">전화번호 : </span>
          {hotel.hotelPhoneNumber}
        </p>

        <p>
          <span className="font-semibold text-gray-900">주소 : </span>
          {hotel.streetAddress}
        </p>

        <p>
          <span className="font-semibold text-gray-900">우편번호 : </span>
          {hotel.zipCode}
        </p>

        <p>
          <span className="font-semibold text-gray-900">체크인 시간 : </span>
          {hotel.checkInTime.slice(0, 5)}
        </p>

        <p>
          <span className="font-semibold text-gray-900">체크아웃 시간 : </span>
          {hotel.checkOutTime.slice(0, 5)}
        </p>

        <p className="mt-2 text-gray-600">
          <span className="font-semibold text-gray-900">호텔 옵션 : </span>
          {Array.from(hotel.hotelOptions).join(", ")}
        </p>
      </div>

      <div className="mt-8 pt-6 border-t border-gray-300">
        <h3 className="text-xl font-bold text-gray-800 mb-2">호텔 설명</h3>
        <p className="text-gray-700 leading-relaxed whitespace-pre-line">
          {hotel.hotelExplainContent}
        </p>
      </div>
    </div>
  );
};

export default HotelDetail;
