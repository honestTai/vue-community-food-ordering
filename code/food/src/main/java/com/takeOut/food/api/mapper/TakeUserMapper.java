package com.takeOut.food.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.takeOut.food.api.entity.TakeUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TakeUserMapper extends BaseMapper<TakeUser> {
}
