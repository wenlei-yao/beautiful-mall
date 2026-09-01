package com.yao.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台管理员实体。
 */
@Data
@TableName("admin_user")
public class AdminUserDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String displayName;

    private String mobile;

    private Boolean enabled;

    private Boolean mustChangePassword;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;

    private LocalDateTime passwordUpdatedTime;

    private Long createdBy;

    private Long updatedBy;

    @TableField(
            value = "created_time",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER
    )
    private LocalDateTime createdTime;

    @TableField(
            value = "updated_time",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER
    )
    private LocalDateTime updatedTime;
}
