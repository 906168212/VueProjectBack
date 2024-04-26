package com.example.mapper;

import com.example.Entity.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = CustomMapper.class)
public interface ArticleMapper {
    @Mapping(source = "sid",target = "uid")
    SimpleUserDTO toSimpleUserDTO(Account account);

    ArticleStatDTO toArticleStatDTO(ArticleStat articleStat);

    @Mapping(source = "mid",target = "aid")
    @Mapping(source = "articleStat",target = "stat")
    UserArticleDTO toUserArticleDTO(ArticleInfo articleInfo);

    List<UserArticleDTO> toUserArticleDTOList(List<ArticleInfo> articleInfoList);

    @Mapping(source = "account",target = "user")
    @Mapping(source = "userArticleDTOList",target = "article")
    @Mapping(source = "userArticleDTOList",target = "type",qualifiedByName = "generateTypeList")
    UserAllArticleDTO toUserAllArticleDTO(Account account,List<UserArticleDTO> userArticleDTOList);


}
