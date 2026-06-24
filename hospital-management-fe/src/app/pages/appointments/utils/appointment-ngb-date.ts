import { NgbDate } from '@ng-bootstrap/ng-bootstrap';

/**
 * Hospital datepicker defaults maxDate to today when omitted; use this for future appointment dates.
 */
export const APPOINTMENT_MAX_NGB = new NgbDate(2100, 12, 31);
