package com.system.online_ordering_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Qr {
    @Id
    @SequenceGenerator(name = "qr_seq_gen", sequenceName = "qr_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "qr_seq_gen", strategy = GenerationType.SEQUENCE)
    private int id;

    private String qr_secret;

    private int bill_id;

    private String destination;
}
