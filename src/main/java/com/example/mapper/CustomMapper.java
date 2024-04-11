package com.example.mapper;


import java.sql.Timestamp;

public interface CustomMapper {
    long mapTimestampToLong(Timestamp timestamp);
    String mapCategoryToName(int category);
}
