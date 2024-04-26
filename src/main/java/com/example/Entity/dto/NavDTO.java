package com.example.Entity.dto;

import lombok.Data;

@Data
public class NavDTO {
    private int uid;
    private String username;
    private String nickname;
    private long createTime;
    private WalletDTO wallet;
    private AccountDetailsDTO details;
    private LevelDTO level;
    private VipDTO vip;
}
