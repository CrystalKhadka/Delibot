package com.system.online_ordering_system.service;

import com.system.online_ordering_system.dto.CartDto;
import com.system.online_ordering_system.entity.Cart;
import com.system.online_ordering_system.entity.User;

import java.util.List;

public interface CartService {

    void addToCart(CartDto cartDto);

    List<Cart> getCartList(User user);

    List<Cart> getCartListByStatus(User user);

    List<Cart> getCartListByStatusUnpaid(User user);

    void deleteCart(int id);

    void editCart(CartDto cartDto);
}
