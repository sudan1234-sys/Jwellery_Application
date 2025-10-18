import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgOptimizedImage } from '@angular/common';
import { FormGroup, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

import { ProductService } from '../../core/services/product.service.ts.service';
import { ReviewserviceService } from '../../core/services/reviewservice.service';
import { ProductDTO } from '../../core/models/product.dto';
import { reviewDto } from '../../core/models/reviewDto';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [NgOptimizedImage, CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly reviewService = inject(ReviewserviceService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly product = signal<ProductDTO | null>(null);
  readonly reviews = signal<reviewDto[]>([]);
  readonly selectedImage = signal<string | null>(null);
  readonly loadingProduct = signal(false);
  readonly loadingReviews = signal(false);

  reviewForm: FormGroup = new FormGroup({
    rating: new FormControl(0, Validators.required),
    review: new FormControl('', [Validators.required, Validators.minLength(10)])
  });

  ngOnInit(): void {
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    if (!productId) {
      console.error('Invalid product ID');
      return;
    }

    this.loadProduct(productId);
    this.loadReviews(productId);
  }

  private loadProduct(productId: number): void {
    this.loadingProduct.set(true);
    this.productService.getProductById(productId).subscribe({
      next: (p) => {
        this.product.set(p);
        if (p.imageUrls?.length) this.selectedImage.set(p.imageUrls[0]);
      },
      error: (err) => console.error('Failed to load product', err),
      complete: () => this.loadingProduct.set(false)
    });
  }

  private loadReviews(productId: number): void {
    this.loadingReviews.set(true);
    this.reviewService.getReviewsByProductId(productId).subscribe({
      next: (r) => this.reviews.set(r),
      error: (err) => console.error('Failed to load reviews', err),
      complete: () => this.loadingReviews.set(false)
    });
  }

  selectImage(img: string): void {
    this.selectedImage.set(img);
  }

  addToCart(): void {
    if (this.product()) console.log('Add to cart', this.product()?.id);
  }

  navigateToRate(): void {
    this.router.navigate(['rate-product'], { queryParams: { productId: this.product()?.id } });
  }

  submitReview(): void {
    if (this.reviewForm.invalid || !this.product()) return;

    const payload = {
      review: this.reviewForm.value.review,
      rating: this.reviewForm.value.rating,
      user: { id: 1 },
      product: { id: this.product()?.id }
    };

    this.reviewService.addReview(payload).subscribe({
      next: (res) => {
        console.log('Review added:', res);
        this.reviewForm.reset({ rating: 0, review: '' });
        this.loadReviews(this.product()?.id!);
      },
      error: (err) => console.error('Failed to add review', err)
    });
  }

  setRating(value: number): void {
    this.reviewForm.controls['rating'].setValue(value);
  }
}
