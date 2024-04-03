package com.example.Entity.dto;

import com.example.Entity.BaseData;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@Table(name = "article_info")
public class ArticleInfo implements BaseData{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    int mid;

    @ManyToOne
    @JoinColumn(name = "account_sid",referencedColumnName = "sid")
    private Account account;
    @OneToOne(mappedBy = "articleInfo",cascade = CascadeType.ALL)
    private ArticleStat articleStat;
    private String category;
    private String title;
    @Column(name = "`desc`")
    private String desc;
    private String pic;
    private String picAvif;
    private String picWebp;
    private int status;
    private long pubDate;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createTime;
    private long updateTime;
    private int recommend;
}
