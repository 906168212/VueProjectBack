package com.example.Entity.vo.response;

import com.example.Entity.dto.AccountDetailsDTO;
import com.example.Entity.dto.LevelDTO;
import com.example.Entity.dto.VipDTO;
import com.example.Entity.dto.WalletDTO;
import lombok.Data;

@Data
public class NavVO {
    private int uid;
    private String username;
    private String nickname;
    private long createTime;
    private WalletDTO wallet;
    private AccountDetailsDTO details;
    private LevelVO level;
    private VipDTO vip;
}
