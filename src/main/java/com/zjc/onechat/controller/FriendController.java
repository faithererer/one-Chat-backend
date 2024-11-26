package com.zjc.onechat.controller;

import com.zjc.onechat.dto.FriendDTO;
import com.zjc.onechat.dto.FriendRequestDTO;
import com.zjc.onechat.dto.FriendSearchDTO;
import com.zjc.onechat.dto.Result;
import com.zjc.onechat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 通过ID搜索好友
     * @param friendId 好友ID
     * @return 好友信息
     */
    @GetMapping("/search")
    public ResponseEntity<Result> searchFriend(@RequestParam Long friendId) {
        FriendSearchDTO friend = friendService.searchFriend(friendId);
        if (friend != null) {
            return ResponseEntity.ok(Result.ok(friend));
        } else {
            return ResponseEntity.ok(Result.ok(null));
        }
    }

    /**
     * 发送好友申请
     * @param requestBody 用户ID 好友ID
     * @return 成功或失败消息
     */
    @PostMapping("/add")
    public ResponseEntity<Result> addFriend(@RequestBody Map<String, String> requestBody) {
        try {
            // 发起者
            long userId = Long.parseLong(requestBody.get("userId"));
            // 被发起者
            long friendId = Long.parseLong(requestBody.get("friendId"));
            friendService.sendFriendRequest(userId, friendId);
            return ResponseEntity.ok(Result.ok("好友申请已发送"));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.fail("发送好友申请失败：" + e.getMessage()));
        }
    }

    /**
     * 处理好友申请
     * @param requestId 好友申请ID
     * @param accept 是否同意
     * @return 成功或失败消息
     */
    @GetMapping("/handleRequest")
    public ResponseEntity<Result> handleFriendRequest(@RequestParam Long requestId, @RequestParam boolean accept) {
        try {
            friendService.handleFriendRequest(requestId, accept);
            return ResponseEntity.ok(Result.ok("好友申请已处理"));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.fail("处理好友申请失败：" + e.getMessage()));
        }
    }
    @GetMapping("/friendRequestList")
    public ResponseEntity<Result> fetchFriendRequest(String userId){
        List<FriendRequestDTO> friendRequests = friendService.fetchFriendRequest(userId);
        return ResponseEntity.ok(Result.ok(friendRequests));
        // 查询申请
    }
    @GetMapping("/friendList")
    public  ResponseEntity<Result> fetchFriendList(String id){
        List<FriendDTO> friendList = friendService.fetchFriendList(id);
        return ResponseEntity.ok(Result.ok(friendList));
    }
}