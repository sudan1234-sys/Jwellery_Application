import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { MycartComponent } from './features/mycart/mycart.component';
export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'product/:id',
    loadComponent: () =>
      import('./features/product-detail/product-detail.component')
        .then(m => m.ProductDetailComponent),
  },
  { path: 'products', loadComponent: () => import('./features/products/products.component').then(m => m.ProductsComponent) },
   {
    path: 'mycart', component:MycartComponent
  },
  {
    path: '**',
    redirectTo: '' // fallback to home
  },
 
];

