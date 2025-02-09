import { PageDto } from "@/lib/types/PageDto";
import { BookingResponseSummary } from "@/lib/types/Booking/BookingResponseSummary";

const HotelBookingList = function({bookings} : {bookings : PageDto<BookingResponseSummary>}) {
    const openBookingDetailsPopup = function(bookingId : number) {
        window.open(
            `/orders/${bookingId}/business`,
            "OrderPopup",
            "width=500,height=600,left=200,top=200"
        );
    }
    
    return (
        <div className="max-w-5xl mx-auto px-4 md:px-8"> {/* 최대 너비 + 좌우 패딩 추가 */}
          <h2 className="text-lg font-bold mb-4 text-center">예약 목록</h2>
          <div className="overflow-x-auto"> {/* 작은 화면에서 가로 스크롤 가능하도록 추가 */}
            <table className="w-full border-collapse border border-gray-300">
              <thead>
                <tr className="bg-gray-200">
                  <th className="border p-3">예약 ID</th>
                  <th className="border p-3">객실 번호</th>
                  <th className="border p-3">예약 번호</th>
                  <th className="border p-3">체크인</th>
                  <th className="border p-3">체크아웃</th>
                  <th className="border p-3">상태</th>
                </tr>
              </thead>
              <tbody>
                <style jsx>{`
                  tr:hover {
                    background-color: #f0f0f0;
                  }
                  tr {
                    transition: background-color 0.3s ease;
                  }
                `}</style>
                {bookings.items.length > 0 ? (
                  bookings.items.map((booking) => (
                    <tr key={booking.bookingId} className="border"
                      onClick={() => openBookingDetailsPopup(booking.bookingId)}
                      style={{ cursor: 'pointer' }}>
                      <td className="border p-3 text-center">{booking.bookingId}</td>
                      <td className="border p-3 text-center">{booking.room.roomNumber}</td>
                      <td className="border p-3 text-center">{booking.bookNumber}</td>
                      <td className="border p-3 text-center">{booking.checkInDate}</td>
                      <td className="border p-3 text-center">{booking.checkOutDate}</td>
                      <td className="border p-3 text-center">{booking.bookingStatus}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={6} className="text-center p-4">
                      예약이 없습니다.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      );
}

export default HotelBookingList;