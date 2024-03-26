package com.example.Entity.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "account_details")
public class AccountDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_sid", referencedColumnName = "sid")
    private Account account;

    @Column(name = "point_coin")
    private float pointCoin;

    @Column(name = "future_coin")
    private float future_coin;

    @Column(name = "concerned_num")
    private int concernedNum;

    @Column(name = "motion_num")
    private int motionNum;

    @Column(name = "fan_num")
    private int fanNum;
}
