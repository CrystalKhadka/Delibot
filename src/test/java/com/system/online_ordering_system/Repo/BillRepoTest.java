package com.system.online_ordering_system.Repo;

import com.system.online_ordering_system.entity.Bill;
import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.repo.BillRepo;
import com.system.online_ordering_system.repo.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public
class BillRepoTest {

    @Autowired
    private BillRepo billRepo;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveBillTest() {


        Bill bill = Bill.builder()
                .date(LocalDateTime.now())
                .total(1000)
                .build();

        Bill savedBill = billRepo.save(bill);

        assertNotNull(savedBill);
    }


    @Test
    @Order(3)
    void findAllBillTest() {
        assertNotNull(billRepo.findAll());
    }




}