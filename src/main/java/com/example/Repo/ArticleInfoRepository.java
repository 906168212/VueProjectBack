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
        @Query(value = "SELECT ai from ArticleInfo ai where ai.recommend = 1 AND ai.type = ?1 ORDER BY RAND() LIMIT 4")
        List<ArticleInfo> findRecommendArticleInfosByType(int type);

        @EntityGraph(value = "articleInfo_with_articleStat")
        @Query(value = "SELECT a from ArticleInfo a where a.category = ?1 ORDER BY RAND() LIMIT 4")
        List<ArticleInfo> findArticleInfosByCategory(int category);

        @EntityGraph(value = "articleInfo_with_articleStat")
        @Query(value = "SELECT a from ArticleInfo a where a.category = ?1 ORDER BY a.articleStat.visitor LIMIT 10")
        List<ArticleInfo> findArticleInfosByCategoryOrderByVisitor(int category);

        @EntityGraph(value = "articleInfo_with_articleStat")
        @Query(value = "SELECT a from ArticleInfo a where a.type = ?1 ORDER BY a.createTime DESC LIMIT 2")
        List<ArticleInfo> findLatestArticleInfos(int type);
    }
