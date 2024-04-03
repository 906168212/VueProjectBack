package com.example.Entity.vo.response;
import lombok.Data;
@Data
public class ArticleInfoVO {
    private int mid;
    private String nickName;
    private String category;
    private String title;
    private String desc;
    private String pic;
    private String picAvif;
    private String picWebp;
    private int status;
    private long pubDate;
    private int recommend;
    private int like;
    private int visitor;
    private int collect;
    private int review;
}
