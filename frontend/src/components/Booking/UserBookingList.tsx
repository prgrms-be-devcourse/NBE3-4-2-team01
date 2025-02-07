import { PageDto } from "@/lib/types/PageDto";
import { BookingResponseSummary } from "@/lib/types/Booking/BookingResponseSummary";

const UserBookingList = function({bookings} : {bookings : PageDto<BookingResponseSummary>}) {
    return (
        <div>
          <h2 className="text-lg font-bold mb-4">예약 목록</h2>
          <table className="w-full border-collapse border border-gray-300">
            <thead>
              <tr className="bg-gray-200">
                <th className="border p-2">예약 ID</th>
                <th className="border p-2">객실 ID</th>
                <th className="border p-2">호텔 ID</th>
                <th className="border p-2">예약 번호</th>
                <th className="border p-2">상태</th>
                <th className="border p-2">체크인</th>
                <th className="border p-2">체크아웃</th>
              </tr>
            </thead>
            <tbody>
              {bookings.items.length > 0 ? (
                bookings.items.map((booking) => (
                  <tr key={booking.bookingId} className="border">
                    <td className="border p-2">{booking.bookingId}</td>
                    <td className="border p-2">{booking.roomId}</td>
                    <td className="border p-2">{booking.hotelId}</td>
                    <td className="border p-2">{booking.bookNumber}</td>
                    <td className="border p-2">{booking.bookingStatus}</td>
                    <td className="border p-2">{booking.checkInDate}</td>
                    <td className="border p-2">{booking.checkOutDate}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={7} className="text-center p-4">
                    예약이 없습니다.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
    );
}

export default UserBookingList;