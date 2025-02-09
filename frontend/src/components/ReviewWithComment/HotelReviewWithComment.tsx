import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { Star } from 'lucide-react';
import { HotelReviewResponse } from '@/lib/types/review/HotelReviewResponse';
import { postReviewComment, updateReviewComment, deleteReviewComment } from '@/lib/api/ReviewCommentApi';


interface ReviewWithCommentProps {
  review: HotelReviewResponse;
  isBusinessUser?: boolean;
  onCommentUpdate?: () => void;
}

export const HotelReviewWithComment: React.FC<ReviewWithCommentProps> = ({ 
  review, 
  isBusinessUser = false,
  onCommentUpdate 
}) => {
  console.log(isBusinessUser);
  const [comment, setComment] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  const getReviewData = (review: HotelReviewResponse) => {
    const { hotelReviewWithCommentDto, imageUrls } = review;
    return {
      title: hotelReviewWithCommentDto.memberEmail,
      roomTypeName: hotelReviewWithCommentDto.roomTypeName,
      reviewDto: hotelReviewWithCommentDto.reviewDto,
      reviewCommentDto: hotelReviewWithCommentDto.reviewCommentDto,
      createdAt: hotelReviewWithCommentDto.createdAt,
      imageUrls
    };
  };

  const {
    title,
    roomTypeName,
    reviewDto,
    reviewCommentDto,
    createdAt,
    imageUrls
  } = getReviewData(review);

  useEffect(() => {
    if (reviewCommentDto) {
      setComment(reviewCommentDto.content);
      setIsEditing(false);
    } else if (isBusinessUser) {
      // 답변이 없고 사업자 사용자인 경우 바로 편집 모드로 전환
      setIsEditing(true);
      setComment('');
    }
  }, [reviewCommentDto, isBusinessUser]);

  const handleSaveComment = async () => {
    if (!comment.trim()) return;
    
    setIsSaving(true);
    try {
      if (reviewCommentDto) {
        await updateReviewComment(reviewDto.reviewId, reviewCommentDto.reviewCommentId, comment);
      } else {
        await postReviewComment(reviewDto.reviewId, comment);
      }
      setIsEditing(false);
      onCommentUpdate?.();
    } catch (error) {
      if (error instanceof Error) {
        alert(error.message);
      }
    } finally {
      setIsSaving(false);
    }
  };

  const handleDeleteComment = async () => {
    if (!reviewCommentDto || !window.confirm('답변을 삭제하시겠습니까?')) return;
    
    try {
      await deleteReviewComment(reviewDto.reviewId, reviewCommentDto.reviewCommentId);
      setComment('');
      onCommentUpdate?.();
    } catch (error) {
      if (error instanceof Error) {
        alert(error.message);
      }
    }
  };

  const renderCommentSection = () => {
    if (!isBusinessUser) {
      if (reviewCommentDto) {
        return (
          <div className="mt-4 pl-4 border-l-4 border-gray-700 bg-gray-100 relative rounded-lg">
            <div className="absolute left-[-20px] top-1/2 transform -translate-y-1/2 w-0 h-0 border-l-[10px] border-l-transparent border-b-[10px] border-b-gray-100"></div>
            <div className="bg-gray-200 p-4 rounded">
              <p className="text-gray-700">{reviewCommentDto.content}</p>
              <p className="text-sm text-gray-500 mt-2">
                {new Date(reviewCommentDto.createdAt).toLocaleString()}
              </p>
            </div>
          </div>
        );
      }
      return null;
    }

    if (isEditing) {
      return (
        <div className="space-y-4">
          <Textarea
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="답변을 입력해주세요..."
            className="w-full min-h-[100px]"
          />
          <div className="flex gap-2">
            <Button
              onClick={handleSaveComment}
              disabled={!comment.trim() || isSaving}
            >
              {reviewCommentDto ? '수정' : '작성'}
            </Button>
            {reviewCommentDto && (
              <Button
                variant="outline"
                onClick={() => {
                  setIsEditing(false);
                  setComment(reviewCommentDto.content);
                }}
              >
                취소
              </Button>
            )}
          </div>
        </div>
      );
    }

    if (reviewCommentDto) {
      return (
        <div className="pl-4 border-l-4 border-gray-700 bg-gray-100 relative rounded-lg">
          <div className="absolute left-[-20px] top-1/2 transform -translate-y-1/2 w-0 h-0 border-l-[10px] border-l-transparent border-b-[10px] border-b-gray-100"></div>
          <div className="bg-gray-200 p-4 rounded">
            <p className="text-gray-700">{reviewCommentDto.content}</p>
            <p className="text-sm text-gray-500 mt-2">
              {new Date(reviewCommentDto.createdAt).toLocaleString()}
            </p>
            <div className="flex justify-end gap-2">
              <Button className="bg-blue-500" onClick={() => setIsEditing(true)}>수정</Button>
              <Button variant="destructive" onClick={handleDeleteComment}>
                삭제
              </Button>
            </div>
          </div>
        </div>
      );
    }

    return null;
  };

  return (
    <Card className="mb-4">
      <CardHeader>
        <div className="flex justify-between items-center">
          <div>
            <h3 className="text-lg font-semibold">{title}</h3>
            <p className="text-sm text-gray-500">
              {roomTypeName} • {new Date(createdAt).toLocaleDateString()}
            </p>
          </div>
          <div className="flex items-center">
            {[...Array(5)].map((_, index) => (
              <Star
                key={index}
                className={`w-5 h-5 ${
                  index < reviewDto.rating
                    ? 'text-yellow-400 fill-yellow-400'
                    : 'text-gray-300'
                }`}
              />
            ))}
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {/* Review Content */}
        <div className="mb-4">
          <p className="text-gray-700">{reviewDto.content}</p>
          <p className="text-sm text-gray-500 mt-2">
            {new Date(reviewDto.createdAt).toLocaleString()}
          </p>
          
          {/* Review Images */}
          {imageUrls.length > 0 && (
            <div className="grid grid-cols-3 gap-2 mt-4">
              {imageUrls.map((url, index) => (
                <img
                  key={index}
                  src={url}
                  alt={`Review image ${index + 1}`}
                  className="w-full h-32 object-cover rounded"
                />
              ))}
            </div>
          )}
        </div>

        {/* Comment Section */}
        <div className="mt-4">
          {renderCommentSection()}
        </div>
      </CardContent>
    </Card>
  );
};

export default HotelReviewWithComment;