import { PageDto } from "@/lib/types/PageDto";
import { BookingResponseSummary } from "@/lib/types/Booking/BookingResponseSummary";

const UserBookingList = function({bookings} : {bookings : PageDto<BookingResponseSummary>}) {
    const openBookingDetailsPopup = function(bookingId : number) {
        window.open(
            `/orders/${bookingId}`,
            "OrderPopup",
            "width=500,height=600,left=200,top=200"
        );
    }
    
    return (
        <div className="max-w-screen-sm mx-auto">
          <div className="space-y-4">
            {bookings.items.length > 0 ? (
              bookings.items.map((booking) => (
                <div
                  key={booking.bookingId}
                  className="border rounded-lg hover:bg-gray-100 cursor-pointer flex items-center space-x-6 p-4"
                  onClick={() => openBookingDetailsPopup(booking.bookingId)}
                >
                  {/* 예약별 사진 */}
                  <div className="w-1/3">
                    <img
                      src="https://cdn.pixabay.com/photo/2016/11/18/13/47/apple-1834639_1280.jpg"
                      alt="Booking"
                      className="w-full h-full object-cover rounded-lg"
                    />
                  </div>
      
                  {/* 예약 목록 정보 */}
                  <div className="w-2/3 space-y-2">
                    <div className="flex justify-between text-sm">
                      <span className="font-bold">예약 ID:</span>
                      <span>{booking.bookingId}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span className="font-bold">객실 ID:</span>
                      <span>{booking.roomId}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span className="font-bold">호텔 ID:</span>
                      <span>{booking.hotelId}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span className="font-bold">예약 번호:</span>
                      <span>{booking.bookNumber}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span className="font-bold">상태:</span>
                      <span>{booking.bookingStatus}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span className="font-bold">체크인:</span>
                      <span>{booking.checkInDate}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span className="font-bold">체크아웃:</span>
                      <span>{booking.checkOutDate}</span>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="text-center p-4">
                예약이 없습니다.
              </div>
            )}
          </div>
        </div>
      );
}

export default UserBookingList;