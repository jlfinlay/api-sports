package com.jl.openapi.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class OpenApiPropertiesConfig {

    @Value("${tokenEncodeKey}")
    private String tokenEncodeKey; // = "+.lg_style.@Initializing..=123762";

    @Value("${game.url}")
    private String gameUrl;
}
