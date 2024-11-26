package com.zjc.onechat.service;

public interface ISmsService {
    String sendSms(String phone, String message) throws Exception;
}
