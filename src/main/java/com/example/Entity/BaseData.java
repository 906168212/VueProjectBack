package com.example.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.function.Consumer;

// 映射属性转移，例如将Account中的属性映射到vo中，用于快速转移属性
public interface BaseData {

    //写成lambda的形式就很帅，实现set操作
    default <V> V asViewObject(Class<V> clazz , Consumer<V> consumer){
        V v = this.asViewObject(clazz);
        consumer.accept(v);
        return v;
    }

    //实现转移操作
    default <V> V asViewObject(Class<V> clazz){
        try {
            // 获取所有已经声明的字段
            Field[] declaredFields = clazz.getDeclaredFields();
            // 获取构造器
            Constructor<V> constructor = clazz.getConstructor();
            // 通过构造器，指定vo对象的构造器构造出来
            V v = constructor.newInstance();
            // 同名字段拷贝
            for (Field declaredField : declaredFields) convert(declaredField,v);
            return v;
        }catch (ReflectiveOperationException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private void convert(Field filed, Object vo) {
        try {
            Field source = this.getClass().getDeclaredField(filed.getName());
            filed.setAccessible(true);
            source.setAccessible(true);
            filed.set(vo,source.get(this));
        }catch (IllegalAccessException | NoSuchFieldException ignored){}
    }
}
