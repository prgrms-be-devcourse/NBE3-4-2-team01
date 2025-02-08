export interface PostHotelRequest {
  businessId: number;
  hotelName: string;
  hotelEmail: string;
  hotelPhoneNumber: string;
  streetAddress: string;
  zipCode: number;
  hotelGrade: number;
  checkInTime: string;
  checkOutTime: string;
  hotelExplainContent: string;
  imageExtensions: string[];
  hotelOptions: Set<string>;
}
