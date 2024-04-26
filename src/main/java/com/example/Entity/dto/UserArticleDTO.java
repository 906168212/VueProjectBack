package com.example.Entity.dto;

import lombok.Data;

@Data
public class UserArticleDTO {
    private int aid;
    private String category;
    private int type;
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
