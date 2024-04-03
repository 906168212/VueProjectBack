package com.example.Entity.dto;

import com.example.Entity.BaseData;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "account")
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

    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private AccountDetails accountDetails;
    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private LevelInfo levelInfo;
    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private VipInfo vipInfo;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ArticleInfo> articleInfoList;
}
