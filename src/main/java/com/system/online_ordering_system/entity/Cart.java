package com.system.online_ordering_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @SequenceGenerator(name = "cart_seq_gen", sequenceName = "cart_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_seq_gen")
    @Id
    private int id;

    @Column(name = "cart_qty")
    private int cartQty;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "status", columnDefinition = "varchar(255) default 'pending'")
    private String status;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

}
