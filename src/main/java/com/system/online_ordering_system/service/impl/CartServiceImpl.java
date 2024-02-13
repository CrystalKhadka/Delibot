package com.system.online_ordering_system.service.impl;

import com.lowagie.text.pdf.BidiLine;
import com.system.online_ordering_system.dto.CartDto;
import com.system.online_ordering_system.entity.Cart;
import com.system.online_ordering_system.entity.Item;
import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.repo.CartRepo;
import com.system.online_ordering_system.repo.ItemRepo;
import com.system.online_ordering_system.service.CartService;
import com.system.online_ordering_system.service.ItemService;
import com.system.online_ordering_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final UserService userService;
    private final ItemService  itemService;
    private final ItemRepo itemRepo;

    @Override
    public void addToCart(CartDto cartDto) {
        Cart cart = cartRepo.findById(cartDto.getId()).orElse(new Cart());
        cart.setCartQty(cartDto.getCartQty());
        cart.setTotalPrice(cartDto.getTotalPrice());

        Optional<Item> optionalItem = itemService.getItemById(cartDto.getItemId());

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            int availableQty = item.getItemQuantity();
            int cartQty = cartDto.getCartQty();
            AssertionError error= new AssertionError("Insufficient Quantity");
            if (cartQty > availableQty) {
                throw error;
            }
            int newQty = availableQty - cartQty;
            item.setItemQuantity(newQty);
            itemRepo.save(item);
            cart.setItem(item);
        }

        Optional<User> optionalUser = userService.getActiveUser();
        if (optionalUser.isPresent()) {
           User user = optionalUser.get();
            cart.setUser(user);
        }
        cart.setStatus("Pending");
        cartRepo.save(cart);





    }

    @Override
    public List<Cart> getCartList(User user) {
        return cartRepo.findAllByUser(user.getId());
    }

    @Override
    public List<Cart> getCartListByStatus(User user) {
        return cartRepo.findAllByUserAndStatus(user.getId());
    }

    @Override
    public List<Cart> getCartListByStatusUnpaid(User user) {
        return cartRepo.findAllByUserAndStatusUnpaid(user.getId());
    }

    @Override
    public void deleteCart(int id) {
        Cart cart = cartRepo.findById(id).orElse(new Cart());
        Item item = cart.getItem();
        int availableQty = item.getItemQuantity();
        int cartQty = cart.getCartQty();
        int newQty = availableQty + cartQty;
        item.setItemQuantity(newQty);
        itemRepo.save(item);

        cartRepo.deleteById(id);
    }

    @Override
    public void editCart(CartDto cartDto) {
        Cart cart = cartRepo.findById(cartDto.getId()).orElse(new Cart());
        int itemQty=cart.getItem().getItemQuantity();
        int cartQty=cart.getCartQty();
        Item item=cart.getItem();
        int newQty=itemQty+cartQty;
        int qty=cartDto.getCartQty();
        AssertionError error = new AssertionError("Quantity is greater than available quantity");
        if (qty > newQty) {
            throw error;
        }
        newQty=newQty-qty;
        item.setItemQuantity(newQty);
        cart.setCartQty(qty);
        double price = cart.getItem().getItemPrice();
        cart.setTotalPrice(price * qty);
        itemRepo.save(item);
        cartRepo.save(cart);

    }
}
