import {
  AdminBusinessApprovalReponse,
  AdminBusinessDetailResponse,
  AdminBusinessSummaryReponse,
} from "@/lib/types/admin/response/AdminBusinessResponse";
import { fetchAPI } from "../global/FetchApi";
import { AdminBusinessRequest } from "@/lib/types/admin/request/AdminBusinessRequest";
import { FetchOptions } from "@/lib/types/global/FetchOption";
import { PageDto } from "@/lib/types/PageDto";

export const getAllBusinesses = async (): Promise<
  PageDto<AdminBusinessSummaryReponse>
> => {
  return fetchAPI<PageDto<AdminBusinessSummaryReponse>>(
    "http://localhost:8080/api/admin/businesses"
  );
};

export const getBusiness = async (
  businessId: number
): Promise<AdminBusinessDetailResponse> => {
  return fetchAPI<AdminBusinessDetailResponse>(
    `http://localhost:8080/api/admin/businesses/${businessId}`
  );
};

export const modifyBusiness = async (
  businessId: number,
  formData: AdminBusinessRequest
): Promise<AdminBusinessApprovalReponse> => {
  const options: FetchOptions = {
    method: "PATCH",
    body: formData,
  };

  return fetchAPI<AdminBusinessApprovalReponse>(
    `http://localhost:8080/api/admin/businesses/${businessId}`,
    options
  );
};
