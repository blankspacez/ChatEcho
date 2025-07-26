package com.seu.ai.repository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryChatHistoryRepository implements ChatHistoryRepository{

    private final Map<String, List<String> > chatHistory = new HashMap<>(); //基于内存的存储 type -> chatId

    @Override
    public void save(String type, String chatId) {
        if(chatHistory.containsKey(type)){
            List<String> chatIds = chatHistory.get(type);
            if(!chatIds.contains(chatId))
                chatIds.add(chatId);
        }else{
            chatHistory.put(type, new ArrayList<>());
        }
    }

    @Override
    public List<String> getAllChatIds(String type) {
        List<String> chatIds = chatHistory.get(type);
        if(chatIds != null)
            return chatIds;
        return new ArrayList<>();
    }
}
