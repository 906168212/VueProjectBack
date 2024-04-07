package com.example.Repo;

import com.example.Entity.dto.ArticleInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

    @Repository
    @Transactional
    public interface ArticleInfoRepository extends JpaRepository<ArticleInfo,Integer> {
        @EntityGraph(value = "articleInfo_with_articleStat")
        @Query(value = "SELECT ai from ArticleInfo ai where ai.recommend = 1 ORDER BY RAND() LIMIT 4")
        List<ArticleInfo> findArticleInfosByRecommend(int recommend);

    }
