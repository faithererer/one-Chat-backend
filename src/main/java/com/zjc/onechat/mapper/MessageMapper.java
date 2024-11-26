package com.zjc.onechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.onechat.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("SELECT * FROM message WHERE `sender_id`= #{friendId} AND `to_user_id`=#{userId}  ORDER BY sent_time DESC LIMIT 1")
    Message findLatestMessage(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
}
