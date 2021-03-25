package com.jl.db.utils.bean;


@FunctionalInterface
public interface BeanCopyCallBack<S, T> {

    void callback(S source, T target);


}
