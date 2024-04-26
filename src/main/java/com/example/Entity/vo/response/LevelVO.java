package com.example.Entity.vo.response;

import lombok.Data;

@Data
public class LevelVO {
    private int currentExp;
    private int currentLevel;
    private String currentLevelName;
    private Integer nextExp;
}
