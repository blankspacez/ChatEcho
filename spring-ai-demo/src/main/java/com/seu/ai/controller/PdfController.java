package com.seu.ai.controller;

import com.seu.ai.entity.vo.ResultVO;
import com.seu.ai.enumration.ServiceType;
import com.seu.ai.repository.ChatHistoryRepository;
import com.seu.ai.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/pdf")
public class PdfController {
    private final FileRepository fileRepository;

    private final VectorStore simpleVectorStore;

    private final ChatClient pdfChatClient;

    private final ChatHistoryRepository chatHistoryRepository;
    @RequestMapping("/upload/{chatId}")
    public ResultVO upload(@PathVariable String chatId, @RequestParam("file") MultipartFile file){
        try {
            if(!Objects.equals(file.getContentType(), "application/pdf")){
                return ResultVO.fail("请上传pdf文件!");
            }
            boolean success = fileRepository.saveFile(chatId, file.getResource());
            if(!success){
                return ResultVO.fail("上传失败");
            }
            this.writeToVectorStore(file.getResource());
            return ResultVO.ok();
        } catch (Exception e) {
            log.error("上传失败", e);
            return ResultVO.fail("上传失败");
        }
    }

    @GetMapping("/file/{chatId}")
    public ResponseEntity<Resource> download(@PathVariable String chatId) throws IOException {
        Resource resource = fileRepository.getFile(chatId);
        if(!resource.exists()){
            return ResponseEntity.notFound().build();
        }
        String fileName = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);

    }

    /**
     * 将pdf文件写入向量库
     * @param resource
     */
    private void writeToVectorStore(Resource resource) {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
                resource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        List<Document> documents = pdfReader.read();
        for(Document doc:documents){
            System.out.println(doc.getMetadata());
        }
        simpleVectorStore.add(documents);

    }

    @RequestMapping(value = "/chat", produces = "text/html;charset = utf-8")
    public Flux<String> chat(@RequestParam("prompt") String prompt, String chatId) {

        //获取pdf文件
        Resource resource = fileRepository.getFile(chatId);
        if(!resource.exists()){
            throw new RuntimeException("请先上传pdf文件!");
        }

        chatHistoryRepository.save(ServiceType.PDF.getValue(), chatId);
        //调用模型
        return pdfChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION,"file_name == '" + resource.getFilename() + "'"))
                .stream()
                .content();
    }
}
