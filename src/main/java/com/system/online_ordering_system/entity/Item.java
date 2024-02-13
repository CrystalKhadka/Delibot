package com.system.online_ordering_system.entity;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "items")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @SequenceGenerator(name = "item_seq_gen", sequenceName = "item_seq_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
    private int itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_price")
    private double itemPrice;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_quantity")
    private int itemQuantity;

    @Column(name = "item_image")
    private String itemImage;

    @Column(name="item_resize_image")
    private String itemResizeImage;

    @Transient
    private String itemImageBase64;

    @Transient
    private String itemResizeImageBase64;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
