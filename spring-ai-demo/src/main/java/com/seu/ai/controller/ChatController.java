package com.seu.ai.controller;

import com.seu.ai.enumration.ServiceType;
import com.seu.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
/**
 * 聊天机器人
 */
public class ChatController {

    private final ChatClient chatClient;

    private final ChatHistoryRepository  chatHistoryRepository;

    /*@GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }*/


    /**
     * 支持多模态聊天
     */
    @RequestMapping(value = "/chat", produces = "text/html;charset = utf-8")
    public Flux<String> chat(@RequestParam("prompt") String prompt,
                             @RequestParam("chatId") String chatId,
                             @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        //保存会话Id
        chatHistoryRepository.save(ServiceType.CHAT.getValue(), chatId);

        //调用模型
        if(files==null||files.isEmpty()){
            return textChat(prompt, chatId);
        }else{
            return multimodalChat(prompt, chatId, files);
        }
    }

    /**
     * 文本对话
     * @param prompt
     * @param chatId
     * @return
     */
    public Flux<String> textChat(String prompt, String chatId) {
        //调用模型
        return chatClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }

    /**
     * 多媒体对话
     * @param prompt
     * @param chatId
     * @param files
     * @return
     */
    public Flux<String> multimodalChat(String prompt, String chatId, List<MultipartFile> files) {
        //解析files
        List<Media> medias = files.stream()
                .map(file -> new Media(
                                MimeType.valueOf(Objects.requireNonNull(file.getContentType())),
                                file.getResource()
                        )
                ).toList();
        //调用模型
        return chatClient.prompt()
                .user(p->p.text(prompt).media(medias.toArray(Media[]::new)))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
}
