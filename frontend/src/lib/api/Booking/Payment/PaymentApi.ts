import { UidResponse } from "@/lib/types/Booking/Payment/UidResponse";

// Uid 및 API Key 발급
export const getUid = async function(bookingId : number) : Promise<UidResponse> {
    try {
        const response = await fetch("http://localhost:8080/api/bookings/payments/uid");
        const rsData = await response.json();

        if (rsData.resultCode.startsWith("2")) {
            return rsData.data;
        } else {
            throw new Error(`에러 발생 : ${rsData.resultCode}, ${rsData.msg}`);
        }
    } catch (error) {
        throw error;
    }
}