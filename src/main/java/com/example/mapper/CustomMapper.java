package com.example.mapper;


import com.example.Entity.dto.ArticleTypeDTO;
import com.example.Entity.dto.UserArticleDTO;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.util.List;

public interface CustomMapper {
    long mapTimestampToLong(Timestamp timestamp);
    String mapCategoryToName(int category);

    @Named("mapLevelToName")
    String mapLevelToName(int currentLevel) throws Exception;

    @Named("generateTypeList")
    List<ArticleTypeDTO> generateTypeList(List<UserArticleDTO> articleDTOList);
}
