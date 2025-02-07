export interface HotelDto { // 호텔 파트 데이터 사용할 때 사용하세요
  id: number;
  hotelName: string;
  hotelEmail: string;
  hotelPhoneNumber: string;
  streetAddress: string;
  zipCode: number;
  hotelGrade: number;
  checkInTime: string;
  checkOutTime: string;
  hotelExplainContent: string;
  averageRating: number;
} 