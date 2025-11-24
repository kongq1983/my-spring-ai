package com.kq.spring.ai.zhipu.controller;

import com.kq.spring.ai.zhipu.service.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    /**
     * http://localhost:10000/ai/embedding
     * @param message
     * @return
     */
    @GetMapping("/ai/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("/ai/embedding1")
    public Map embed1(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {

        float[] embed = embeddingModel.embed(message);
        return Map.of("message",message,"vector", embed);
    }


    @GetMapping("/ai/embedding/best")
    public String getBest(@RequestParam(value = "message", defaultValue = "书籍") String message) {

        return this.embeddingService.queryBestMatch(message);
    }

}
