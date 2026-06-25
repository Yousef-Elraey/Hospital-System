export const DEFAULT_PAGE_SIZE_OPTIONS = [10, 20, 50] as const;

export function paginate<T>(items: T[], page: number, pageSize: number): T[] {
  const start = (page - 1) * pageSize;
  return items.slice(start, start + pageSize);
}

export function totalPages(count: number, pageSize: number): number {
  return Math.max(1, Math.ceil(count / pageSize));
}

export function pageNumbers(count: number, pageSize: number): number[] {
  return Array.from({ length: totalPages(count, pageSize) }, (_, i) => i + 1);
}

export function clampPage(page: number, count: number, pageSize: number): number {
  return Math.min(page, totalPages(count, pageSize));
}
