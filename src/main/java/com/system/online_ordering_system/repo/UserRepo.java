package com.system.online_ordering_system.repo;

import com.system.online_ordering_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "select * from users where email=?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "select * from users where email=?1 and active="+"true", nativeQuery = true)
    Optional<User> findByActiveEmail(String email);

    @Query(value="select * from users order by id", nativeQuery = true)
    List<User> findAllUsers();
}
