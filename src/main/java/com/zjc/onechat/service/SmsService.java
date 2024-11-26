package com.zjc.onechat.service;

import com.aliyun.tea.TeaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService implements ISmsService{

    @Autowired
    private com.aliyun.dysmsapi20170525.Client client;

    public String sendSms(String phone, String code) throws Exception {
        // 使用阿里云短信服务发送短信

        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("oneChat")
                .setTemplateCode("SMS_467445410")
                .setPhoneNumbers(phone)
                .setTemplateParam( String.format("{\"code\":\"%s\"}",code) );
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            System.out.println(client.sendSmsWithOptions(sendSmsRequest, runtime).statusCode);
        } catch (TeaException error) {
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
        return code;
    }
}
