import { ReviewCommentDto } from "../types/ReviewCommentDto";
import { RsData } from "../types/RsData";

export const postReviewComment = async (reviewId: number, content: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/comments`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content }),
      });
  
      const rsData = await response.json();
      if(rsData.resultCode !== '200-1') {
          throw new Error(rsData.msg);
      }
    } catch (error) {
      throw error;
    }
  };

  export const deleteReviewComment = async (reviewId: number, commentId: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/comments/${commentId}`, {
        method: 'DELETE',
        credentials: 'include',
      });
  
      
      const rsData = await response.json();
      if(rsData.resultCode !== '200-1') {
          throw new Error(rsData.msg);
      } 

    } catch (error) {
        throw error;
      }
  }

  export const fetchReviewComment = async (reviewId: number, commentId: number) : Promise<ReviewCommentDto> => {
    try {
      const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/comments/${commentId}`, {
        credentials: 'include',
      });
      const rsData: RsData<ReviewCommentDto> = await response.json();
      
      if(rsData.resultCode !== '200-1') {
          throw new Error(rsData.msg);
      }
      return rsData.data;
    } catch (error) {
      throw error;
    }
  }

  export const updateReviewComment = async (reviewId: number, commentId: number, content: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/comments/${commentId}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content }),
      });
  
      const rsData = await response.json();
      if(rsData.resultCode !== '200-1') {
          throw new Error(rsData.msg);
      }
    } catch (error) {
      throw error;
    }
  }