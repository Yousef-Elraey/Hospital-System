import type { DoctorSpeciality } from '../models/response/doctor-response.dto';

export function specialityDisplayName(
  speciality: DoctorSpeciality | null | undefined,
  lang: string,
): string {
  if (!speciality) return '-';
  const isAr = lang === 'ar';
  const en = speciality.nameEn ?? speciality.name_en ?? '';
  const ar = speciality.nameAr ?? speciality.name_ar ?? '';
  const name = isAr ? ar : en;
  return name || en || ar || '-';
}
