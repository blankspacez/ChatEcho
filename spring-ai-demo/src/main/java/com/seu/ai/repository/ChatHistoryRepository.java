package com.seu.ai.repository;

import java.util.List;

public interface ChatHistoryRepository {
    /**
     * 保存会话记录
     *
     * @param type 业务类型
     * @param chatId 会话ID
     */
    void save(String type, String chatId);

    /**
     * 获取所有会话ID
     *
     * @param type 业务类型
     * @return 会话ID列表
     */
    List<String> getAllChatIds(String type);
}
