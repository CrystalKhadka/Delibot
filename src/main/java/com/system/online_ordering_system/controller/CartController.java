package com.system.online_ordering_system.controller;

import com.system.online_ordering_system.dto.CartDto;
import com.system.online_ordering_system.entity.Cart;
import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.service.CartService;
import com.system.online_ordering_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    @PostMapping("/add")
    public String addToCart(@Valid CartDto cartDto) {
        try{
        cartService.addToCart(cartDto);
        }
        catch (AssertionError e){
            return "redirect:/dashboard/menu?invalidQuantity";
        }
        return "redirect:/dashboard/menu";
    }

    @GetMapping("/list")
    public String getCartList(Model model) {
        User user = userService.getActiveUser().get();
        model.addAttribute("user",user);
        List<Cart> carts=cartService.getCartListByStatusUnpaid(user);
        model.addAttribute("carts", carts);
        model.addAttribute("total", carts.stream().mapToDouble(Cart::getTotalPrice).sum());
        model.addAttribute("user", user);
        return "Cart/viewCart";
    }

    @PostMapping("/delete/{id}")
    public String deleteCart(@PathVariable int id) {
        cartService.deleteCart(id);
        return "redirect:/cart/list";
    }

    @PostMapping("/edit")
    public String editCart(@Valid CartDto cartDto) {
        try{
        cartService.editCart(cartDto);
        }
        catch (AssertionError e){
            return "redirect:/cart/list?invalidQuantity";
        }
        return "redirect:/cart/list";
    }
}
