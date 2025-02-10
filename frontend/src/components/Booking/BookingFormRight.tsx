import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
    CardFooter
  } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { BookingProps } from "@/lib/types/Booking/BookingProps";
import Payment from "./Payment/Payment";
import { KakaoPaymentRequest } from "@/lib/types/Booking/Payment/KakaoPaymentRequest";
import { book } from "@/lib/api/Booking/BookingApi";
import { BookingRequest } from "@/lib/types/Booking/BookingRequest";
import { useRouter } from "next/navigation";

const BookingFormRight = function({hotelId, roomId, checkInDate, checkOutDate} : BookingProps) {
    const router = useRouter();

    const createBookingAndRedirect = async (
        kakaoPaymentRequest: KakaoPaymentRequest
      ) => {
        try {
          // KakaoPaymentRequest와 나머지 정보를 합쳐서 BookingRequest 생성
          const bookingRequest: BookingRequest = {
            hotelId: hotelId,
            roomId: roomId,
            checkInDate: checkInDate,
            checkOutDate: checkOutDate,
            merchantUid: kakaoPaymentRequest.merchantUid,
            amount: kakaoPaymentRequest.amount,
            paidAtTimestamp: kakaoPaymentRequest.paidAtTimestamp
          };
      
          // 예약 API 호출
          await book(bookingRequest);
          alert("예약에 성공했습니다.");
          router.push('/me/orders');
        } catch (error) {
          console.error(error);
        }
      };
    
    return (
      <Card className="self-start">
        <CardHeader>
          <CardTitle>예약 정보</CardTitle>
          <CardDescription>예약 정보를 확인해주세요.</CardDescription>
        </CardHeader>
        <CardContent className="space-y-6">
          <div>
            <h3 className="text-sm font-medium text-gray-500">호텔 ID</h3>
            <p className="text-lg font-medium mt-1">{hotelId}</p>
          </div>

          <div>
            <h3 className="text-sm font-medium text-gray-500">객실 ID</h3>
            <p className="text-lg font-medium mt-1">{roomId}</p>
          </div>

          <div>
            <h3 className="text-sm font-medium text-gray-500">체크인 날짜</h3>
            <p className="text-lg font-medium mt-1">{checkInDate}</p>
          </div>

          <div>
            <h3 className="text-sm font-medium text-gray-500">체크아웃 날짜</h3>
            <p className="text-lg font-medium mt-1">{checkOutDate}</p>
          </div>

          <div>
            <h3 className="text-sm font-medium text-gray-500">투숙객 이름</h3>
            <p className="text-lg font-medium mt-1">{"dummy"}</p>
          </div>

          <div>
            <h3 className="text-sm font-medium text-gray-500">투숙객 이메일</h3>
            <p className="text-lg font-medium mt-1">{"dummy@gmail.com"}</p>
          </div>

          <div className="pt-4 border-t">
            <div className="flex justify-between items-center">
              <span className="text-base font-medium">총 결제 금액</span>
              <span className="text-xl font-bold text-blue-600">308,000원</span>
            </div>
          </div>
        </CardContent>
        <CardFooter>
          <Payment 
          buyerEmail="dummy@gmail.com" 
          buyerName="dummy" 
          amount={1234} 
          productName="dummy room"
          onPaymentComplete={createBookingAndRedirect}></Payment>
        </CardFooter>
      </Card>
    );
}

export default BookingFormRight;