package com.example.Entity.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "account_details")
public class AccountDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private int id;
    private int concernedNum;
    private int motionNum;
    private int fanNum;
}
