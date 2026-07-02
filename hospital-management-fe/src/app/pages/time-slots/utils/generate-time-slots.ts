import { addDays, format, isValid, parseISO } from 'date-fns';
import { combineSlotIsoDateAndTime } from './slot-date-form';
import type { GenerateSlotsRequest, WeekDay } from '../models/request/generate-slots-request.dto';
import type { AppointmentSlotStatus } from '../models/request/appointment-slot-status.dto';

export interface SlotDraft {
  doctorId: number;
  startTime: string;
  endTime: string;
  status: AppointmentSlotStatus;
}

const JS_DAY_TO_WEEKDAY: WeekDay[] = [
  'Sunday',
  'Monday',
  'Tuesday',
  'Wednesday',
  'Thursday',
  'Friday',
  'Saturday',
];

function parseTimeToMinutes(time: string): number | null {
  const match = /^(\d{1,2}):(\d{2})$/.exec(time.trim());
  if (!match) return null;
  const hours = Number(match[1]);
  const minutes = Number(match[2]);
  if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) return null;
  return hours * 60 + minutes;
}

function minutesToTime(totalMinutes: number): string {
  const hours = Math.floor(totalMinutes / 60);
  const minutes = totalMinutes % 60;
  return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
}

function weekdayName(date: Date): WeekDay {
  return JS_DAY_TO_WEEKDAY[date.getDay()];
}

export function generateTimeSlots(request: GenerateSlotsRequest): SlotDraft[] {
  const startDate = parseISO(request.dayStart);
  const endDate = parseISO(request.dayEnd);
  const dayStart = parseTimeToMinutes(request.start);
  const dayEnd = parseTimeToMinutes(request.end);
  const duration = request.duration;
  const selectedDays = new Set(request.days);

  if (!isValid(startDate) || !isValid(endDate) || dayStart == null || dayEnd == null || duration <= 0) {
    return [];
  }
  if (endDate < startDate || dayEnd <= dayStart || selectedDays.size === 0) {
    return [];
  }

  const slots: SlotDraft[] = [];
  let currentDay = startDate;

  while (currentDay <= endDate) {
    if (selectedDays.has(weekdayName(currentDay))) {
      const dateIso = format(currentDay, 'yyyy-MM-dd');
      for (let minute = dayStart; minute + duration <= dayEnd; minute += duration) {
        const startTime = combineSlotIsoDateAndTime(dateIso, minutesToTime(minute));
        const endTime = combineSlotIsoDateAndTime(dateIso, minutesToTime(minute + duration));
        if (!startTime || !endTime) continue;
        slots.push({
          doctorId: request.doctorId,
          startTime,
          endTime,
          status: 'AVAILABLE',
        });
      }
    }
    currentDay = addDays(currentDay, 1);
  }

  return slots;
}

export function estimateSlotCount(request: GenerateSlotsRequest): number {
  return generateTimeSlots(request).length;
}
