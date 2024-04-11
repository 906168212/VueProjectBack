package com.example.Entity.dto;

import lombok.Data;

@Data
public class ArticleDTO {
    private int aid;
    private SimpleUserDTO user;
    private int category;
    private String title;
    private String desc;
    private String pic;
    private String picAvif;
    private String picWebp;
    private int status;
    private long pubDate;
    private long createTime;
    private long updateTime;
    private int recommend;
    private ArticleStatDTO stat;
}
