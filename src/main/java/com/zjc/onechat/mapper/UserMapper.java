package com.zjc.onechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.onechat.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
