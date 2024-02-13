package com.system.online_ordering_system.controller;

import com.system.online_ordering_system.dto.ItemDto;
import com.system.online_ordering_system.entity.Item;
import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.service.CategoryService;
import com.system.online_ordering_system.service.ItemService;
import com.system.online_ordering_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final CategoryService categoryService;

    private final ItemService itemService;

    private final UserService userService;
    @GetMapping("/add")
    public String addItem(Model model){
        User activeUser = userService.getActiveUser().get();
        model.addAttribute("user",activeUser);
        model.addAttribute("categories",categoryService.getAllCategories());
        return "Item/addItem";
    }

    @PostMapping("/add")
    public String addItemPost(@Valid ItemDto itemDto) throws Exception {

        itemService.addItem(itemDto);
        return "redirect:/item/list";
    }

    @GetMapping("/list")
    public String listItems(
            Model model,
            @RequestParam(defaultValue = "1") int page
            ,@RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order) throws IOException {

        User activeUser = userService.getActiveUser().get();
        model.addAttribute("user",activeUser);
        List<Item> allItems = itemService.getAllItems();
        int totalItems = allItems.size();
        int pageSize=3;

        // Calculate the total number of pages based on the page size and total items
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Ensure the requested page is within the valid range
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        // Calculate the starting index and ending index for the subset of items to display
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);



        List<Item> items = itemService.getThreeItems(page,sort,order);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("items", items.stream().map(item -> Item.builder()
                    .itemId(item.getItemId())
                    .itemName(item.getItemName())
                    .itemPrice(item.getItemPrice())
                    .itemDescription(item.getItemDescription())
                    .itemQuantity(item.getItemQuantity())
                    .itemResizeImageBase64(getImageBase64(item.getItemResizeImage()))
                    .category(item.getCategory())
                    .build()
        ));

        return "Item/listItem";
    }

    @GetMapping("/edit/{id}")
    public String editItem(@PathVariable("id") int id, Model model) {
        User activeUser = userService.getActiveUser().get();
        model.addAttribute("user",activeUser);
        Item item = itemService.getItemById(id).get();
        model.addAttribute("item", item);
        model.addAttribute("itemResizeImageBase64", getImageBase64(item.getItemResizeImage()));
        return "Item/editItem";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        itemService.deleteItem(id);
        return "redirect:/item/list";
    }

    public String getImageBase64(String fileName) {
        String filePath = System.getProperty("user.dir") + "/item_img/";
        File file = new File(filePath + fileName);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return base64;
    }

}
