package com.example.mapper;

import com.example.Entity.dto.Account;
import com.example.Entity.dto.ArticleDTO;
import com.example.Entity.dto.ArticleInfo;
import com.example.Entity.dto.SimpleUserDTO;
import com.example.Entity.vo.ArticleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = CustomMapper.class)
public interface MainMapper {
    @Mapping(source = "mid",target = "aid")
    @Mapping(source = "account.sid",target = "user.uid")
    @Mapping(source = "articleStat",target = "stat")
    ArticleDTO toArticleDTO(ArticleInfo articleInfo);
    List<ArticleDTO> toArticleDTOList(List<ArticleInfo> articleInfoList);


    ArticleVO toArticleVO(ArticleDTO articleDTO);
    List<ArticleVO> toArticleVOList(List<ArticleDTO> articleDTOList);
}
