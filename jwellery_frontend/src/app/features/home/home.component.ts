import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
  computed,
  OnInit,
} from '@angular/core';
import { CategoryProducts, ProductService } from '../../core/services/product.service.ts.service';
import { CategoryService } from '../../core/services/category.service.ts.service';
import { ProductDTO } from '../../core/models/product.dto';
import { Category } from '../../core/models/category';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { CartserviceService } from '../../core/services/cartservice.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterLink, NgOptimizedImage],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HomeComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly categoryService = inject(CategoryService);
  private readonly cartService = inject(CartserviceService);

  // data signals
  readonly featuredProducts = signal<ProductDTO[]>([]);
  readonly categories = signal<Category[]>([]);
  readonly productsByCategory = signal<CategoryProducts[]>([]);

  // per-section loading signals (improves UX: show skeletons per section)
  readonly loadingFeatured = signal(true);
  readonly loadingCategories = signal(true);
  readonly loadingByCategory = signal(true);

  // derived
  readonly hasProducts = computed(() => this.featuredProducts().length > 0);

  ngOnInit(): void {
    this.loadHomeData();
  }

  private loadHomeData(): void {
    // set all loaders true at start
    this.loadingFeatured.set(true);
    this.loadingCategories.set(true);
    this.loadingByCategory.set(true);

    // Featured products
    this.productService.getFeaturedProducts().subscribe({
      next: (products) => this.featuredProducts.set(products),
      error: (err) => {
        console.error('Error loading featured products', err);
        this.featuredProducts.set([]);
      },
      complete: () => this.loadingFeatured.set(false),
    });

    // Categories
    this.categoryService.getAllCategories().subscribe({
      next: (cats) => this.categories.set(cats),
      error: (err) => {
        console.error('Error loading categories', err);
        this.categories.set([]);
      },
      complete: () => this.loadingCategories.set(false),
    });

    // Products by category (blocks)
    this.productService.getProductsByCategory().subscribe({
      next: (blocks) => this.productsByCategory.set(blocks),
      error: (err) => {
        console.error('Error loading products by category', err);
        this.productsByCategory.set([]);
      },
      complete: () => this.loadingByCategory.set(false),
    });
  }

  addToCart(product: ProductDTO): void {
    // userId hardcoded for now (replace with actual logged-in user id later)
    const userId = 1;
    this.cartService.addToCart(userId, product.id, 1).subscribe({
      next: (response) => {
        console.log('Product added to cart successfully:', response);
        // Could show toast/snackbar here
      },
      error: (err) => {
        console.error('Error adding product to cart:', err);
        // Show error UI if desired
      }
    });
  }
}
