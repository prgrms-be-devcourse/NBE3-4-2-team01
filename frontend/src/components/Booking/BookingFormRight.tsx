import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
    CardFooter
  } from "@/components/ui/card";
import { BookingFormRightProps, BookingProps } from "@/lib/types/Booking/BookingProps";
import Payment from "./Payment/Payment";
import { KakaoPaymentRequest } from "@/lib/types/Booking/Payment/KakaoPaymentRequest";
import { book } from "@/lib/api/Booking/BookingApi";
import { BookingRequest } from "@/lib/types/Booking/BookingRequest";
import { useRouter } from "next/navigation";

const BookingFormRight = function(
    {hotelName, roomName, amount, bookingProps: {hotelId, roomId, checkInDate, checkOutDate}} : BookingFormRightProps) {
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
          router.push('/me/orders');
          alert("예약에 성공했습니다.");
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
            <h3 className="text-sm font-medium text-gray-500">호텔명</h3>
            <p className="text-lg font-medium mt-1">{hotelName}</p>
          </div>

          <div>
            <h3 className="text-sm font-medium text-gray-500">객실 유형</h3>
            <p className="text-lg font-medium mt-1">{roomName}</p>
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
                <span className="text-xl font-bold text-blue-600">
                {amount.toLocaleString()}원
                </span>
            </div>
          </div>
        </CardContent>
        <CardFooter>
          <Payment 
          buyerEmail="sete3683@gmail.com" 
          buyerName="dummy" 
          amount={amount} 
          productName={roomName} 
          onPaymentComplete={createBookingAndRedirect}></Payment>
        </CardFooter>
      </Card>
    );
}

export default BookingFormRight;