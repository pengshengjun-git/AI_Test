package com.aitest.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus自动填充配置
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

/**
 * 重写父类的insertFill方法，用于在插入数据时自动填充字段值
 * @param metaObject 元数据对象，包含实体类的相关信息
 */
    @Override
    public void insertFill(MetaObject metaObject) {
    // 调用fillStrategy方法为createTime字段填充当前时间
        this.fillStrategy(metaObject, "createTime", LocalDateTime.now());
    // 调用fillStrategy方法为updateTime字段填充当前时间
        this.fillStrategy(metaObject, "updateTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "updateTime", LocalDateTime.now());
    }
}
