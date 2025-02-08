import { BedTypeNumber } from "./BedTypeNumber";
import { RoomStatus } from "./RoomStatus";

export interface PostRoomRequest {
  roomName: string;
  roomNumber: number;
  basePrice: number;
  standardNumber: number;
  maxNumber: number;
  bedTypeNumber: BedTypeNumber;
  imageExtensions: string[];
  roomOptions: Set<string>;
}
