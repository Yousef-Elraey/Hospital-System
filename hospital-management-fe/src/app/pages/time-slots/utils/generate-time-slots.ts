import { addDays, format, isValid, isWeekend, parseISO } from 'date-fns';
import { combineSlotIsoDateAndTime } from './slot-date-form';
import type { GenerateSlotsRequest } from '../models/request/generate-slots-request.dto';
import type { AppointmentSlotStatus } from '../models/request/appointment-slot-status.dto';

export interface SlotDraft {
  doctorId: number;
  startTime: string;
  endTime: string;
  status: AppointmentSlotStatus;
}

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

export function generateTimeSlots(request: GenerateSlotsRequest): SlotDraft[] {
  const startDate = parseISO(request.startDate);
  const endDate = parseISO(request.endDate);
  const dayStart = parseTimeToMinutes(request.dailyStartTime);
  const dayEnd = parseTimeToMinutes(request.dailyEndTime);
  const duration = request.slotDurationMinutes;

  if (!isValid(startDate) || !isValid(endDate) || dayStart == null || dayEnd == null || duration <= 0) {
    return [];
  }
  if (endDate < startDate || dayEnd <= dayStart) {
    return [];
  }

  const slots: SlotDraft[] = [];
  let currentDay = startDate;

  while (currentDay <= endDate) {
    if (!request.excludeWeekends || !isWeekend(currentDay)) {
      const dateIso = format(currentDay, 'yyyy-MM-dd');
      for (let minute = dayStart; minute + duration <= dayEnd; minute += duration) {
        const startTime = combineSlotIsoDateAndTime(dateIso, minutesToTime(minute));
        const endTime = combineSlotIsoDateAndTime(
          dateIso,
          minutesToTime(minute + duration),
        );
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
