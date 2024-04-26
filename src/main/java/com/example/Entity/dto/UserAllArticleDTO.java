package com.example.Entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserAllArticleDTO {
    private SimpleUserDTO user;
    private List<UserArticleDTO> article;
    private List<ArticleTypeDTO> type;
}
