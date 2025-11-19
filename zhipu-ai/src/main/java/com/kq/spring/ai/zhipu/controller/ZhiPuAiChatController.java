package com.kq.spring.ai.zhipu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ZhiPuAiChatController {

    private final ChatClient chatClient;


    public ZhiPuAiChatController(ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder
//                .defaultSystem("你是一个精通Java的工程师，专门解决Java遇到的问题。")// 设定默认角色
                .defaultSystem("你是一个幽默专家，专门讲各种笑话!")// 设定默认角色
                .defaultUser("你是谁？")//设置默认问题
                .build();

    }

    /**
     * http://localhost:10000/chat
     * 当前用户输入后，返回文本类型的回答
     * @return
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "userInput",defaultValue = "给我讲个100个字的笑话")  String userInput) {
        String content = this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
        log.info("content: {}", content);
        return content;
    }

    /**
     * 当前用户输入后，返回文本类型的回答，流式回答
     * @return
     */
//    @GetMapping(path = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ServerSentEvent<String>> chatStream(
//            @RequestParam(value = "userInput", defaultValue = "给我讲个100个字的笑话") String userInput) {
//
//        log.info("开始处理聊天请求: {}", userInput);
//
//        return chatClient.prompt()
//                .user(userInput)
//                .stream()
//                .content()
//                .doOnNext(content -> log.debug("【原始内容】: {}", content)) // 应该会打印
//                .map(content -> ServerSentEvent.<String>builder()
//                        .data(content)
//                        .build())
//                .doOnError(error -> log.error("流处理出错", error));
//    }

    @GetMapping(value = "/chat-stream", produces = "text/html;charset=UTF-8")
    public Flux<String> streamChat(@RequestParam(defaultValue = "给我讲个100个字的笑话")
                                   String question) {

        return chatClient.prompt()
                .user(question)
                .stream().content();
    }



    @GetMapping("/test-stream")
    public Mono<String> testStream() {
        return chatClient.prompt()
                .user("讲个100个字的笑话")
                .stream()
                .content()
                .take(1) // 取第一个 chunk
                .singleOrEmpty()
                .switchIfEmpty(Mono.just("【stream 没有返回任何数据】"));
    }

//    @GetMapping("/check-streaming")
//    public String checkStreaming() {
//        return chatModel instanceof ChatStreamingModel ? "支持流式" : "不支持流式";
//    }

}
