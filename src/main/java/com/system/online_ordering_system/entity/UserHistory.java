package com.system.online_ordering_system.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserHistory {

    @SequenceGenerator(name = "user_history_seq_gen", sequenceName = "user_history_seq_id", allocationSize = 1)
    @GeneratedValue(generator = "user_history_seq_gen", strategy = GenerationType.SEQUENCE)
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="month")
    private String month;

    @Column(name="year")
    private String year;

    @Column(name="total")
    private double total;

    @Column(name="period")
    private String period;

    @OneToMany(mappedBy = "userHistory")
    private List<Bill> bills;


}
