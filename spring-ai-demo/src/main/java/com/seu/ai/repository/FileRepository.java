package com.seu.ai.repository;


import org.springframework.core.io.Resource;

public interface FileRepository {

    /**
     * 保存文件，会话id -> 文件
     * @param chatId
     * @param resource
     * @return
     */
    boolean saveFile(String chatId, Resource resource);

    /**
     * 获取文件
     * @param chatId
     * @return
     */
    Resource getFile(String chatId);
}
