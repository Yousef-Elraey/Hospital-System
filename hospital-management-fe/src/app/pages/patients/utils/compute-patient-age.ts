/** Age in full years from an API date-of-birth string (YYYY-MM-DD). */
export function computePatientAge(dateOfBirth?: string | null): number | null {
  if (!dateOfBirth) return null;
  const iso = dateOfBirth.slice(0, 10);
  if (!/^\d{4}-\d{2}-\d{2}$/.test(iso)) return null;
  const [y, m, d] = iso.split('-').map(Number);
  const today = new Date();
  let age = today.getFullYear() - y;
  const monthDiff = today.getMonth() + 1 - m;
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < d)) age--;
  return age >= 0 ? age : null;
}
