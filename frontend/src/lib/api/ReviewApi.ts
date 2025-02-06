import { Empty } from "../types/Empty";
import { GetReviewResponse } from "../types/GetReviewResponse";
import { HotelReviewListResponse } from "../types/HotelReviewListResponse";
import { MyReviewResponse } from "../types/MyReviewResponse";
import { PageDto } from "../types/PageDto";
import { PostReviewRequest } from "../types/PostReviewRequest"
import { PresignedUrlsResponse } from "../types/PresignedUrlsResponse";
import { RsData } from "../types/RsData";
import { UpdateReviewRequest } from "../types/UpdateReviewRequest";

// 리뷰 생성 요청 후 PresignedUrlReponse 응답
export const postReview = async (
    bookingId: string, 
    hotelId: string,
    roomId: string, 
    postReviewRequest: PostReviewRequest) : Promise<PresignedUrlsResponse> => {
    
        try{
            const response = await fetch(`http://localhost:8080/api/reviews/${bookingId}?hotelId=${hotelId}&roomId=${roomId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(postReviewRequest),
            });

            const rsData = await response.json();
            if(rsData.resultCode !== '200') {
                throw new Error(rsData.msg);
            }   
            return rsData.data;
        } catch (error) {
            throw error;
        }
}

// 이미지 url 백엔드 서버에 업로드
export const uploadImageUrls = async (reviewId: number, viewUrls: string[]) => {
    try {
        const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/urls`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(viewUrls),
        })

        const rsData : RsData<Empty> = await response.json();
        if(rsData.resultCode !== '200') {
            throw new Error(rsData.msg);
        }
    } catch (error) {
        throw error;
    }
}

// 리뷰 삭제
export const deleteReview = async (reviewId: number) => {
    try {
        const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}`, {
            method: 'DELETE',
        });

        const rsData = await response.json();
        if (rsData.resultCode !== '200') {
            throw new Error(rsData.msg);
        }
    } catch (error) {
        throw error;
    }
}

// 호텔 리뷰 목록 조회
export const getHotelReviews = async (hotelId: number, page: number) : Promise<HotelReviewListResponse> => {
    try {
        const response = await fetch(`http://localhost:8080/api/reviews/hotels/${hotelId}?page=${page}`);
        
        const rsData = await response.json();
        if(rsData.resultCode !== '200') {
            throw new Error(rsData.msg);
        }
        return rsData.data;
    } catch (error) {
        throw error;
    }
}

// 내 리뷰 목록 조회
export const getMyReviews = async (page: number) : Promise<PageDto<MyReviewResponse>> => {
    try {
        const response = await fetch(`http://localhost:8080/api/reviews/me?page=${page}`);
        const rsData = await response.json();
        if(rsData.resultCode !== '200') {
            throw new Error(rsData.msg);
        }
        return rsData.data;
    } catch (error) {
        throw error;
    }
}

// 단건 리뷰 조회
export const fetchReview = async (reviewId: number) : Promise<GetReviewResponse> => {
    try {
        const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}`);
        const rsData = await response.json();
        if(rsData.resultCode !== '200') {
            throw new Error(rsData.msg);
        }
        return rsData.data;
    } catch (error) {
        throw error;
    }
}

// 리뷰 업데이트 요청 후 PresigenedUrlsReponse 응답
export const updateReview = async (reviewId: number, updateReviewRequest: UpdateReviewRequest) : Promise<PresignedUrlsResponse> => {
    try {
        const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updateReviewRequest),
        });

        const rsData = await response.json();
        if(rsData.resultCode !== '200') {
            throw new Error(rsData.msg);
        }
        return rsData.data;
    } catch (error) {
        throw error;
    }
}
