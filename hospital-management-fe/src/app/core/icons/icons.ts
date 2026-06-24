/**
 * Central registry mapping icon names → Font Awesome classes.
 * Use with <app-icon name="...">.
 */
export type IconName =
  | 'patients'
  | 'doctors'
  | 'specialities'
  | 'appointments'
  | 'appointmentSlots'
  | 'appointmentsClipboard'
  | 'appointmentsBook'
  | 'billing'
  | 'reports'
  | 'medicalRecords'
  | 'medicalRecord'
  | 'home'
  | 'logo'
  | 'logout'
  | 'plus'
  | 'back'
  | 'edit'
  | 'delete'
  | 'view'
  | 'save'
  | 'cancel'
  | 'search'
  | 'globe'
  | 'check';

export const FA_ICONS: Record<IconName, string> = {
  patients:              'fa-solid fa-hospital-user',
  doctors:               'fa-solid fa-user-doctor',
  specialities:          'fa-solid fa-stethoscope',
  appointments:          'fa-solid fa-calendar-check',
  appointmentSlots:      'fa-solid fa-calendar-days',
  appointmentsClipboard: 'fa-solid fa-clipboard-list',
  appointmentsBook:      'fa-solid fa-calendar-plus',
  billing:               'fa-solid fa-file-invoice-dollar',
  reports:               'fa-solid fa-chart-pie',
  medicalRecords:        'fa-solid fa-notes-medical',
  medicalRecord:         'fa-solid fa-file-medical',
  home:                  'fa-solid fa-house',
  logo:                  'fa-solid fa-hospital',
  logout:                'fa-solid fa-right-from-bracket',
  plus:                  'fa-solid fa-plus',
  back:                  'fa-solid fa-arrow-left',
  edit:                  'fa-solid fa-pen-to-square',
  delete:                'fa-solid fa-trash-can',
  view:                  'fa-solid fa-eye',
  save:                  'fa-solid fa-floppy-disk',
  cancel:                'fa-solid fa-xmark',
  search:                'fa-solid fa-magnifying-glass',
  globe:                 'fa-solid fa-globe',
  check:                 'fa-solid fa-circle-check',
};
