package com.example.Entity.vo;

import com.example.Entity.dto.ArticleStatDTO;
import com.example.Entity.dto.SimpleUserDTO;
import lombok.Data;

@Data
public class ArticleVO {
    private int aid;
    private SimpleUserDTO user;
    private String category;
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
