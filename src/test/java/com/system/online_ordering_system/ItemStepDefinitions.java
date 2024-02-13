package com.system.online_ordering_system;

import com.system.online_ordering_system.dto.ItemDto;
import com.system.online_ordering_system.entity.Item;
import com.system.online_ordering_system.repo.ItemRepo;
import com.system.online_ordering_system.service.ItemService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")

public class ItemStepDefinitions {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepo itemRepo;

    private ItemDto newItemDto;

    private Item item;

    @Given("I have filled all input boxes with valid data")
    public void i_have_filled_all_input_boxes_with_valid_data() {
        newItemDto = new ItemDto();
        newItemDto.setItemName("test");
        newItemDto.setItemPrice(100);
        newItemDto.setItemQuantity(10);
        newItemDto.setItemDescription("test");
        log.info("ItemDto created successfully");
    }

    @When("I click on the add item button")
    public void i_click_on_the_add_item_button() throws IOException {
        itemService.addItem(newItemDto);
        log.info("Item added successfully");
    }

    @Then("I should see the item in the table")
    public void i_should_see_the_item_in_the_table() {
        item = itemRepo.findById(newItemDto.getItemId()).orElse(null);
        Assertions.assertThat(item).isNotNull();
        log.info("Item found successfully");
    }

    @Given("find all list")
    public void find_all_list() {
        List<Item> items = itemRepo.findAll();
        log.info(items);
        System.out.println(items);
        Assert.assertTrue(!items.isEmpty());
    }

    @Given("find item by id")
    public void find_item_by_id() {
        item = itemRepo.findById(3).orElse(null);
        log.info(item);
        Assert.assertTrue(item != null);

    }



}
