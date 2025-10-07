import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../core/services/product.service.ts.service';
import { ProductDTO } from '../../core/models/product.dto';
import { ReviewserviceService } from '../../core/services/reviewservice.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-rate-product',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './rate-product.component.html',
  styleUrl: './rate-product.component.scss'
})
export class RateProductComponent implements OnInit {
  reviewForm: FormGroup = new FormGroup({});
  readonly product = signal<ProductDTO | null>(null);
  readonly loadingProduct = signal(true);
  private readonly productService = inject(ProductService);
  private readonly route = inject(ActivatedRoute);
  private readonly reviewservice = inject(ReviewserviceService);
  private readonly router = inject(Router);
  ProductId!: number;

  ngOnInit() {
    this.reviewForm = new FormGroup({
      // rating: new FormControl(0),
      review: new FormControl('', [Validators.required, Validators.minLength(10)]),
    });
    this.route.queryParams.subscribe(params => {
      this.ProductId = Number(params['productId']);
      if (this.ProductId) {
        this.productService.getProductById(this.ProductId).subscribe({
          next: (p) => {this.product.set(p)
            console.log(p);
          },
          error: (err) => console.error('Failed to load product', err),
          complete: () => this.loadingProduct.set(false),
        });
      } else {
        console.error('Invalid Product ID in query params');
        this.loadingProduct.set(false);
      }
    });
  }
  onSubmit() {
  if (this.reviewForm.invalid) return;

  const payload = {
    ...this.reviewForm.value, // contains only "review" and maybe "rating"
    user: { id: 1 },
    product: { id: this.ProductId },
  };
  console.log('Review Submitted:', payload);
  this.reviewservice.addReview(payload).subscribe({
  next: (r) => {
    console.log('Review Added:', r);
    this.reviewForm.reset();
    // ✅ Absolute path, ensure ProductId exists
    if (this.ProductId) {
      this.router.navigate(['/product', this.ProductId]);
    }
  },
  error: (err) => console.error('Failed to add review', err),
});

}
}
