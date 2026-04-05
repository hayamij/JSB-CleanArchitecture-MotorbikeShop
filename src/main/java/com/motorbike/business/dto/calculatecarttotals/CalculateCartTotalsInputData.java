package com.motorbike.business.dto.calculatecarttotals;

import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.GioHang;
import java.util.List;
import java.util.stream.Collectors;

public class CalculateCartTotalsInputData {
    private final List<ChiTietGioHang> cartItems;
    private final GioHang cart; // For single cart support

    // Constructor with List<ChiTietGioHang>
    public CalculateCartTotalsInputData(List<ChiTietGioHang> cartItems) {
        this.cartItems = cartItems;
        this.cart = null;
    }
    
    // Constructor with GioHang
    private CalculateCartTotalsInputData(GioHang cart) {
        this.cart = cart;
        this.cartItems = cart != null ? cart.getDanhSachSanPham() : List.of();
    }

    // Static factory method for tests accepting List<GioHang>
    public static CalculateCartTotalsInputData fromCarts(List<GioHang> carts) {
        if (carts == null || carts.isEmpty()) {
            return new CalculateCartTotalsInputData(List.of());
        }
        if (carts.size() == 1) {
            return new CalculateCartTotalsInputData(carts.get(0));
        }
        List<ChiTietGioHang> items = carts.stream()
            .flatMap(c -> c.getDanhSachSanPham().stream())
            .collect(Collectors.toList());
        return new CalculateCartTotalsInputData(items);
    }

    public List<ChiTietGioHang> getCartItems() {
        return cartItems;
    }
    
    public GioHang getCart() {
        return cart;
    }
}
