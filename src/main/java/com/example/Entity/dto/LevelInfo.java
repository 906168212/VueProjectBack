package com.example.Entity.dto;

import com.example.Entity.BaseData;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="level_info")
public class LevelInfo implements BaseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_sid", referencedColumnName = "sid")
    private Account account;

    @Column(name = "current_exp")
    private int currentExp;

    @Column(name = "current_level")
    private int currentLevel;

    @Column(name = "current_level_name")
    private String currentLevelName;
    @Column(name = "next_exp")
    private Integer nextExp;
}
