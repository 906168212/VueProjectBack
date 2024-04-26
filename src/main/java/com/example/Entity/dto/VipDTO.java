package com.example.Entity.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class VipDTO {
    private int status;

    private int type;

    private long openingTime;

    private long expireTime;
}
