package com.jl.db.vo.invo;

import com.jl.db.vo.BaseVO;
import lombok.Data;

import java.util.Date;

@Data
public class CompetitionDataVO extends BaseVO {
    /**
     * id主键 自增长
     */
    private Integer id;

    /**
     * 业主id
     */
    private Integer agentId;

    /**
     * 游戏编码
     */
    private String gameCode;

    /**
     * 体育赛项名称
     */
    private String sportName;

    /**
     * 体育赛项ID
     */
    private String sportId;

    /**
     * 各种投注组别/类型  m:体育赛事 o:冠军 l:乐透
     */
    private String cat;

    /**
     * 是否滚球 y:滚球 n:非滚球
     */
    private String ql;

    /**
     * 优先值(用于决定赛事排序先后)
     */
    private String pr;

    /**
     * 赛事数量 JSON里面有4个数据( c:早盘赛事数量, t:今日赛事数量, a:早盘赛事 + 今日赛事 ,text:y)
     */
    private String gameCount;

    /**
     * 体育种类
     */
    private String sportType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 是否热门 y:热门 n:普通赛事
     */
    private String isHot;


}
