package com.github.caiyun.auth.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sms.aliyun")
@Component
@Data
//请在yaml文件注明你的配置
public class AliyunAccessKeyProperties {
    private String accessKeyId;
    private String accessKeySecret;
}