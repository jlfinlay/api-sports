package com.jl.openapi.utils;

import cn.hutool.core.util.RandomUtil;

public class RandomUtils {

    public static String getRandomUrl(String[] urls){
        String url = urls[RandomUtil.randomInt(urls.length)];
        return url;
    }
}
