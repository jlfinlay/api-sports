package com.jl.db.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@ApiModel(value = "PageBean", description = "返回数据分页信息")
public class PageBean<T> implements Serializable {

    @ApiModelProperty(name = "pageNo",value = "当前页码")
    private long pageNo;

    @ApiModelProperty(name = "pageSize",value = "当前页大小")
    private long pageSize;

    @ApiModelProperty(name = "total",value = "总条数")
    private long total;

    @ApiModelProperty(name = "data",value = "返回数据")
    private List<T> data;

    public static<T> PageBean page(long pageNo, long pageSize, long total, List<T> data){
        return new PageBean(pageNo, pageSize, total, data);
    }


}
