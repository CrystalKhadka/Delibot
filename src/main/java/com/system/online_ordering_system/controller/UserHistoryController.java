package com.system.online_ordering_system.controller;

import com.system.online_ordering_system.entity.Bill;
import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.entity.UserHistory;
import com.system.online_ordering_system.service.UserHistoryService;
import com.system.online_ordering_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/userHistory")
public class UserHistoryController {
    private final UserHistoryService userHistoryService;

    private final UserService userService;
    @GetMapping("/list/{id}")
    public String listUserHistory(@PathVariable int id, Model model){
        List<UserHistory> userHistories=userHistoryService.getAllUserHistoryByUser(id);

        List<Month> months = new ArrayList<>(Arrays.asList(Month.values()));
        double[] totals=new double[12];
        for(int i=0;i<12;i++){
            totals[i]=0;
        }
        for(UserHistory userHistory:userHistories){
            totals[Month.valueOf(userHistory.getMonth()).getValue()-1]=userHistory.getTotal();
        }
        for(int i=0;i<12;i++){
            System.out.println(totals[i]);
        }
        model.addAttribute("userHistories",userHistories);
        model.addAttribute("months",months);
        model.addAttribute("totals",totals);
        User activeUser = userService.getActiveUser().get();
        model.addAttribute("user",activeUser);


        return "dashboard/userHistory";
    }
}
