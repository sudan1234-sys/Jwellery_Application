import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDTO } from '../models/product.dto';
import { environment } from '../../../environments/environment';
import { HttpParams } from '@angular/common/http';
import { PaginatedResponse } from '../models/paginated-response.model';
export interface CategoryProducts {
  category: string;
  products: ProductDTO[];
}
export interface ProductFilter {
  category?: number | null;
  minPrice?: number | null;
  maxPrice?: number | null;
  page?: number;
  size?: number;
  search?: string | null;
}
@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/products`; // 🔹 adjust if your backend runs elsewhere

  /**
   * Get all products with pagination and optional filters
   */
  getProducts(params?: {
    
    categoryId?: number;
    minPrice?: number;
    maxPrice?: number;
    search?: string;
    page?: number;
    size?: number;
    sort?: string;
  }): Observable<{ content: ProductDTO[]; totalElements: number }> {
    return this.http.get<{ content: ProductDTO[]; totalElements: number }>(
      this.apiUrl,
      { params: params as any }
    );
  }

  /**
   * Get a single product by its ID
   */
  getProductById(id: number): Observable<ProductDTO> {
  return this.http.get<ProductDTO>(`${environment.apiUrl}/product/${id}`);
}

  /**
   * Get featured products for homepage
   */
  getFeaturedProducts(): Observable<ProductDTO[]> {
    return this.http.get<ProductDTO[]>(`${this.apiUrl}/featured`);
  }
  getProductsByCategory(): Observable<CategoryProducts[]> {
  return this.http.get<CategoryProducts[]>(`${environment.apiUrl}/getProductbycategory`);
}
// src/app/core/services/product.service.ts
// Fetch products with page and size
  getAllProducts(page: number = 0, size: number = 20): Observable<PaginatedResponse<ProductDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PaginatedResponse<ProductDTO>>(`${environment.apiUrl}/allProducts`, { params });
  }
getProductsByFilter(filters: ProductFilter): Observable<PaginatedResponse<ProductDTO>> {
    let params = new HttpParams();

    if (filters.category) params = params.set('categoryId', filters.category);
    if (filters.minPrice != null) params = params.set('minPrice', filters.minPrice.toString());
    if (filters.maxPrice != null) params = params.set('maxPrice', filters.maxPrice.toString());
    if (filters.search != null) params = params.set('search', filters.search);
    if (filters.page != null) params = params.set('page', filters.page.toString());
    if (filters.size != null) params = params.set('size', filters.size.toString());

    return this.http.get<PaginatedResponse<ProductDTO>>(`${environment.apiUrl}/products/filter`, { params });
}
}