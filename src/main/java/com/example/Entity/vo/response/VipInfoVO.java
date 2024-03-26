package com.example.Entity.vo.response;

import lombok.Data;

@Data
public class VipInfoVO {
    private int status;
    private int type;
    private long openingTime;
    private long expireTime;
}
