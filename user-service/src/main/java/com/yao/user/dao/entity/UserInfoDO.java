package com.yao.user.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户基础信息实体。
 */
@Data
@TableName("tb_user_info")
public class UserInfoDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    private String mobile;

    private String nickname;

    private Integer status;

    @TableField(
            value = "created_at",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER
    )
    private LocalDateTime createdAt;

    @TableField(
            value = "updated_at",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER
    )
    private LocalDateTime updatedAt;
}
