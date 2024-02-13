package com.system.online_ordering_system.service.impl;


import com.system.online_ordering_system.dto.ItemDto;
import com.system.online_ordering_system.entity.Category;
import com.system.online_ordering_system.entity.Item;
import com.system.online_ordering_system.repo.ItemRepo;
import com.system.online_ordering_system.service.CategoryService;
import com.system.online_ordering_system.service.ItemService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepo itemRepo;
    private final CategoryService categoryService;
    private final String uploadDirectory = System.getProperty("user.dir") + "/item_img";

    @Override
    public void addItem(ItemDto itemDto) throws IOException {
        Item item = itemRepo.findById(itemDto.getItemId()).orElse(new Item());
        item.setItemName(itemDto.getItemName());
        item.setItemPrice(itemDto.getItemPrice());
        item.setItemDescription(itemDto.getItemDescription());
        item.setItemQuantity(itemDto.getItemQuantity());
        if (itemDto.getItemImage() != null) {
            // Generate a unique image file name using user email and item name
            String imageName = itemDto.getItemName().substring(0,6) + "_" + System.currentTimeMillis();
            System.out.println(imageName);
            String originalFilename = itemDto.getItemImage().getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // Save the original image file
            Path originalFilePath = Paths.get(uploadDirectory, imageName + fileExtension);
            Files.write(originalFilePath, itemDto.getItemImage().getBytes());

            item.setItemImage(imageName + fileExtension);

            // Resize the image
            int desiredWidth = 300;  // Set your desired width
            int desiredHeight = 200; // Set your desired height

            // Provide the path to the output resized image
            String resizedImagePath = uploadDirectory + "/" + imageName + "_resized" + fileExtension;

            // Resize the image using Thumbnails library
            Thumbnails.of(originalFilePath.toFile())
                    .size(desiredWidth, desiredHeight)
                    .outputFormat(fileExtension.substring(1)) // Remove the dot (.) from the file extension
                    .toFile(resizedImagePath);

            // Update the item with the resized image path
            item.setItemResizeImage(imageName + "_resized" + fileExtension);
        }
        Category category = categoryService.getCategoryById(itemDto.getCategoryId());
        item.setCategory(category);
        itemRepo.save(item);
        itemDto.setItemId(item.getItemId());
    }

    @Override
    public Optional<Item> getItemById(int id) {
        return itemRepo.findByIdNoOpt(id);
    }

    @Override
    public List<Item> getAllItems() throws IOException {
//        To clean up images that are not in the database
        deleteUnwantedImages();
        return itemRepo.findAll();
    }

    @Override
    public List<Item> getThreeItems(int page,String sort,String order) {
        int offset = (page - 1) * 3;

        switch (sort) {
            case "name" -> {
                if (order.equals("asc")) {
                    return itemRepo.findThreeItemsByNameAsc(offset);
                } else {
                    return itemRepo.findThreeItemsByNameDesc(offset);
                }
            }
            case "price" -> {
                if (order.equals("asc")) {
                    return itemRepo.findThreeItemsByPriceAsc(offset);
                } else {
                    return itemRepo.findThreeItemsByPriceDesc(offset);
                }
            }
            case"id" -> {
                if (order.equals("asc")) {
                    return itemRepo.findThreeItemsByItemIdAsc(offset);
                } else {
                    return itemRepo.findThreeItemsByItemIdDesc(offset);
                }
            }
            default -> {
                return itemRepo.findThreeItemsByItemIdAsc(offset);
            }
        }
    }



    @Override
    public void deleteItem(int id) {
        itemRepo.deleteById(id);
    }

    @Override
    public List<Item> getSixItems(int page, String partialName) {
        int offset = (page - 1) * 6;
        return itemRepo.findSixItems(offset,partialName);
    }

    @Override
    public int countAllItems(String partialName) {
        return itemRepo.countAllItems(partialName);
    }

    @Override
    public int countAllItemsByCategoryId(int id, String partialName) {
        return itemRepo.countAllByCategoryId(id,partialName);
    }

    @Override
    public List<Item> getSixItemsByCategoryId(int id, int page, String partialName) {
        int offset = (page - 1) * 6;
        return itemRepo.findSixItemsByCategoryId(id,offset,partialName);
    }

    public List<String> getAllItemImages() {
        return itemRepo.findAllImages();
    }

    public List<String> getAllItemResizeImages() {
        return itemRepo.findAllResizeImages();
    }

    public void deleteUnwantedImages() throws IOException {
        List<Item> allItems = itemRepo.findAll();
        List<String> imageNamesFromDB = allItems.stream()
                .map(Item::getItemImage)
                .toList();

        List<String> resizedImageNamesFromDB = allItems.stream()
                .map(Item::getItemResizeImage)
                .toList();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(uploadDirectory))) {
            for (Path filePath : directoryStream) {
                String fileName = filePath.getFileName().toString();

                // Check if the file is an unwanted image or resized image
                if (!imageNamesFromDB.contains(fileName) && !resizedImageNamesFromDB.contains(fileName)) {
                    Files.delete(filePath);
                }
            }
        }
    }
}
