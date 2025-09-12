import { Component, inject, OnInit, OnDestroy, signal, computed } from '@angular/core';
import { ProductService } from '../../core/services/product.service.ts.service';
import { PaginatedResponse } from '../../core/models/paginated-response.model';
import { ProductDTO } from '../../core/models/product.dto';
import { CategoryService } from '../../core/services/category.service.ts.service';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { FormsModule, FormControl,ReactiveFormsModule } from '@angular/forms';
import { Category } from '../../core/models/category';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  imports: [CommonModule, NgOptimizedImage, RouterLink, FormsModule,ReactiveFormsModule],
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit, OnDestroy {
  // Services / Router
  private readonly productService = inject(ProductService);
  private readonly categoryService = inject(CategoryService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  // Signals & derived values
  paginatedResponse = signal<PaginatedResponse<ProductDTO> | null>(null);
  readonly product = computed(() => this.paginatedResponse()?.content || []);
  readonly loading = signal(false);
  currentPage = signal(0);
  categories = signal<Category[]>([]);

  // Filters
  categoryFilter = signal<number | null>(null);
  minPriceFilter = signal<number | null>(null);
  maxPriceFilter = signal<number | null>(null);
  searchValue = signal('');

  // Reactive control for search input
  searchControl = new FormControl('');

  // unsubscribe helper
  private readonly destroy$ = new Subject<void>();

  ngOnInit() {
    // 1) Sync with query params
    this.route.queryParams.pipe(takeUntil(this.destroy$)).subscribe(params => {
      const cat = params['category'] ? +params['category'] : null;
      this.categoryFilter.set(cat);

      const minP = params['minPrice'] ? +params['minPrice'] : null;
      this.minPriceFilter.set(minP);

      const maxP = params['maxPrice'] ? +params['maxPrice'] : null;
      this.maxPriceFilter.set(maxP);

      const page = params['page'] ? +params['page'] : 0;
      this.currentPage.set(page);

      const search = params['search'] ?? '';
      this.searchValue.set(search);
      this.searchControl.setValue(search, { emitEvent: false });

      this.loadProductsFromServer(this.currentPage());
    });

    // 2) Debounced search
    this.searchControl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(value => {
        const trimmed = (value ?? '').trim();
        this.searchValue.set(trimmed);
        this.updateQueryParams({ search: trimmed || null, page: 0 }, { replaceUrl: true });
      });

    // 3) Load categories
    this.loadCategories();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // --- Data loading ---
  private loadProductsFromServer(page: number = 0) {
    this.loading.set(true);

    const search = (this.searchValue() && this.searchValue().trim().length)
      ? this.searchValue().trim()
      : null;

    const filters = {
      category: this.categoryFilter(),
      minPrice: this.minPriceFilter(),
      maxPrice: this.maxPriceFilter(),
      search,
      page,
      size: 20
    };

    this.productService.getProductsByFilter(filters).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response) => {
        this.paginatedResponse.set(response);
        this.loading.set(false);
        this.currentPage.set(response.number);

        if ((response.totalPages ?? 0) > 0 && this.currentPage() >= response.totalPages) {
          const last = Math.max(0, (response.totalPages ?? 1) - 1);
          this.updateQueryParams({ page: last });
        }
      },
      error: (err) => {
        console.error('Error loading products:', err);
        this.loading.set(false);
      }
    });
  }

  // --- Category loader ---
  loadCategories() {
    this.categoryService.getAllCategories().pipe(takeUntil(this.destroy$)).subscribe({
      next: cats => this.categories.set(cats),
      error: err => console.error('Error loading categories:', err)
    });
  }

  // --- Pagination ---
  nextPage() {
    if (this.currentPage() + 1 < (this.paginatedResponse()?.totalPages ?? 0)) {
      this.updateQueryParams({ page: this.currentPage() + 1 });
    }
  }

  prevPage() {
    if (this.currentPage() > 0) {
      this.updateQueryParams({ page: this.currentPage() - 1 });
    }
  }

  // --- Filter actions ---
  applyFilters() {
    this.updateQueryParams({
      page: 0,
      category: this.categoryFilter(),
      minPrice: this.minPriceFilter(),
      maxPrice: this.maxPriceFilter()
    });
  }

  clearFilters() {
    this.categoryFilter.set(null);
    this.minPriceFilter.set(null);
    this.maxPriceFilter.set(null);
    this.searchValue.set('');
    this.searchControl.setValue('', { emitEvent: false });
    this.currentPage.set(0);

    this.router.navigate([], { relativeTo: this.route, queryParams: {} });
  }

  // --- Helpers for template binding ---
  get categoryFilterValue() {
    return this.categoryFilter();
  }
  set categoryFilterValue(val: number | null) {
    this.categoryFilter.set(val);
  }

  get minPriceFilterValue() {
    return this.minPriceFilter();
  }
  set minPriceFilterValue(val: number | null) {
    this.minPriceFilter.set(val);
  }

  get maxPriceFilterValue() {
    return this.maxPriceFilter();
  }
  set maxPriceFilterValue(val: number | null) {
    this.maxPriceFilter.set(val);
  }

  // --- Query param updater ---
  private updateQueryParams(
    overrides: Partial<Record<'category'|'minPrice'|'maxPrice'|'search'|'page', any>> = {},
    options: { replaceUrl?: boolean } = {}
  ) {
    const keys: Array<'category'|'minPrice'|'maxPrice'|'search'|'page'> = ['category','minPrice','maxPrice','search','page'];
    const finalParams: any = {};

    for (const k of keys) {
      if (Object.prototype.hasOwnProperty.call(overrides, k)) {
        finalParams[k] = overrides[k as keyof typeof overrides] ?? null;
      } else {
        switch (k) {
          case 'category': finalParams[k] = this.categoryFilter(); break;
          case 'minPrice': finalParams[k] = this.minPriceFilter(); break;
          case 'maxPrice': finalParams[k] = this.maxPriceFilter(); break;
          case 'search':
            finalParams[k] = (this.searchValue() && this.searchValue().trim().length)
              ? this.searchValue().trim()
              : null;
            break;
          case 'page': finalParams[k] = this.currentPage() ?? 0; break;
        }
      }
    }

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: finalParams,
      replaceUrl: !!options.replaceUrl
    });
  }
}
