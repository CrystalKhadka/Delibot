package com.system.online_ordering_system.service;

import com.system.online_ordering_system.entity.Bill;

import java.util.List;
import java.util.Optional;

public interface BillService {

    void generateBill();

    List<Bill> getAllBillsByUser(int id);

    List<Bill> getBillForTenDays();

    void sentQrCodeToUser(int id);

    Optional<Bill> getLatestBillByUser(int id);
}
