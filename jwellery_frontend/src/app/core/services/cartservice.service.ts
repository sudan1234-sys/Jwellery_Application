// src/app/core/services/cartservice.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CartItemDTO {
  productId: number;
  image: string;
  productName: string;
  price: number;
  quantity: number;
  subtotal: number;
}

export interface CartDTO {
  cartId: number;
  userId: number;
  status: string;
  items: CartItemDTO[];
  total: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartserviceService {
  private http = inject(HttpClient);
  // Ensure this matches your Spring Boot base path
  private baseUrl = 'http://localhost:8080/api/users';

  constructor() { }

  // Add item (increment by X). Useful when adding from product listing.
  addToCart(userId: number, productId: number, quantity: number): Observable<CartDTO> {
    const params = new HttpParams()
      .set('productId', productId.toString())
      .set('quantity', quantity.toString());

    // POST /api/users/{userId}/cart/items?productId=...&quantity=...
    return this.http.post<CartDTO>(`${this.baseUrl}/${userId}/cart/items`, null, { params });
  }

  // Remove product from cart
  removeFromCart(userId: number, productId: number): Observable<CartDTO> {
    // DELETE /api/users/{userId}/cart/items/{productId}
    return this.http.delete<CartDTO>(`${this.baseUrl}/${userId}/cart/items/${productId}`);
  }

  // Get cart for a user
  getCart(userId: number): Observable<CartDTO> {
    // GET /api/users/{userId}/cart
    return this.http.get<CartDTO>(`${this.baseUrl}/${userId}/cart`);
  }

  // Update quantity to an explicit value (PUT). Backend: updateProductQuantity
  updateQuantity(userId: number, productId: number, quantity: number): Observable<CartDTO> {
    const params = new HttpParams()
      .set('productId', productId.toString())
      .set('quantity', quantity.toString());

    // PUT /api/users/{userId}/cart/items?productId=...&quantity=...
    return this.http.put<CartDTO>(`${this.baseUrl}/${userId}/cart/items`, null, { params });
  }
}
