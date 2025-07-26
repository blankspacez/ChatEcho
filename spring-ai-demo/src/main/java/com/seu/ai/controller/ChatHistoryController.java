package com.seu.ai.controller;

import com.seu.ai.entity.vo.MessageVO;
import com.seu.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ai/history")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatHistoryRepository chatHistoryRepository;

    private final ChatMemory  chatMemory;

    @GetMapping("/{type}")
    public List<String> getAllChatIds(@PathVariable("type") String type) {
        return chatHistoryRepository.getAllChatIds(type);
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);
        if(messages==null){
            return new ArrayList<>();
        }else{
            return messages.stream().map(MessageVO::new).toList();
        }

    }
}
