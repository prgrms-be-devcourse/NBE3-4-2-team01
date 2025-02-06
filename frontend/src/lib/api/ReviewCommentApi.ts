import { ReviewCommentDto } from "../types/ReviewCommentDto";
import { RsData } from "../types/RsData";

export const postReviewComment = async (reviewId: string, content: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/comments`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content }),
      });
  
      const rsData = await response.json();
      if(rsData.resultCode !== '200') {
          throw new Error(rsData.msg);
      }
    } catch (error) {
      throw error;
    }
  };

  export const deleteReviewComment = async (reviewId: string, commentId: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/comments/${commentId}`, {
        method: 'DELETE',
      });
  
      
      const rsData = await response.json();
      if(rsData.resultCode !== '200') {
          throw new Error(rsData.msg);
      } 

    } catch (error) {
        throw error;
      }
  }

  export const fetchReviewComment = async (reviewId: string, commentId: string) : Promise<ReviewCommentDto> => {
    try {
      const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/comments/${commentId}`);
      const rsData: RsData<ReviewCommentDto> = await response.json();
      
      if(rsData.resultCode !== '200') {
          throw new Error(rsData.msg);
      }
      return rsData.data;
    } catch (error) {
      throw error;
    }
  }

  export const updateReviewComment = async (reviewId: string, commentId: string, content: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reviews/${reviewId}/comments/${commentId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content }),
      });
  
      const rsData = await response.json();
      if(rsData.resultCode !== '200') {
          throw new Error(rsData.msg);
      }
    } catch (error) {
      throw error;
    }
  }