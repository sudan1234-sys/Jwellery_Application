// src/app/features/product-detail/product-detail.component.ts
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
  signal,
  computed,
} from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgOptimizedImage } from '@angular/common';
import { ProductService } from '../../core/services/product.service.ts.service';
import { ProductDTO } from '../../core/models/product.dto';

@Component({
  selector: 'app-product-detail',
  imports: [NgOptimizedImage, CommonModule, RouterLink],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly route = inject(ActivatedRoute);

  readonly product = signal<ProductDTO | null>(null);
  readonly selectedImage = signal<string | null>(null);
  readonly loading = signal(false);

  ngOnInit(): void {
    this.loadProduct();
  }

  private loadProduct(): void {
    this.loading.set(true);
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.productService.getProductById(id).subscribe({
      next: (p) => {
        this.product.set(p);
        if (p.imageUrls?.length) {
          this.selectedImage.set(p.imageUrls[0]);
        }
      },
      error: (err) => console.error(err),
      complete: () => this.loading.set(false),
    });
  }

  selectImage(img: string): void {
    this.selectedImage.set(img);
  }

  addToCart(): void {
    if (this.product()) {
      console.log('Add to cart', this.product()?.id);
      // Later connect with CartService
    }
  }
}
