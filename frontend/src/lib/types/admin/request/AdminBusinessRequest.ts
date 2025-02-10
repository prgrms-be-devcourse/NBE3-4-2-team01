import { BusinessApprovalStatus } from "../../business/BusinessApprovalStatus";

export interface AdminBusinessRequest {
  businessRegistrationNumber: number;
  startDate: string;
  ownerName: string;
  businessApprovalStatus: BusinessApprovalStatus;
}
