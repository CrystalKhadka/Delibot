package com.system.online_ordering_system.Repo;

import com.system.online_ordering_system.entity.User;
import com.system.online_ordering_system.repo.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepoTests {

    @Autowired
    private UserRepo userRepo;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveUserTest() {
        User user = User.builder()
                .firstName("Crystal")
                .lastName("Khadka")
                .email("s@gmail.com")
                .password("1234")
                .address("KTM")
                .phoneNumber("1234567890")
                .image("crystal.jpg")
                .active(true)
                .build();
        userRepo.save(user);
    }

    @Test
    @Order(2)
    public void findUserByIdTest() {
        User user = userRepo.findById(1).get();
        Assertions.assertThat(user.getEmail()).isEqualTo("s@gmail.com");
    }

    @Test
    @Order(3)
    public void findUserByEmailTest() {
        String email = "s@gmail.com";
        User user = userRepo.findByEmail(email).get();
        Assertions.assertThat(user.getEmail()).isEqualTo(email);

    }

    @Test
    @Order(4)
    public void findAllUser(){
        List<User> users = userRepo.findAll();
        Assertions.assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    @Order(5)
    public void updateUserTest() {
        User user = userRepo.findById(1).get();
        user.setFirstName("Safal");
        user.setLastName("Pandey");
        userRepo.save(user);

        User updatedUser = userRepo.findById(1).get();
        Assertions.assertThat(updatedUser.getFirstName()).isEqualTo("Safal");
    }

    @Test
    @Order(6)
    public void deleteUserTest() {
        User user = userRepo.findById(1).get();
        userRepo.delete(user);

       User deletedUser=null;
        Optional<User> optionalUser = userRepo.findById(1);
        if(optionalUser.isPresent()){
            deletedUser = optionalUser.get();
        }
        Assertions.assertThat(deletedUser).isNull();
    }

}
