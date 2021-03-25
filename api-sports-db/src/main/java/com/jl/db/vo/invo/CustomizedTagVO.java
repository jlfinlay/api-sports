package com.jl.db.vo.invo;

import lombok.Data;

import java.util.List;


@Data
public class CustomizedTagVO {

    /**
     * 代理标识配置表id列表
     */
    private List<Integer> proxyTagConfigIds;

    /**
     * 标签类型 1.代理标签 2.自定义标签
     */
    private Integer tagType;

    /**
     * 业主id
     */
    private Integer agentId;

    /**
     * 游戏id
     */
    private Integer gameId;


}
