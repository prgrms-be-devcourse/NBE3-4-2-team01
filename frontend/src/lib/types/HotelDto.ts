export interface HotelDto { // 호텔 파트 데이터 사용할 때 사용하세요
  hotelId: number;
  hotelName: string;
  hotelEmail: string;
  hotelPhoneNumber: string;
  streetAddress: string;
  zipCode: number;
  hotelGrade: number;
  checkInTime: string;
  checkOutTime: string;
  hotelExplainContent: string;
  hotelStatus: string;
  rooms: {
    id: number;
    roomName: string;
    roomNumber: number;
    basePrice: number;
  }[];
  hotelOptions: string[];
} 