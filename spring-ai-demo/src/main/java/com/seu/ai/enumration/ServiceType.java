package com.seu.ai.enumration;

/**
 * 业务类型枚举
 */
public enum ServiceType {
    // 聊天
    CHAT("chat"),
    // 游戏
    GAME("game"),
    // 客服
    SERVICE("service"),
    // PDF
    PDF("pdf");

    private final String value;

    ServiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
