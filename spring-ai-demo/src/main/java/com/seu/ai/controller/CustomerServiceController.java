package com.seu.ai.controller;

import com.seu.ai.enumration.ServiceType;
import com.seu.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class CustomerServiceController {
    private final ChatClient serviceChatClient;

    private final ChatHistoryRepository chatHistoryRepository;

    @RequestMapping(value = "/service", produces = "text/html;charset = utf-8")
    public Flux<String> chat(@RequestParam("prompt") String prompt, String chatId) {
        //保存会话Id
        chatHistoryRepository.save(ServiceType.SERVICE.getValue(), chatId);
        //调用模型
        return serviceChatClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }

}
