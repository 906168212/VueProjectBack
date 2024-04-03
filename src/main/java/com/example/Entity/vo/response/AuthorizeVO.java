package com.example.Entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
// 用户详细信息，vo=与前端交互的层
public class AuthorizeVO {
    int sid;
    String username;
    String role;
    String token;
    long expireTime;
}
