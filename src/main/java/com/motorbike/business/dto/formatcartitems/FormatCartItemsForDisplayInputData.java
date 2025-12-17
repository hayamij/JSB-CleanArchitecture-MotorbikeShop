package com.motorbike.business.dto.formatcartitems;

import com.motorbike.domain.entities.ChiTietGioHang;
import java.util.List;

/**
 * UC-78: Format Cart Items For Display - Input Data
 * Holds the list of cart items (ChiTietGioHang) to be formatted
 */
public class FormatCartItemsForDisplayInputData {
    
    private final List<ChiTietGioHang> cartItems;
    
    public FormatCartItemsForDisplayInputData(List<ChiTietGioHang> cartItems) {
        this.cartItems = cartItems;
    }
    
    public List<ChiTietGioHang> getCartItems() {
        return cartItems;
    }
}
