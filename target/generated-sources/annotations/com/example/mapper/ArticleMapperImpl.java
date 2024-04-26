package com.example.mapper;

import com.example.Entity.dto.Account;
import com.example.Entity.dto.ArticleInfo;
import com.example.Entity.dto.ArticleStat;
import com.example.Entity.dto.ArticleStatDTO;
import com.example.Entity.dto.SimpleUserDTO;
import com.example.Entity.dto.UserAllArticleDTO;
import com.example.Entity.dto.UserArticleDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-17T14:10:28+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Autowired
    private CustomMapper customMapper;

    @Override
    public SimpleUserDTO toSimpleUserDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        SimpleUserDTO simpleUserDTO = new SimpleUserDTO();

        simpleUserDTO.setUid( account.getSid() );
        simpleUserDTO.setNickname( account.getNickname() );
        simpleUserDTO.setUsername( account.getUsername() );

        return simpleUserDTO;
    }

    @Override
    public ArticleStatDTO toArticleStatDTO(ArticleStat articleStat) {
        if ( articleStat == null ) {
            return null;
        }

        ArticleStatDTO articleStatDTO = new ArticleStatDTO();

        articleStatDTO.setMid( articleStat.getMid() );
        articleStatDTO.setLike( articleStat.getLike() );
        articleStatDTO.setVisitor( articleStat.getVisitor() );
        articleStatDTO.setCollect( articleStat.getCollect() );
        articleStatDTO.setReview( articleStat.getReview() );

        return articleStatDTO;
    }

    @Override
    public UserArticleDTO toUserArticleDTO(ArticleInfo articleInfo) {
        if ( articleInfo == null ) {
            return null;
        }

        UserArticleDTO userArticleDTO = new UserArticleDTO();

        userArticleDTO.setAid( articleInfo.getMid() );
        userArticleDTO.setStat( toArticleStatDTO( articleInfo.getArticleStat() ) );
        userArticleDTO.setCategory( customMapper.mapCategoryToName( articleInfo.getCategory() ) );
        userArticleDTO.setType( articleInfo.getType() );
        userArticleDTO.setTitle( articleInfo.getTitle() );
        userArticleDTO.setDesc( articleInfo.getDesc() );
        userArticleDTO.setPic( articleInfo.getPic() );
        userArticleDTO.setPicAvif( articleInfo.getPicAvif() );
        userArticleDTO.setPicWebp( articleInfo.getPicWebp() );
        userArticleDTO.setStatus( articleInfo.getStatus() );
        userArticleDTO.setPubDate( articleInfo.getPubDate() );
        userArticleDTO.setCreateTime( customMapper.mapTimestampToLong( articleInfo.getCreateTime() ) );
        userArticleDTO.setUpdateTime( articleInfo.getUpdateTime() );
        userArticleDTO.setRecommend( articleInfo.getRecommend() );

        return userArticleDTO;
    }

    @Override
    public List<UserArticleDTO> toUserArticleDTOList(List<ArticleInfo> articleInfoList) {
        if ( articleInfoList == null ) {
            return null;
        }

        List<UserArticleDTO> list = new ArrayList<UserArticleDTO>( articleInfoList.size() );
        for ( ArticleInfo articleInfo : articleInfoList ) {
            list.add( toUserArticleDTO( articleInfo ) );
        }

        return list;
    }

    @Override
    public UserAllArticleDTO toUserAllArticleDTO(Account account, List<UserArticleDTO> userArticleDTOList) {
        if ( account == null && userArticleDTOList == null ) {
            return null;
        }

        UserAllArticleDTO userAllArticleDTO = new UserAllArticleDTO();

        if ( account != null ) {
            userAllArticleDTO.setUser( toSimpleUserDTO( account ) );
        }
        if ( userArticleDTOList != null ) {
            List<UserArticleDTO> list = userArticleDTOList;
            if ( list != null ) {
                userAllArticleDTO.setArticle( new ArrayList<UserArticleDTO>( list ) );
            }
            userAllArticleDTO.setType( customMapper.generateTypeList( userArticleDTOList ) );
        }

        return userAllArticleDTO;
    }
}
