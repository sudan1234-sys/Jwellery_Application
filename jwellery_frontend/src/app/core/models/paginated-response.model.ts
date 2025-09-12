// models/paginated-response.model.ts
export interface PaginatedResponse<T> {
  content: T[];           // actual list of items
  totalPages: number;     // total number of pages
  totalElements: number;  // total items in DB
  number: number;         // current page number
  size: number;           // page size
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
  pageable: any;          // optional: page info like sort
}
