package com.jl.openapi.game.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@Component
public class YsbPropertiesConfig {

    // URL域名
    @Value("${ysb.http.url}")
    private String baseUrl;

    // 秘钥
    @Value("${ysb.http.secretkey}")
    private String secretkey;

    // 商户号VID
    @Value("${ysb.http.vid}")
    private String vid;

    // 前缀
    @Value("${ysb.http.prefix}")
    private String prefix;

    // ip白名单
    @Value("${ysb.http.thirdIp}")
    private String thirdIp;

    // 币种
    @Value("${ysb.http.cr}")
    private String cr;

    // 语言
    @Value("${ysb.http.language}")
    private String language;
}
