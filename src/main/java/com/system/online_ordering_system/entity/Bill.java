package com.system.online_ordering_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Bill {

    @Id
    @SequenceGenerator(name = "bill_seq_gen", sequenceName = "bill_seq_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_seq_gen")
    private int id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="user_history_id")
    private UserHistory userHistory;

    @Column(name="total")
    private double total;

    @Column(name="date")
    private LocalDateTime date;

}
