import type { AppointmentType } from '../../../appointments/models/request/appointment-type.dto';

export type WeekDay =
  | 'Monday'
  | 'Tuesday'
  | 'Wednesday'
  | 'Thursday'
  | 'Friday'
  | 'Saturday'
  | 'Sunday';

export const WEEK_DAYS: WeekDay[] = [
  'Monday',
  'Tuesday',
  'Wednesday',
  'Thursday',
  'Friday',
  'Saturday',
  'Sunday',
];

export interface GenerateSlotsRequest {
  start: string;
  end: string;
  dayStart: string;
  dayEnd: string;
  doctorId: number;
  duration: number;
  days: WeekDay[];
  appointmentType: AppointmentType;
}
