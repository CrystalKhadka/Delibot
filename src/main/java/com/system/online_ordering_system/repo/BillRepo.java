package com.system.online_ordering_system.repo;

import com.system.online_ordering_system.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepo extends JpaRepository<Bill, Integer> {
    @Query(value = "select * from bill where user_id=?1", nativeQuery = true)
    List<Bill> findAllByUser(int id);

    @Query(value="select * from bill  where user_id=?1 and date between ?2 and ?3 order by date ",nativeQuery = true)
    List<Bill> findBillForTenDays(int id, LocalDate startDate, LocalDate endDate);

    @Query(value="select * from bill where user_id=?1 order by date desc limit 1",nativeQuery = true)
    Optional<Bill> findTopByUserIdOrderByDateDesc(int userId);
}
