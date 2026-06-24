export interface GenerateSlotsRequest {
  doctorId: number;
  startDate: string;
  endDate: string;
  dailyStartTime: string;
  dailyEndTime: string;
  slotDurationMinutes: number;
  excludeWeekends: boolean;
}
