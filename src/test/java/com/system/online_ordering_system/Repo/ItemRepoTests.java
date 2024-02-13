package com.system.online_ordering_system.Repo;

import com.system.online_ordering_system.entity.Item;
import com.system.online_ordering_system.repo.ItemRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemRepoTests {

    @Autowired
    private ItemRepo itemRepo;

//    JUnit to save item
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveItemTest() {
        Item item=Item.builder()
                .itemName("Chicken")
                .itemPrice(1000)
                .itemDescription("Chicken")
                .itemQuantity(10)
                .itemImage("Chicken.jpg")
                .build();
        itemRepo.save(item);

        Assertions.assertThat(item.getItemId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void findItemByIdTest() {
        Item item = itemRepo.findById(1).get();
        Assertions.assertThat(item.getItemId()).isEqualTo(1);
    }

    @Test
    @Order(3)
    public void getListItemTest() {
        List<Item> items = itemRepo.findAll();
        Assertions.assertThat(items.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    public void updateItemTest() {
        Item item = itemRepo.findById(1).get();
        item.setItemName("Mutton");

        Item updatedItem = itemRepo.save(item);
        Assertions.assertThat(updatedItem.getItemName()).isEqualTo("Mutton");
        Assertions.assertThat(updatedItem.getItemId()).isEqualTo(1);

    }

    @Test
    @Order(5)
    public void deleteItemTest() {
        Item item = itemRepo.findById(1).get();
        itemRepo.delete(item);

        Item deletedItem = null;
        Optional<Item> optionalItem= itemRepo.findById(1);
        if(optionalItem.isPresent()) {
            deletedItem = optionalItem.get();
        }

        Assertions.assertThat(deletedItem).isNull();
    }
}
