package com.example.Entity.dto;

import com.example.Entity.BaseData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "account")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "account.info",
                attributeNodes = {
                        @NamedAttributeNode("accountDetails"),
                        @NamedAttributeNode("levelInfo"),
                        @NamedAttributeNode("vipInfo")
                }
        ),
        @NamedEntityGraph(
                name = "account.article",
                attributeNodes = {
                        @NamedAttributeNode(value="articleInfoList",subgraph = "articleInfoListGraph"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name="articleInfoListGraph",
                                attributeNodes = @NamedAttributeNode("articleStat")
                        )
                }
        ),
        @NamedEntityGraph(
                name = "account.all",
                attributeNodes = {
                        @NamedAttributeNode("accountDetails"),
                        @NamedAttributeNode("levelInfo"),
                        @NamedAttributeNode("vipInfo"),
                        @NamedAttributeNode(value="articleInfoList",subgraph = "articleInfoListGraph"),
                },
                subgraphs = {
                        @NamedSubgraph(
                                name="articleInfoListGraph",
                                attributeNodes = @NamedAttributeNode("articleStat")
                        )
                }
        )
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Account implements BaseData {
    @Id     //表明主键
            @Column(name = "sid")       //表明数据表中的名称
            @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
            int sid;
    @Column(name = "username")
    String username;
    @Column(name = "nickname")
    String nickname;
    @Column(name = "password")
    String password;
    @Column(name = "email")
    String email;
    @Column(name = "role")
    String role;
    @Column
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createTime;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_wid")
    private Wallet wallet;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "details_id")
    private AccountDetails accountDetails;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private LevelInfo levelInfo;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "vip_id")
    private VipInfo vipInfo;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"account"})
    private List<ArticleInfo> articleInfoList;
}
