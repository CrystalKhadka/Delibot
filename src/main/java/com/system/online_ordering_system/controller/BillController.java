package com.system.online_ordering_system.controller;

import com.system.online_ordering_system.entity.Bill;
import com.system.online_ordering_system.service.BillService;
import com.system.online_ordering_system.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bill")
public class BillController {
    private final BillService billService;
    private final UserService userService;

    @PostMapping("/generate")
    public String generateBill() {
        billService.generateBill();
        int userId = userService.getActiveUser().get().getId();
        Bill bill = billService.getLatestBillByUser(userId).get();
        billService.sentQrCodeToUser(bill.getId());

        return "redirect:/dashboard/";
    }



}
