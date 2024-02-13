package com.system.online_ordering_system.service.impl;

import com.system.online_ordering_system.entity.Bill;
import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.entity.UserHistory;
import com.system.online_ordering_system.repo.BillRepo;
import com.system.online_ordering_system.repo.UserHistoryRepo;
import com.system.online_ordering_system.service.UserHistoryService;
import com.system.online_ordering_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserHistoryServiceImpl implements UserHistoryService {
    private final UserService userService;
    private final BillService billService;
    private final UserHistoryRepo userHistoryRepo;
    private final BillRepo billRepo;

    public void generateUserHistory(User user) {
        List<Bill> bills=billService.getAllBillsByUser(user.getId());
        List<UserHistory> userHistories=new ArrayList<>();
        for(Bill bill:bills){
            if(bill.getUserHistory()!=null){
                continue;
            }
            String month=bill.getDate().getMonth().toString();
            String year=String.valueOf(bill.getDate().getYear());
            UserHistory userHistory=userHistoryRepo.findByUserIdAndDate(user.getId(),month,year).orElse(new UserHistory());
            String period=getPeriod(month,year);
            userHistory.setMonth(month);
            userHistory.setYear(year);
            userHistory.setUser(user);
            userHistory.setPeriod(period);
            userHistory.setTotal(userHistory.getTotal()+bill.getTotal());
            userHistoryRepo.save(userHistory);
            if(!userHistories.contains(userHistory)){
                userHistories.add(userHistory);
            }
        }

        for(UserHistory userHistory:userHistories){
            for(Bill bill:bills){
                String month=bill.getDate().getMonth().toString();
                String year=String.valueOf(bill.getDate().getYear());
                if(userHistory.getMonth().equals(month) && userHistory.getYear().equals(year)){
                    bill.setUserHistory(userHistory);
                    billRepo.save(bill);
                }
            }
        }



    }


    @Override
    public void generateAllUserHistory() {
        List<User> users=userService.getAllUsers();
        for(User user:users){
            generateUserHistory(user);
        }
    }

    @Override
    public List<UserHistory> getAllUserHistoryByUser(int id) {
        return userHistoryRepo.findAllByUserId(id);
    }



    public static String getPeriod(String monthName, String yearNo) {
        Month month = Month.valueOf(monthName.toUpperCase());
        int   year= Integer.parseInt(yearNo);
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM d");
        return startOfMonth.format(formatter) + " - " + endOfMonth.format(formatter);
    }
}
