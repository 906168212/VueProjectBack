package com.example.Entity.dto;

import com.example.Entity.BaseData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "article_info")
@NamedEntityGraphs(
        @NamedEntityGraph(
                name = "articleInfo_with_articleStat",
                attributeNodes = {
                        @NamedAttributeNode("account"),
                        @NamedAttributeNode("articleStat"),
                }
        )
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ArticleInfo implements BaseData{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    int mid;

    @ManyToOne
    @JoinColumn(name = "account_sid",referencedColumnName = "sid")
    @JsonIgnoreProperties({"articleInfoList"})
    private Account account;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "stat_mid")
    private ArticleStat articleStat;
    private int category;
    private String title;
    @Column(name = "`desc`")
    private String desc;
    private String pic;
    private String picAvif;
    private String picWebp;
    // status 文章的发布状态 null 没有 1 已发布 2 草稿箱
    private int status;
    private long pubDate;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createTime;
    private long updateTime;
    // recommend 文章是否为推荐类 1 推荐精选 0 普通
    private int recommend;
    // type  3 game
    private int type;
}
