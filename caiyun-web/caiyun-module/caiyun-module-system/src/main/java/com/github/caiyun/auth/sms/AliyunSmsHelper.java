package com.github.caiyun.auth.sms;


import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.github.framework.common.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class AliyunSmsHelper {

    @Resource(name = "aliyunClient")
    private Client client;

    /**
     * 发送短信
     * @param signName
     * @param templateCode
     * @param phone
     * @param templateParam
     * @return
     */
    public boolean sendMessage(String signName, String templateCode, String phone, String templateParam) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(phone)
                .setTemplateParam(templateParam);
        RuntimeOptions runtime = new RuntimeOptions();

        try {
            log.info("==> 开始短信发送, phone: {}, signName: {}, templateCode: {}, templateParam: {}", phone, signName, templateCode, templateParam);

//             //发送短信
//            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            // todo 模拟进行模拟发送短信成功
            SendSmsResponse response = new SendSmsResponse();

            // 固定返回200状态码
            response.setStatusCode(200);

            // 添加空请求头
            response.setHeaders(Map.of());

            // 构建成功响应体
            SendSmsResponseBody body = new SendSmsResponseBody();
            body.setCode("OK");
            body.setMessage("SMS模拟发送成功");
            body.setBizId("MOCK_" + System.currentTimeMillis());
            response.setBody(body);



            log.info("==> 短信发送成功, response: {}", JsonUtils.toJsonString(response));

            //否则调用对应服务进行发送处理
            return true;
        } catch (Exception error) {
            log.error("==> 短信发送错误: ", error);
            return false;
        }
    }
}