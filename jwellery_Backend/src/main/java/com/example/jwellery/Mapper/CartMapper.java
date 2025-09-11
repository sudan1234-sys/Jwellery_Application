package com.example.jwellery.Mapper;

import com.example.jwellery.Dto.CartDTO;
import com.example.jwellery.Dto.CartItemDTO;
import com.example.jwellery.Entity.Cart;
import com.example.jwellery.Entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartDTO toDTO(Cart cart) {
        if (cart == null) return null;

        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setStatus(cart.getStatus());


        List<CartItemDTO> items = cart.getItems().stream()
                .map(this::mapItem)
                .collect(Collectors.toList());

        dto.setItems(items);
        dto.setTotal(items.stream().mapToDouble(CartItemDTO::getSubtotal).sum());

        return dto;
    }

    private CartItemDTO mapItem(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setPrice(item.getProduct().getPrice());
        dto.setQuantity(item.getQuantity());
        if (item.getProduct().getImages() != null && !item.getProduct().getImages().isEmpty()) {
            dto.setImage(item.getProduct().getImages().get(0).getImageUrl());
        } else {
            dto.setImage(null); // or set a default placeholder URL
        }
        dto.setSubtotal(item.getQuantity() * item.getProduct().getPrice());
        return dto;
    }
}
