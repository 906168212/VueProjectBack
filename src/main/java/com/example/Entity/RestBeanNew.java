package com.example.Entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public record RestBeanNew<T> (int code, T data, String message) {
    //使用java9-17的新特性 record类型
    //工具类方法，快速构建对象
    public static <T> RestBeanNew<T> success(T data){
        return new RestBeanNew<>(200,data,"请求成功");
    }

    public static <T> RestBeanNew<T> success(T data,String message){return new RestBeanNew<>(200,data,message);}

    public static <T> RestBeanNew<T> success(String message){return new RestBeanNew<>(200,null,message);}

    public static <T> RestBeanNew<T> success(){return new RestBeanNew<>(200,null,"请求成功");}


    public static <T> RestBeanNew<T> failure(int code,String message){
        return new RestBeanNew<>(code,null,message);
    }

    public static <T> RestBeanNew<T> failure(int code){
        return failure(code,"请求失败");
    }

    //将当前对象转为JSON格式
    public String asJSONString(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
