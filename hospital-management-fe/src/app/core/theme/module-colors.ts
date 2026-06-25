import type { IconName } from '../icons/icons';

/** Hex module colors — used for inline styles (gradients need real color values). */
export const MODULE_COLORS: Record<string, string> = {
  patients: '#0d9488',
  doctors: '#6366f1',
  appointments: '#0284c7',
  appointmentSlots: '#7c3aed',
  appointmentsClipboard: '#0284c7',
  appointmentsBook: '#0284c7',
  billing: '#059669',
  medicalRecords: '#d97706',
  medicalRecord: '#d97706',
  specialities: '#db2777',
  reports: '#4f46e5',
  home: '#4f46e5',
  logo: '#4f46e5',
};

export const CHART_COLORS = {
  primary: '#0d9488',
  secondary: '#6366f1',
  info: '#0284c7',
  success: '#059669',
  warning: '#d97706',
  danger: '#dc2626',
  accent: '#db2777',
  palette: ['#0d9488', '#6366f1', '#0284c7', '#059669', '#d97706', '#db2777'],
  doughnut: ['#0d9488', '#db2777', '#6366f1'],
  payment: ['#059669', '#d97706', '#dc2626', '#0284c7'],
} as const;

export function moduleColorForIcon(iconName: IconName): string {
  return MODULE_COLORS[iconName] ?? '#0d9488';
}

/** Inline style for a vibrant icon tile — works reliably in all browsers. */
export function moduleIconStyle(color: string): Record<string, string> {
  return {
    background: `linear-gradient(145deg, ${color}, ${color}d9)`,
    boxShadow: `0 6px 18px ${color}40`,
  };
}
