export const BED_TYPES = [
  "single",
  "double",
  "queen",
  "king",
  "twin",
  "triple",
] as const;

export type BedType = (typeof BED_TYPES)[number];

export interface BedTypeNumber {
  single: number;
  double: number;
  queen: number;
  king: number;
  twin: number;
  triple: number;
}
