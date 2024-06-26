package com.example.Entity.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "article_stat")
public class ArticleStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private int mid;

    @Column(name = "`like`")
    private int like;
    private int visitor;
    private int collect;
    private int review;
}
