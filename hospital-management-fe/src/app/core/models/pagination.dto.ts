/** Query params for paginated list endpoints (0-based page index). */
export interface PageRequest {
  page?: number;
  size?: number;
}

/** Paginated list response from the API. */
export interface PageResponse<T> {
  data: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}
