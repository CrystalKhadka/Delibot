package com.system.online_ordering_system.controller;

import com.system.online_ordering_system.entity.Bill;
import com.system.online_ordering_system.entity.Item;
import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.service.BillService;
import com.system.online_ordering_system.service.CategoryService;
import com.system.online_ordering_system.service.ItemService;
import com.system.online_ordering_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final BillService  billService;
    private final UserService userService;

    @GetMapping("/")
    public String dashboard(Model model){
        List<Bill>  bills=billService.getBillForTenDays();
        int[] barData=generateBarData(bills);
        for (int i = 0; i < barData.length; i++) {
            System.out.println(barData[i]);
        }

        User activeUser = userService.getActiveUser().get();
        model.addAttribute("user",activeUser);

        model.addAttribute("barData", barData);


        // Generate date labels for the previous ten days
        List<String> dateLabels = generateDateLabels();
        model.addAttribute("dateLabels", dateLabels);
        return "dashboard/userDashboard";
    }


    int[] generateBarData(List<Bill> bills){
        int[] data=new int[10];
        for(Bill bill   :bills){
            LocalDateTime date=bill.getDate();
            int index=(date.getDayOfMonth()-1)%10;
            data[index]+=bill.getTotal();
        }
        return data;
    }

    List<String> generateDateLabels() {
        List<String> dateLabels = new ArrayList<>();
        LocalDate date = LocalDate.now().minusDays(9);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");
        for (int i = 0; i < 10; i++) {
            dateLabels.add(date.format(formatter));
            date = date.plusDays(1);
        }
        return dateLabels;
    }



    @GetMapping("/menu")
    public String menuByCategory(Model model, @RequestParam(defaultValue = "0") int id,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "") String partialName) throws IOException {
        int totalItems;
        if(id==0) {
            totalItems = itemService.countAllItems(partialName);
        }
        else{
            totalItems = itemService.countAllItemsByCategoryId(id,partialName);
        }

        int pageSize=6;

        // Calculate the total number of pages based on the page size and total items
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if(totalPages==0){
            totalPages=1;
        }
        // Ensure the requested page is within the valid range
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        // Calculate the starting index and ending index for the subset of items to display
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);





        List<Item> items =new ArrayList<>();
        if(id==0){
            items=itemService.getSixItems(page,partialName);
        }else{
            items=itemService.getSixItemsByCategoryId(id,page,partialName);
        }


        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("categories", categoryService.getAllCategories());
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

        User activeUser = userService.getActiveUser().get();
        model.addAttribute("user",activeUser);
        return "dashboard/catalog";
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
