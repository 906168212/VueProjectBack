package com.example.mapper;

import com.example.Entity.dto.Account;
import com.example.Entity.dto.ArticleDTO;
import com.example.Entity.dto.ArticleInfo;
import com.example.Entity.dto.ArticleStat;
import com.example.Entity.dto.ArticleStatDTO;
import com.example.Entity.dto.SimpleUserDTO;
import com.example.Entity.vo.ArticleVO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-10T13:11:33+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class MainMapperImpl implements MainMapper {

    @Autowired
    private CustomMapper customMapper;

    @Override
    public ArticleDTO toArticleDTO(ArticleInfo articleInfo) {
        if ( articleInfo == null ) {
            return null;
        }

        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setUser( accountToSimpleUserDTO( articleInfo.getAccount() ) );
        articleDTO.setAid( articleInfo.getMid() );
        articleDTO.setStat( articleStatToArticleStatDTO( articleInfo.getArticleStat() ) );
        articleDTO.setCategory( articleInfo.getCategory() );
        articleDTO.setTitle( articleInfo.getTitle() );
        articleDTO.setDesc( articleInfo.getDesc() );
        articleDTO.setPic( articleInfo.getPic() );
        articleDTO.setPicAvif( articleInfo.getPicAvif() );
        articleDTO.setPicWebp( articleInfo.getPicWebp() );
        articleDTO.setStatus( articleInfo.getStatus() );
        articleDTO.setPubDate( articleInfo.getPubDate() );
        articleDTO.setCreateTime( customMapper.mapTimestampToLong( articleInfo.getCreateTime() ) );
        articleDTO.setUpdateTime( articleInfo.getUpdateTime() );
        articleDTO.setRecommend( articleInfo.getRecommend() );

        return articleDTO;
    }

    @Override
    public List<ArticleDTO> toArticleDTOList(List<ArticleInfo> articleInfoList) {
        if ( articleInfoList == null ) {
            return null;
        }

        List<ArticleDTO> list = new ArrayList<ArticleDTO>( articleInfoList.size() );
        for ( ArticleInfo articleInfo : articleInfoList ) {
            list.add( toArticleDTO( articleInfo ) );
        }

        return list;
    }

    @Override
    public ArticleVO toArticleVO(ArticleDTO articleDTO) {
        if ( articleDTO == null ) {
            return null;
        }

        ArticleVO articleVO = new ArticleVO();

        articleVO.setAid( articleDTO.getAid() );
        articleVO.setUser( articleDTO.getUser() );
        articleVO.setCategory( customMapper.mapCategoryToName( articleDTO.getCategory() ) );
        articleVO.setTitle( articleDTO.getTitle() );
        articleVO.setDesc( articleDTO.getDesc() );
        articleVO.setPic( articleDTO.getPic() );
        articleVO.setPicAvif( articleDTO.getPicAvif() );
        articleVO.setPicWebp( articleDTO.getPicWebp() );
        articleVO.setStatus( articleDTO.getStatus() );
        articleVO.setPubDate( articleDTO.getPubDate() );
        articleVO.setCreateTime( articleDTO.getCreateTime() );
        articleVO.setUpdateTime( articleDTO.getUpdateTime() );
        articleVO.setRecommend( articleDTO.getRecommend() );
        articleVO.setStat( articleDTO.getStat() );

        return articleVO;
    }

    @Override
    public List<ArticleVO> toArticleVOList(List<ArticleDTO> articleDTOList) {
        if ( articleDTOList == null ) {
            return null;
        }

        List<ArticleVO> list = new ArrayList<ArticleVO>( articleDTOList.size() );
        for ( ArticleDTO articleDTO : articleDTOList ) {
            list.add( toArticleVO( articleDTO ) );
        }

        return list;
    }

    protected SimpleUserDTO accountToSimpleUserDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        SimpleUserDTO simpleUserDTO = new SimpleUserDTO();

        simpleUserDTO.setUid( account.getSid() );
        simpleUserDTO.setNickname( account.getNickname() );
        simpleUserDTO.setUsername( account.getUsername() );

        return simpleUserDTO;
    }

    protected ArticleStatDTO articleStatToArticleStatDTO(ArticleStat articleStat) {
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
}
