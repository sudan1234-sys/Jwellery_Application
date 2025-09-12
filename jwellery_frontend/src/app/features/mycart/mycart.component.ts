// src/app/features/mycart/mycart.component.ts
import { Component, OnInit, inject } from '@angular/core';
import { CartserviceService, CartDTO, CartItemDTO } from '../../core/services/cartservice.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-mycart',
  templateUrl: './mycart.component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./mycart.component.scss']
})
export class MycartComponent implements OnInit {
  private cartService = inject(CartserviceService);

  cart: CartDTO | null = null;
  loading = false;

  // track productIds currently updating (prevents double API calls)
  updating = new Set<number>();

  // TODO: replace with actual logged-in user id
  userId = 1;

  // Max quantity selectable
  maxQty = 10;

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.loading = true;
    this.cartService.getCart(this.userId).subscribe({
      next: (res) => { 
        this.cart = res;

        // compute subtotal for each item locally
        if (this.cart) {
          this.cart.items.forEach(it => it.subtotal = it.price * it.quantity);
          this.cart.total = this.cart.items.reduce((s, it) => s + it.subtotal, 0);
        }

        this.loading = false;
      },
      error: (err) => { console.error('Load cart error', err); this.loading = false; }
    });
  }

  /**
   * Called when quantity is changed via dropdown
   */
  updateQuantity(item: CartItemDTO, newQty: number): void {
    const pid = item.productId;
    if (this.updating.has(pid)) return;

    // optimistic update locally
    const oldQty = item.quantity;
    item.quantity = newQty;
    item.subtotal = newQty * item.price;
    if (this.cart) this.cart.total = this.cart.items.reduce((s, it) => s + it.subtotal, 0);

    this.updating.add(pid);
    this.cartService.updateQuantity(this.userId, pid, newQty).subscribe({
      next: (res) => { 
        this.cart = res;
        this.updating.delete(pid);
      },
      error: (err) => {
        console.error('Quantity update error', err);
        // rollback on failure
        item.quantity = oldQty;
        item.subtotal = oldQty * item.price;
        if (this.cart) this.cart.total = this.cart.items.reduce((s, it) => s + it.subtotal, 0);
        this.updating.delete(pid);
      }
    });
  }

  removeItem(productId: number): void {
    if (this.updating.has(productId)) return;

    const backup = this.cart ? JSON.parse(JSON.stringify(this.cart)) as CartDTO : null;
    if (this.cart) {
      this.cart.items = this.cart.items.filter(i => i.productId !== productId);
      this.cart.total = this.cart.items.reduce((s, it) => s + it.subtotal, 0);
    }

    this.updating.add(productId);
    this.cartService.removeFromCart(this.userId, productId).subscribe({
      next: (res) => { this.cart = res; this.updating.delete(productId); },
      error: (err) => {
        console.error('Remove error', err);
        if (backup) this.cart = backup;
        this.updating.delete(productId);
      }
    });
  }

  // helper: returns array [1,2,...,maxQty] for dropdown
  quantityOptions(): number[] {
    return Array.from({ length: this.maxQty }, (_, i) => i + 1);
  }
}
