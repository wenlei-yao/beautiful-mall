package com.yao.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 当前管理员角色摘要。
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAdminRoleRespDTO {

    private Long id;

    private String code;

    private String name;
}
