package com.yao.user.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户登录凭证实体。
 */
@Data
@TableName("tb_user_identity")
public class UserIdentityDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("identity_type")
    private String identityType;

    private String identifier;

    private String credential;

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
