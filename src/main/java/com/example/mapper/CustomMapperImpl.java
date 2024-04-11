package com.example.mapper;

import com.example.Util.Const;
import org.mapstruct.Mapper;

import java.sql.Timestamp;

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
}
