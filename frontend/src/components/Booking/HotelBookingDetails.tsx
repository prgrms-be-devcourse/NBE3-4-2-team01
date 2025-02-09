import { BookingResponseDetails } from "@/lib/types/Booking/BookingResponseDetails";
import { completeBooking, cancelBooking } from "@/lib/api/Booking/BookingApi";

const HotelBookingDetails = function({bookingDetails} : {bookingDetails : BookingResponseDetails}) {
    const complete = async function() {
        try {
            await completeBooking(bookingDetails.bookingId);
            alert("예약이 정상적으로 완료되었습니다.")
        } catch (error) {
            alert(error);
        } finally {
            window.close();
        }
    }

    const cancel = async function() {
        try {
            await cancelBooking(bookingDetails.bookingId);
            alert("예약이 정상적으로 취소되었습니다.")
        } catch (error) {
            alert(error);
        } finally {
            window.close();
        }
    }
    
    return (
        <div style={{ textAlign: "center" }}>
          <h2>예약 상세 정보</h2>
          <ul style={{ listStyle: "none", padding: 0 }}>
            <li><strong>예약 ID:</strong> {bookingDetails.bookingId}</li>
            <li><strong>호텔 이름:</strong> {bookingDetails.hotel.hotelName}</li>
            <li><strong>호텔 Email:</strong> {bookingDetails.hotel.hotelEmail}</li>
            <li><strong>호텔 연락처:</strong> {bookingDetails.hotel.hotelPhoneNumber}</li>
            <li><strong>객실 유형:</strong> {bookingDetails.room.roomName}</li>
            <li><strong>객실 번호:</strong> {bookingDetails.room.roomNumber}</li>
            <li><strong>예약 번호:</strong> {bookingDetails.bookNumber}</li>
            <li><strong>예약 상태:</strong> {bookingDetails.bookingStatus}</li>
            <li><strong>예약 생성일:</strong> {bookingDetails.createdAt}</li>
            <li><strong>체크인 날짜:</strong> {bookingDetails.checkInDate}</li>
            <li><strong>체크아웃 날짜:</strong> {bookingDetails.checkOutDate}</li>
          </ul>
    
          <div style={{ margin: "20px 0" }}></div>
    
          <h2>결제 정보</h2>
          <ul style={{ listStyle: "none", padding: 0 }}>
            <li><strong>결제 ID:</strong> {bookingDetails.payment.paymentId}</li>
            <li><strong>Merchant UID:</strong> {bookingDetails.payment.merchantUid}</li>
            <li><strong>결제 금액:</strong> {bookingDetails.payment.amount}원</li>
            <li><strong>결제 상태:</strong> {bookingDetails.payment.paymentStatus}</li>
            <li><strong>결제 일시:</strong> {bookingDetails.payment.paidAt}</li>
          </ul>
    
          <div style={{ marginTop: "20px" }}>
            <button 
              style={{ backgroundColor: "green", color: "white", margin: "0 10px", padding: "10px 20px", cursor: "pointer", border: "none", borderRadius: "5px" }}
              onClick={() => complete()}
            >
              숙박 완료
            </button>
            <button 
              style={{ backgroundColor: "red", color: "white", margin: "0 10px", padding: "10px 20px", cursor: "pointer", border: "none", borderRadius: "5px" }}
              onClick={() => cancel()}
            >
              예약 취소
            </button>
          </div>
        </div>
    );
}

export default HotelBookingDetails;