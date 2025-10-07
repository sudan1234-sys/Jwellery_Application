// src/app/features/product-detail/product-detail.component.ts
import {
  Component,
  OnInit,
  inject,
  signal,
} from '@angular/core';
import { ActivatedRoute, RouterLink , Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgOptimizedImage } from '@angular/common';
import { ProductService } from '../../core/services/product.service.ts.service';
import { ProductDTO } from '../../core/models/product.dto';
import { reviewDto } from '../../core/models/reviewDto';
import { ReviewserviceService } from '../../core/services/reviewservice.service';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-detail',
  imports: [NgOptimizedImage, CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly reviewService = inject(ReviewserviceService);
  private readonly route = inject(ActivatedRoute);

  readonly product = signal<ProductDTO | null>(null);
  readonly reviews = signal<reviewDto[]>([]);
  readonly selectedImage = signal<string | null>(null);
  readonly loadingProduct = signal(false);
  readonly loadingReviews = signal(false);
  reviewForm: FormGroup = new FormGroup({});

  constructor(private readonly router: Router) {}
  ngOnInit(): void {
    
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    if (!productId) {
      console.error('Invalid product ID');
      return;
    }

    this.loadProduct(productId);
    this.loadReviews(productId);
  }

  // Load product details
  private loadProduct(productId: number): void {
    this.loadingProduct.set(true);
    this.productService.getProductById(productId).subscribe({
      next: (p) => {
        this.product.set(p);
        if (p.imageUrls?.length) {
          this.selectedImage.set(p.imageUrls[0]);
        }
      },
      error: (err) => console.error('Failed to load product', err),
      complete: () => this.loadingProduct.set(false),
    });
  }

  // Load reviews for the product
  private loadReviews(productId: number): void {
    this.loadingReviews.set(true);
    this.reviewService.getReviewsByProductId(productId).subscribe({
      next: (r) => this.reviews.set(r),
      error: (err) => console.error('Failed to load reviews', err),
      complete: () => this.loadingReviews.set(false),
    });
  }

  // Select product image
  selectImage(img: string): void {
    this.selectedImage.set(img);
  }

  // Add product to cart (placeholder)
  addToCart(): void {
    if (this.product()) {
      console.log('Add to cart', this.product()?.id);
      // Later connect with CartService
    }
  }
  navigateToRate(){
    this.router.navigate(['rate-product'], { queryParams: { productId: this.product()?.id } });
  }
}
