package com.zjc.onechat.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class aaaController {
//    @Resource
//    StringRedisTemplate stringRedisTemplate;
//    @PostMapping("/sendCode")
//    public Result sendCode(@RequestBody User user) throws Exception {
//        System.out.println(user);
//        String code = RandomUtil.randomNumbers(4);
//        System.out.println(user.getPhone());
//        System.out.println(code);
//        stringRedisTemplate.opsForValue().set(user.getPhone(),code,5, TimeUnit.MINUTES);
//        smsTest.go(code);
//        return Result.ok("可以的");
//    }
//    @PostMapping("/auth")
//    public Result auth(@RequestBody User user, HttpSession httpSession) throws Exception {
//        System.out.println(user);
//        String rightCode = stringRedisTemplate.opsForValue().get(user.getPhone());
//        System.out.println("Stored code: " + rightCode);
//        System.out.println("Provided code: " + user.getCode());
//        System.out.println(httpSession.getId());
//        if (rightCode != null && user.getCode() != null && rightCode.equals(user.getCode())) {
//            return Result.ok();
//        } else {
//            return Result.fail("验证码错误");
//        }

}
