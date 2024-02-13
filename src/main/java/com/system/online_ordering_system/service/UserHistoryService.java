package com.system.online_ordering_system.service;

import com.system.online_ordering_system.entity.UserHistory;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface UserHistoryService {


    void generateAllUserHistory();

    List<UserHistory> getAllUserHistoryByUser(int id);


}
