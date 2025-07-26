package com.seu.ai.controller;

import com.seu.ai.enumration.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 哄哄模拟器
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class GameController {
    private final ChatClient gameChatClient;

    @RequestMapping(value = "/game", produces = "text/html;charset = utf-8")
    public Flux<String> chat(@RequestParam("prompt") String prompt, String chatId) {
        //调用模型
        return gameChatClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
}
