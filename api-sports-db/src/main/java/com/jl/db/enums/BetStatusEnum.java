package com.jl.db.enums;

import java.util.HashMap;
import java.util.Map;

public enum BetStatusEnum {

    COMMIT_SUCCESS("COMMIT_SUCCESS", 1 ,"提交成功,待确认"),
    COMMIT_FAIL("COMMIT_FAIL", 2 ,"提交失败"),
    BET_SUCCESS("BET_SUCCESS", 3 ,"投注成功,待派彩"),
    BET_FAIL("BET_FAIL", 4 ,"投注失败"),
    WIN("WIN", 5 ,"赢"),
    LOSE("LOSE", 6,"输"),
    VOID("VOID", 7,"赛事取消"),
    WIN_HALF("WIN_HALF", 9,"赢半"),
    LOSE_HALF("LOSE_HALF", 10,"输半"),
    CANCEL("CANCEL", 11,"提前结算取消"),
    ;

    private String code;
    private Integer value;
    private String desc;

    BetStatusEnum(String code, Integer value, String desc) {
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
        for (BetStatusEnum temp : BetStatusEnum.values()) {
            map.put(temp.getCode(), temp.getDesc());
        }
        return map;
    }
}
