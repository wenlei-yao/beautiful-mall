package com.yao.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 当前管理员信息响应。
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAdminRespDTO {

    private Long id;

    private String username;

    private String displayName;

    private String mobile;

    private Boolean enabled;

    private List<CurrentAdminRoleRespDTO> roles;

    private List<String> permissions;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;

    private LocalDateTime createdTime;
}
