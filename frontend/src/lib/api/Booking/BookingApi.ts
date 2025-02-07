import { BookingRequest } from "@/lib/types/Booking/BookingRequest";
import { BookingResponseDetails } from "@/lib/types/Booking/BookingResponseDetails";
import { BookingResponseSummary } from "@/lib/types/Booking/BookingResponseSummary";
import { PageDto } from "@/lib/types/PageDto";

// 예약 및 결제
export const book = async function(bookingRequest : BookingRequest) : Promise<BookingResponseDetails> {
    try {
        const response = await fetch("http://localhost:8080/api/bookings", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(bookingRequest)
        });

        const rsData = await response.json();

        if (rsData.resultCode.startsWith("2")) {
            return rsData.data;
        } else {
            throw new Error(`에러 발생 : ${rsData.resultCode}, ${rsData.msg}`);
        }
    } catch (error) {
        throw error;
    }
};

// 전체 예약 조회 (관리자측, 미사용)
export const getAllBookings = async function(page? : number, pageSize? : number) : Promise<PageDto<BookingResponseSummary>> {
    try {
        const params = new URLSearchParams();

        if (page) {
            params.append("page", page.toString());
        }
        if (pageSize) {
            params.append("page_size", pageSize.toString());
        }

        const response = await fetch(`http://localhost:8080/api/bookings?${params.toString()}`);
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

// 내 예약 조회 (사용자측)
export const getMyBookings = async function(page? : number, pageSize? : number) : Promise<PageDto<BookingResponseSummary>> {
    try {
        const params = new URLSearchParams();

        if (page) {
            params.append("page", page.toString());
        }
        if (pageSize) {
            params.append("page_size", pageSize.toString());
        }

        const response = await fetch(`http://localhost:8080/api/bookings/me?${params.toString()}`);
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

// 호텔 예약 조회 (사업자측)
export const getHotelBookings = async function(page? : number, pageSize? : number) : Promise<PageDto<BookingResponseSummary>> {
    try {
        const params = new URLSearchParams();

        if (page) {
            params.append("page", page.toString());
        }
        if (pageSize) {
            params.append("page_size", pageSize.toString());
        }

        const response = await fetch(`http://localhost:8080/api/bookings/myHotel?${params.toString()}`);
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

// 예약 상세 조회
export const getBookingDetails = async function(bookingId : number) : Promise<BookingResponseDetails> {
    try {
        const response = await fetch(`http://localhost:8080/api/bookings/${bookingId}`);
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

// 예약 취소
export const cancelBooking = async function(bookingId : number) : Promise<BookingResponseDetails> {
    try {
        const response = await fetch(`http://localhost:8080/api/bookings/${bookingId}`, {
            method: "DELETE"
        });

        const rsData = await response.json();

        if (rsData.resultCode.startsWith("2")) {
            return rsData.data;
        } else {
            throw new Error(`에러 발생 : ${rsData.resultCode}, ${rsData.msg}`);
        }
    } catch (error) {
        throw error;
    }
};

// 예약 완료 처리
export const completeBooking = async function(bookingId : number) : Promise<BookingResponseDetails> {
    try {
        const response = await fetch(`http://localhost:8080/api/bookings/${bookingId}`, {
            method: "PATCH"
        });

        const rsData = await response.json();

        if (rsData.resultCode.startsWith("2")) {
            return rsData.data;
        } else {
            throw new Error(`에러 발생 : ${rsData.resultCode}, ${rsData.msg}`);
        }
    } catch (error) {
        throw error;
    }
};