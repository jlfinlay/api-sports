package com.jl.db.vo.outvo;

import com.jl.db.vo.BaseVO;
import lombok.Data;

import java.util.Date;


@Data
public class SportTypeConfigVO extends BaseVO {


    /**
     * 主键
     */
    private Integer id;

    /**
     * 业主id
     */
    private Integer agentId;

    /**
     * 体育游戏编号
     */
    private String gameCode;

    /**
     * 体育游戏名称
     */
    private String gameName;

    /**
     * 是否开启 0:开启 1:关闭
     */
    private Integer isOpen;

    /**
     * 排序
     */
    private Integer sort;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;


}
