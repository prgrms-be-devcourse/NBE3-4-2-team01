'use client';

import { useState, useEffect } from 'react';
import { useSearchParams, useParams, useRouter } from 'next/navigation';
import { PresignedUrlsResponse } from '@/lib/types/review/PresignedUrlsResponse';
import { PostReviewRequest } from '@/lib/types/review/PostReviewRequest';
import { postReview } from '@/lib/api/ReviewApi';
import { uploadImagesToS3 } from '@/lib/api/AwsS3Api';
import { uploadImageUrls } from '@/lib/api/ReviewApi';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';

export default function CreateReviewPage() {
  const [content, setContent] = useState('');
  const [rating, setRating] = useState(0);
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [presignedUrls, setPresignedUrls] = useState<string[]>([]);
  const [bookingId, setBookingId] = useState(0);
  const [hotelId, setHotelId] = useState(0);
  const [roomId, setRoomId] = useState(0);
  const [reviewId, setReviewId] = useState(0);
  const searchParams = useSearchParams();
  const params = useParams();
  const router = useRouter();

  useEffect(() => {
    setBookingId(Number(params.bookingId));
    setHotelId(Number(searchParams.get('hotelId')));
    setRoomId(Number(searchParams.get('roomId')));
  }, [searchParams]);

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const selectedFiles = Array.from(e.target.files);
      setImages(selectedFiles);
      
      // 미리보기 업데이트
      const previews = selectedFiles.map(file => URL.createObjectURL(file));
      setImagePreviews(previews);
    }
  };

  // 리뷰 생성 요청 (Presigned URLs 받기)
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    const requestBody: PostReviewRequest = {
      content,
      rating,
      imageExtensions: images.map(file => file.name.split('.').pop()?.toLowerCase() || ''),
    };
  
    try {
      const presignedUrlResponse: PresignedUrlsResponse = await postReview(bookingId, hotelId, roomId, requestBody);
      setPresignedUrls(presignedUrlResponse.presignedUrls);
      setReviewId(presignedUrlResponse.reviewId);
      console.log(presignedUrlResponse);
      await submitImages();
      console.log('리뷰가 성공적으로 생성되었습니다.');
    } catch (error) {
      alert('리뷰 생성 또는 이미지 업로드 중 오류가 발생했습니다.');
    }
  };

  // Presigned URLs을 사용하여 이미지 업로드
  const submitImages = async () => {
    if (presignedUrls.length === 0) {
      alert('이미지 업로드 URL을 가져오지 못했습니다.');
      return;
    }

    try {
      await uploadImagesToS3(presignedUrls, images);
      await saveImageUrls();
      console.log('이미지가 성공적으로 업로드되었습니다.');
    } catch (error) {
      console.error('Error:', error);
      alert('이미지 업로드 중 오류가 발생했습니다.');
    }
  }

  // 사진 조회용 URL들 서버로 전달
  const saveImageUrls = async () => {
    const viewUrls = presignedUrls.map((presignedUrl) => {
      return presignedUrl.split('?')[0];
    });

    try {
      await uploadImageUrls(reviewId, viewUrls);
      alert('리뷰가 성공적으로 생성되었습니다.');
      router.push('/me/reviews/');
    } catch (error) {
      console.error('Error:', error);
      alert('이미지 URL 저장 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="p-4">
      <Card>
        <CardHeader>
          <CardTitle>리뷰 생성</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <Label htmlFor="content" className="block mb-2">내용</Label>
              <Textarea
                id="content"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                className="min-h-32 border p-2 w-full"
                placeholder="리뷰 내용을 입력해주세요"
                required
              />
            </div>

            <div>
              <Label htmlFor="rating" className="block mb-2">평점</Label>
              <div className="flex gap-1">
                {[1, 2, 3, 4, 5].map((value) => (
                  <button
                    key={value}
                    type="button"
                    onClick={() => setRating(value)}
                    className="text-2xl focus:outline-none"
                  >
                    {value <= rating ? (
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="24"
                        height="24"
                        viewBox="0 0 24 24"
                        fill="#FFD700"
                        stroke="#FFD700"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                      >
                        <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
                      </svg>
                    ) : (
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="24"
                        height="24"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="#FFD700"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                      >
                        <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
                      </svg>
                    )}
                  </button>
                ))}
                <span className="ml-2 text-gray-600">
                  {rating ? `${rating}점` : ''}
                </span>
              </div>
            </div>

            <div>
              <Label htmlFor="images" className="block mb-2">이미지</Label>
              <Input
                id="images"
                type="file"
                multiple
                accept="image/*"
                onChange={handleImageUpload}
                className="cursor-pointer border p-2 w-full"
              />
              {imagePreviews.length > 0 && (
                <div className="mt-4 grid grid-cols-3 gap-4">
                  {imagePreviews.map((preview, index) => (
                    <div key={index} className="relative">
                      <img
                        src={preview}
                        alt={`미리보기 이미지 ${index + 1}`}
                        className="w-full h-32 object-cover rounded-md"
                      />
                    </div>
                  ))}
                </div>
              )}
            </div>

            <Button type="submit" className="w-full bg-blue-500 text-white mt-4">
              리뷰 생성
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
