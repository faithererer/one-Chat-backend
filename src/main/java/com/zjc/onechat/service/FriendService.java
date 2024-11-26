package com.zjc.onechat.service;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.onechat.dto.FriendDTO;
import com.zjc.onechat.dto.FriendRequestDTO;
import com.zjc.onechat.dto.FriendSearchDTO;
import com.zjc.onechat.entity.Friend;
import com.zjc.onechat.entity.FriendRequest;
import com.zjc.onechat.entity.User;
import com.zjc.onechat.entity.UserHolder;
import com.zjc.onechat.mapper.FriendMapper;
import com.zjc.onechat.mapper.FriendRequestMapper;
import com.zjc.onechat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FriendService {

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 通过ID搜索好友
     * @param userId 用户ID
     * @param friendId 好友ID
     * @return 好友信息
     */
    public FriendSearchDTO searchFriend(Long friendId) {
        User user = userMapper.selectById(friendId);
        FriendSearchDTO friendSearchDTO = new FriendSearchDTO();
        if(user!=null) {
            friendSearchDTO.setNickName(user.getNickName());
            friendSearchDTO.setAvatar(user.getAvatar());
            friendSearchDTO.setId(user.getId());
            // 判断是否为好友：
            Long userId = UserHolder.getUserId();
            Friend friend = friendMapper.selectOne(new QueryWrapper<Friend>()
                    .eq("user_id", userId)
                    .eq("friend_id", user.getId()));
            System.out.println(friend);
            System.out.println("他们的关系");
            System.out.println(friend!=null);
            friendSearchDTO.setIsFriend(friend!=null);
            if(Objects.equals(user.getId(), userId)){
                friendSearchDTO.setIsFriend(false);
            }
            System.out.println("搜索到:" + user);
        }
        return friendSearchDTO;
    }

    /**
     * 发送好友申请
     * @param userId 用户ID
     * @param friendId 好友ID
     */
    public void sendFriendRequest(Long userId, Long friendId) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setUserId(userId);
        friendRequest.setFriendId(friendId);
        friendRequestMapper.insert(friendRequest);
    }

    /**
     * 处理好友申请
     * @param userId 用户ID
     * @param requestId 好友申请ID
     * @param accept 是否同意
     */
    public void handleFriendRequest(Long requestId, boolean accept) {
        FriendRequest friendRequest = friendRequestMapper.selectById(requestId);
        if(friendRequest==null) return;
        Integer count = friendMapper.selectCount(new QueryWrapper<Friend>()
                .eq("user_id", friendRequest.getUserId())
                .eq("friend_id", friendRequest.getFriendId()));
        if (accept && count == 0) {
            Friend friend = new Friend();
            friend.setUserId(friendRequest.getUserId());
            friend.setFriendId(friendRequest.getFriendId());
            friendMapper.insert(friend);

            friend.setUserId(friendRequest.getFriendId());
            friend.setFriendId(friendRequest.getUserId());

            friendMapper.insert(friend);
        }
        friendRequestMapper.deleteById(requestId);
    }

    public List<FriendRequestDTO> fetchFriendRequest(String userId) {
        List<FriendRequest> friend_list = friendRequestMapper.selectList(new QueryWrapper<FriendRequest>()
                .select().eq("friend_id", userId));
        List<FriendRequestDTO> list = new ArrayList<>();
        friend_list.forEach((friendRequest -> {
            Long friendId = friendRequest.getUserId();
            User user = userMapper.selectById(friendId);
            FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
            friendRequestDTO.setId(friendRequest.getId());
            friendRequestDTO.setAvatar(user.getAvatar());
            friendRequestDTO.setNickName(user.getNickName());
            friendRequestDTO.setFriendId(friendRequest.getFriendId());
            friendRequestDTO.setUserId(friendRequest.getUserId());
            list.add(friendRequestDTO);
        }));
        return list;
    }

    public List<FriendDTO> fetchFriendList(String id) {
        List<FriendDTO> friendDTOList = new ArrayList<>();
        List<Friend> friendList = friendMapper.selectList(new QueryWrapper<Friend>()
                .eq("user_id", id));
        System.out.println(friendList);
        for( Friend friend : friendList){
            User user = userMapper.selectById(friend.getFriendId());
            System.out.println(user);
            FriendDTO bean = BeanUtil.toBean(user, FriendDTO.class);
            friendDTOList.add(bean);
        }


        return friendDTOList;

    }
}
