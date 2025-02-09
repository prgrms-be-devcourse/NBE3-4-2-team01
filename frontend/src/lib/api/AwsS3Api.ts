import { GetHotelResponse } from "../types/hotel/GetHotelResponse";
import { PageDto } from "../types/PageDto";
import { RsData } from "../types/RsData";

// Presigned URLs을 사용하여 AWS S3에 이미지 업로드
export const uploadImagesToS3 = async (presignedUrls: string[], images: File[]) => {
    try {
        await Promise.all(
            images.map((image, index) =>
                fetch(presignedUrls[index], {
                    method: 'PUT',
                    body: image,
                    headers: {
                        'Content-Type': image.type, // 이미지 파일 타입 지정
                    },
                }).then((uploadRes) => {
                    if (!uploadRes.ok) {
                        throw new Error(`이미지 업로드 실패: ${uploadRes.statusText}`);
                    }
                    console.log(`이미지 업로드 성공: ${presignedUrls[index]}`);
                })
            )
        )
    } catch(error) {
        console.error('Error:', error);
        throw error;
    }
}

// 호텔 목록 획득 API
export const getHotelList = async (
    page: number, 
    pageSize: number, 
    filterName: string, 
    streetAddress:string, 
    checkInDate:string, 
    checkoutDate:string, 
    personal:number,
    filterDirection?:string, 
    ) : Promise<PageDto<GetHotelResponse>> => {

        const params = new URLSearchParams({
            page: page.toString(),
            pageSize: pageSize.toString(),
            filterName: filterName,
            streetAddress: streetAddress,
            checkInDate: checkInDate,
            checkoutDate: checkoutDate,
            personal: personal.toString()
        });
    
        if (filterDirection) {
            params.append("filterDirection", filterDirection);
        }
    
        const url = `http://localhost:8080/api/hotels?${params.toString()}`;    

    try {
        console.log(url);
        const response = await fetch(url);
        const rsData : RsData<PageDto<GetHotelResponse>> = await response.json();
        console.log(rsData);
        if(rsData.resultCode !== '200-1') {
            throw new Error(rsData.msg);
        }
        return rsData.data;
    } catch(error) {
        console.error('Error:', error);
        throw error;
    }
}