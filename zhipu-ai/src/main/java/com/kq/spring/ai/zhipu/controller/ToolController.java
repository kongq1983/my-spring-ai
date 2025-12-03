package com.kq.spring.ai.zhipu.controller;

import com.kq.spring.ai.zhipu.tools.DateTimeTools;
import com.kq.spring.ai.zhipu.tools.LotteryTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ToolController {


    private final ChatClient chatClient;


    public ToolController(ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder
//                .defaultSystem("你是一个精通Java的工程师，专门解决Java遇到的问题。")// 设定默认角色
                .defaultSystem("你是一个时间助手! 主要协助用户回答时间方面的解答!!")// 设定默认角色
                .defaultUser("你是谁？")//设置默认问题
                // 默认工具
                .defaultTools(new DateTimeTools(),new LotteryTools())
                .build();

    }

    /**
     * http://localhost:10000/tool?input=帮我抽个奖
     * 当前用户输入后，返回文本类型的回答
     * @return
     */
    @GetMapping("/tool")
    public String chat(@RequestParam(value = "input",defaultValue = "现在时间")  String userInput) {
        String content = this.chatClient.prompt()
                .user(userInput)
                // 这里在defaultTools指定了
//                .tools(new DateTimeTools(),new LotteryTools())
                .call()
                .content();
        log.info("content: {}", content);
        return content;
    }

}
