package com.example.mapper;

import com.example.Entity.dto.ArticleTypeDTO;
import com.example.Entity.dto.UserArticleDTO;
import com.example.Util.Const;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public class CustomMapperImpl implements CustomMapper{
    @Override
    public long mapTimestampToLong(Timestamp timestamp) {
        return timestamp.getTime();
    }

    @Override
    public String mapCategoryToName(int category) {
        return Const.categoryMap.get(category);
    }

    @Override
    public String mapLevelToName(int currentLevel) throws Exception {
        switch (currentLevel){
            case 1: return "新手上路";
            case 2: return "小试牛刀";
            case 3: return "崭露头角";
            case 4: return "轻车熟路";
            case 5: return "秋名车神";
            case 6: return "羽化登仙";
            default: throw new Exception("内部程序错误");
        }
    }

    @Override
    public List<ArticleTypeDTO> generateTypeList(List<UserArticleDTO> articleDTOList) {
        Map<Integer, Integer> typeCountMap = new HashMap<>();
        // 创建默认列表
        for (int i = 0;i<=Const.ARTICLE_TYPE;i++) {
            typeCountMap.put(i, 0);
        }
        // 遍历 article，统计每种类型的数量
        for (UserArticleDTO userArticleDTO : articleDTOList) {
            int type = userArticleDTO.getType();
            typeCountMap.put(type,typeCountMap.get(type) + 1);
        }
        // 将统计结果转换为 ArticleTypeDTO 对象列表
        List<ArticleTypeDTO> typeList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : typeCountMap.entrySet()) {
            ArticleTypeDTO type = new ArticleTypeDTO();
            type.setTid(entry.getKey());
            type.setName(Const.typeMap.get(entry.getKey()));
            type.setCount(entry.getValue());
            typeList.add(type);
        }
        return typeList;
    }
}
