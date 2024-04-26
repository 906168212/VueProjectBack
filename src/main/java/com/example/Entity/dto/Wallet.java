package com.example.Entity.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int wid;
    @Column
    private float futureCoin;
    @Column
    private float pointCoin;
}
