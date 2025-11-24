package com.kq.spring.ai.zhipu.controller;

import com.kq.spring.ai.zhipu.service.RagService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rag")
public class RagController {

    @Resource
    private RagService ragService;


    /**
     * http://localhost:10000/rag/ask?question=美酒
     * @param question
     * @return
     */
    @GetMapping("/ask")
    public Map<String,String> ask(@RequestParam(value = "question", defaultValue = "书籍") String question) {

        String resp = ragService.answer(question);
        return Map.of("question",question,"answer",resp);
    }


}
