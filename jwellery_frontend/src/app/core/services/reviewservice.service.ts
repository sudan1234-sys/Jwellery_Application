import { Injectable } from '@angular/core'; 
import { HttpClient } from '@angular/common/http'; 
import { environment } from '../../../environments/environment';
 import { reviewDto } from '../models/reviewDto'; 
 @Injectable({ providedIn: 'root' })
  export class ReviewserviceService {
    
    constructor(private http: HttpClient) { } 

  getReviewsByProductId(productId: number) {
  return this.http.get<reviewDto[]>(`${environment.apiUrl}/getReviewsById`, {
    params: { productId: productId.toString() } // must be string
  });
}


 }
  