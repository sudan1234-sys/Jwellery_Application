import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../core/services/product.service.ts.service';
import { ProductDTO } from '../../core/models/product.dto';
import { ReviewserviceService } from '../../core/services/reviewservice.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-rate-product',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './rate-product.component.html',
  styleUrls: ['./rate-product.component.scss']
})
export class RateProductComponent implements OnInit {
  reviewForm: FormGroup = new FormGroup({});
  readonly product = signal<ProductDTO | null>(null);
  readonly loadingProduct = signal(true);
  readonly rating = signal(0); // <-- current selected star rating
  private readonly productService = inject(ProductService);
  private readonly route = inject(ActivatedRoute);
  private readonly reviewservice = inject(ReviewserviceService);
  private readonly router = inject(Router);
  ProductId!: number;

  ngOnInit() {
    // Initialize form with rating control
    this.reviewForm = new FormGroup({
      rating: new FormControl(0, Validators.required),
      review: new FormControl('', [Validators.required, Validators.minLength(10)]),
    });

    this.route.queryParams.subscribe(params => {
      this.ProductId = Number(params['productId']);
      if (this.ProductId) {
        this.productService.getProductById(this.ProductId).subscribe({
          next: (p) => this.product.set(p),
          error: (err) => console.error('Failed to load product', err),
          complete: () => this.loadingProduct.set(false),
        });
      } else {
        console.error('Invalid Product ID in query params');
        this.loadingProduct.set(false);
      }
    });
  }

  // Set rating on star click
  setRating(star: number) {
    this.rating.set(star);
    this.reviewForm.get('rating')?.setValue(star);
  }

  onSubmit() {
    if (this.reviewForm.invalid) return;

    const payload = {
      ...this.reviewForm.value, // contains "review" and "rating"
      user: { id: 1 },
      product: { id: this.ProductId },
    };

    console.log('Review Submitted:', payload);
    this.reviewservice.addReview(payload).subscribe({
      next: (r) => {
        console.log('Review Added:', r);
        this.reviewForm.reset();
        this.rating.set(0);
        if (this.ProductId) {
          this.router.navigate(['/product', this.ProductId]);
        }
      },
      error: (err) => console.error('Failed to add review', err),
    });
  }
}
