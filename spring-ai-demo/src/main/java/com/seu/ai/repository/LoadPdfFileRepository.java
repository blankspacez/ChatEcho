package com.seu.ai.repository;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoadPdfFileRepository implements FileRepository{

    private final VectorStore simpleVectorStore;

    private final Properties chatFiles= new Properties();
    @Override
    public boolean saveFile(String chatId, Resource resource) {
        String fileName = resource.getFilename();
        File file = new File(Objects.requireNonNull(fileName));
        if (!file.exists()) {
            try {
                Files.copy(resource.getInputStream(), file.toPath());
            }catch (Exception e){
                log.error("Failed to save file: " + fileName, e);
                return false;
            }
        }
        chatFiles.put(chatId, fileName);
        return true;
    }

    @Override
    public Resource getFile(String chatId) {
        return new FileSystemResource(chatFiles.getProperty(chatId));
    }

    /**
     * 持久化
     * properties文件保存pdf
     * json文件保存向量数据
     */
    @PostConstruct
    private void init(){
        FileSystemResource pdfResource = new FileSystemResource("chat-pdf.properties");
        if(pdfResource.exists()){
            try {
                chatFiles.load(new BufferedReader(new InputStreamReader(pdfResource.getInputStream())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        FileSystemResource vectorResource = new FileSystemResource("chat-pdf.json");
        if(vectorResource.exists()){
            SimpleVectorStore vectorStore = (SimpleVectorStore) this.simpleVectorStore;
            vectorStore.load(vectorResource);
        }
    }

    @PreDestroy
    private void persistent() {
        try {
            chatFiles.store(new FileWriter("chat-pdf.properties"), LocalDateTime.now().toString());
            SimpleVectorStore vectorStore = (SimpleVectorStore) simpleVectorStore;
            vectorStore.save(new File("chat-pdf.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
