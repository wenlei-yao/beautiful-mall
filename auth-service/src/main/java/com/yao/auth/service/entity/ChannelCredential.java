package com.yao.auth.service.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ChannelCredential {

    private String channelUserId;

    /**
     * 是否已经是主账号 userId（如账号密码登录为 true；微信登录为 false）
     */
    private boolean isDirectUserId;
    /**
     * 主账号 UserId（当 isDirectUserId = true 时使用）
     */
    private Long userId;

    private Map<String, Object> extParams;

    public static ChannelCredential ofUserId(Long userId) {
        return ChannelCredential.builder()
                .isDirectUserId(true)
                .userId(userId)
                .build();
    }

    public static ChannelCredential ofSocial(String channelUserId) {
        return ChannelCredential.builder()
                .isDirectUserId(false)
                .channelUserId(channelUserId)
                .build();
    }
}
