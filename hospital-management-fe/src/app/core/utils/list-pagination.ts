import type { PageRequest, PageResponse } from '../models/pagination.dto';

export const DEFAULT_PAGE_SIZE_OPTIONS = [10, 20, 50] as const;
export const DROPDOWN_FETCH_SIZE = 100;
export const STATS_FETCH_SIZE = 1000;
export const DROPDOWN_PAGE_REQUEST: PageRequest = { page: 0, size: DROPDOWN_FETCH_SIZE };
export const STATS_PAGE_REQUEST: PageRequest = { page: 0, size: STATS_FETCH_SIZE };

/** Maps 1-based UI page to API query params. */
export function toPageRequest(currentPage: number, pageSize: number): PageRequest {
  return { page: currentPage - 1, size: pageSize };
}

/** Applies a paginated API response to list component state. */
export function applyPageResponse<T>(
  response: PageResponse<T>,
  defaults: { pageSize: number },
): { list: T[]; totalElements: number; currentPage: number; pageSize: number } {
  return {
    list: response.data ?? [],
    totalElements: response.totalElements ?? 0,
    currentPage: (response.page ?? 0) + 1,
    pageSize: response.size ?? defaults.pageSize,
  };
}

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
