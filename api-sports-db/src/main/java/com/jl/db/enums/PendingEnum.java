package com.jl.db.enums;

import java.util.HashMap;
import java.util.Map;

public enum PendingEnum {

    PENDING("Pending", 0 ,"未派彩"),
    PROCES("Proces", 1,"已派彩"),
    ;

    private String code;
    private Integer value;
    private String desc;

    PendingEnum(String code, Integer value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<String,String> toMap(){
        Map<String,String> map = new HashMap<>();
        for (PendingEnum temp : PendingEnum.values()) {
            map.put(temp.getCode(), temp.getDesc());
        }
        return map;
    }
}
