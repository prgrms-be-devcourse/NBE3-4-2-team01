import { UidResponse } from "@/lib/types/Booking/Payment/UidResponse";

// Uid 및 API Key 발급
export const getUid = async function() : Promise<UidResponse> {
    try {
        const response = await fetch("http://localhost:8080/api/bookings/payments/uid", {
            credentials: 'include',
        });
        const rsData = await response.json();

        if (rsData.resultCode.startsWith("2")) {
            return rsData.data;
        } else {
            throw new Error(`${rsData.msg}`);
        }
    } catch (error) {
        throw error;
    }
}