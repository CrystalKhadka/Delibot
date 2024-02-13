package com.system.online_ordering_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Otp {


    @Id
    @SequenceGenerator(name = "otp_seq_gen", sequenceName = "otp_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "otp_seq_gen", strategy = GenerationType.SEQUENCE)
    private int id;
    private String otp;
    private String email;
    private LocalDateTime date;
}
