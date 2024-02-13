
package com.system.online_ordering_system.repo;

import com.system.online_ordering_system.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHistoryRepo extends JpaRepository<UserHistory,Integer> {
    @Query(value = "select * from user_history where user_id=?1 and month=?2 and year=?3",nativeQuery = true)
    Optional<UserHistory> findByUserIdAndDate(int userId, String month, String year);

    @Query(value = "select * from user_history where user_id=?1",nativeQuery = true)
    List<UserHistory> findAllByUserId(int id);
}
