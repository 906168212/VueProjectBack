package com.example.mapper;

import com.example.Entity.dto.Account;
import com.example.Entity.dto.NavDTO;
import com.example.Entity.vo.response.NavVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = CustomMapper.class)
public interface NavMapper {
    @Mapping(source = "sid",target = "uid")
    @Mapping(source = "accountDetails",target = "details")
    @Mapping(source = "vipInfo",target = "vip")
    @Mapping(source = "levelInfo",target = "level")
    NavDTO toNavDTO(Account account);

    @Mapping(source = "level.currentLevel",target = "level.currentLevelName",qualifiedByName = "mapLevelToName")
    NavVO toNavVO(NavDTO navDTO);
}
