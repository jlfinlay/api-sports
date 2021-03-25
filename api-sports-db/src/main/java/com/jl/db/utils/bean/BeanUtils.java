package com.jl.db.utils.bean;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;


public class BeanUtils {

    public static void copyProperties(Object source, Object target) {
        if(source == null){
            return;
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    public static <T> T copyProperties(Object source, Supplier<T> supplier) {
        if(source == null){
            return null;
        }
        T t = supplier.get();
        copyProperties(source, t);
        return t;
    }

    public static <T, R> List<R> copyProperties(Collection<T> source, Supplier<R> supplier) {
        return copyProperties(source, supplier, null);
    }

    public static <T, R> List<R> copyProperties(Collection<T> source, Supplier<R> supplier, BeanCopyCallBack<T, R> callBack) {
        List<R> list = Lists.newArrayList();
        if(source != null && !source.isEmpty()){
            for (T t : source) {
                R r = supplier.get();
                copyProperties(t, r);
                if(callBack != null){
                    callBack.callback(t, r);
                }
                list.add(r);
            }
        }
        return list;
    }

}
