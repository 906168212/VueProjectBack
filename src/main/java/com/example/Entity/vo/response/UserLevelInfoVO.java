package com.example.Entity.vo.response;

import lombok.Data;

@Data
public class UserLevelInfoVO {
   private int currentExp;
   private int currentLevel;
   private String currentLevelName;
   private Integer nextExp;
}
