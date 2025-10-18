import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
  computed,
  OnInit,
  ViewChild,
  ElementRef,
  AfterViewInit,
  OnDestroy,
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
export class HomeComponent implements OnInit, AfterViewInit, OnDestroy {
  private readonly productService = inject(ProductService);
  private readonly categoryService = inject(CategoryService);
  private readonly cartService = inject(CartserviceService);

  // Signals
  myMap = new Map<string, number>();
  readonly featuredProducts = signal<ProductDTO[]>([]);
  readonly categories = signal<Category[]>([]);
  readonly productsByCategory = signal<CategoryProducts[]>([]);
  readonly loadingFeatured = signal(true);
  readonly loadingCategories = signal(true);
  readonly loadingByCategory = signal(true);

  readonly hasProducts = computed(() => this.featuredProducts().length > 0);

  // --- Carousel ---
  ads = ['Ad 1', 'Ad 2', 'Ad 3', 'Ad 4']; // Dummy ads
  @ViewChild('carouselContainer', { static: true }) carouselContainer!: ElementRef<HTMLDivElement>;
  private currentIndex = 0;
  private intervalId: any;

  ngOnInit(): void {
    this.loadHomeData();
    this.myMap.set('Rings', 1);
    this.myMap.set('Necklaces', 2);
    this.myMap.set('Earrings', 3);
  }

  ngAfterViewInit(): void {
    // Ensure container exists before starting
    if (this.carouselContainer) {
      this.startAutoScroll();
    }
  }

  ngOnDestroy(): void {
    this.stopAutoScroll();
  }

  private loadHomeData(): void {
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
      next: (cats) => {
        this.categories.set(cats);
        for (const cat of cats) {
          this.myMap.set(cat.name, cat.id);
        }
      },
      error: (err) => {
        console.error('Error loading categories', err);
        this.categories.set([]);
      },
      complete: () => this.loadingCategories.set(false),
    });

    // Products by category
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
    const userId = 1; // Replace with actual user
    this.cartService.addToCart(userId, product.id, 1).subscribe({
      next: (res) => console.log('Added to cart:', res),
      error: (err) => console.error('Error adding to cart:', err),
    });
  }

  // --- Carousel Methods ---
  startAutoScroll() {
    this.stopAutoScroll(); // clear any existing interval
    this.intervalId = setInterval(() => this.nextSlide(), 3000);
  }

  stopAutoScroll() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
      this.intervalId = null;
    }
  }

  nextSlide() {
    if (!this.carouselContainer) return;
    this.currentIndex = (this.currentIndex + 1) % this.ads.length;
    this.carouselContainer.nativeElement.style.transform = `translateX(-${this.currentIndex * 100}%)`;
  }

  onMouseEnter() {
    this.stopAutoScroll();
  }

  onMouseLeave() {
    this.startAutoScroll();
  }
}
