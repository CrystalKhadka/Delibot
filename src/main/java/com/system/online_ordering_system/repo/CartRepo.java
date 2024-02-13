package com.system.online_ordering_system.repo;

import com.system.online_ordering_system.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer>{

    @Query(value = "select * from cart where user_id = ?1", nativeQuery = true)
    List<Cart> findAllByUser(int id);

    @Query(value = "select * from cart where user_id = ?1 and status = 'Paid'", nativeQuery = true)
    List<Cart> findAllByUserAndStatus(int id);

    @Query(value = "select * from cart where user_id = ?1 and status = 'Pending'", nativeQuery = true)
    List<Cart> findAllByUserAndStatusUnpaid(int id);
}
