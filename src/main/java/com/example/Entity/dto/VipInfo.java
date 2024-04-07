package com.example.Entity.dto;

import com.example.Entity.BaseData;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="vip_info")
public class VipInfo implements BaseData {
    // type 0-普通 1-月度 2-季度 3-年度
    // status 0-非会员 1-会员
    // 记得加入nickname
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    int id;

    @Column(name = "status")
    private int status;

    @Column(name = "type")
    private int type;

    @Column(name = "opening_time")
    private long openingTime;

    @Column(name = "expire_time")
    private long expireTime;
}
