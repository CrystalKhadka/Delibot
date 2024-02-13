package com.system.online_ordering_system.repo;

import com.system.online_ordering_system.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<Item, Integer> {

    @Query(value="select * from items where item_id = ?1", nativeQuery = true)
    Optional<Item> findByIdNoOpt(int id);


        // Other methods...


    @Query(value="select item_image from items", nativeQuery = true)
   List<String> findAllImages();

    @Query(value="select item_resize_image from items", nativeQuery = true)
    List<String> findAllResizeImages();

    @Query(value = "select * from items order by item_name offset ?1 limit 3", nativeQuery = true)
    List<Item> findThreeItemsByNameAsc(int offset);

    @Query(value = "select * from items order by item_name desc offset ?1 limit 3", nativeQuery = true)
    List<Item> findThreeItemsByNameDesc(int offset);

    @Query(value = "select * from items order by item_price offset ?1 limit 3", nativeQuery = true)
    List<Item> findThreeItemsByPriceAsc(int offset);

    @Query(value = "select * from items order by item_price desc offset ?1 limit 3", nativeQuery = true)
    List<Item> findThreeItemsByPriceDesc(int offset);

    @Query(value = "select * from items order by item_id  offset ?1 limit 3", nativeQuery = true)
    List<Item> findThreeItemsByItemIdAsc(int offset);

    @Query(value = "select * from items order by item_id desc offset ?1 limit 3", nativeQuery = true)
    List<Item> findThreeItemsByItemIdDesc(int offset);

    @Query(value = "select * from items where category_id = ?1 and LOWER(item_name) LIKE CONCAT('%', LOWER(?3), '%') order by item_id offset ?2 limit 6", nativeQuery = true)
    List<Item> findSixItemsByCategoryId(int id, int offset, String partialName);

    @Query(value = "select * from items where LOWER(item_name) LIKE CONCAT('%', LOWER(?2), '%') order by item_id  offset ?1 limit 6", nativeQuery = true)
    List<Item> findSixItems(int offset, String partialName);

    @Query(value = "select count(*) from items where LOWER(item_name) LIKE CONCAT('%', LOWER(?1), '%')", nativeQuery = true)
    int countAllItems(String partialName);

    @Query(value = "select count(*) from items where category_id = ?1 and LOWER(item_name) LIKE CONCAT('%', LOWER(?2), '%')", nativeQuery = true)
    int countAllByCategoryId(int id, String partialName);
}
