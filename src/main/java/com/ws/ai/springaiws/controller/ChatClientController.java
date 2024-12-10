package com.ws.ai.springaiws.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author yunhua
 * @date 2024-12-09
 * @see
 * @since 1.0.0
 */
@RestController
@RequestMapping("ai")
public class ChatClientController {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private ChatModel chatModel ;

    @GetMapping("/chat")
    public String generation(@RequestParam(value = "message",defaultValue = "给我讲个笑话") String message) {
        //prompt提示词
        return this.chatClient.prompt()
                //用户信息
                .user(message)
                //远程请求大模型
                .call()
                //返回文本
                .content();
    }

    /**
     * 流方式返回需要编码
     * @param message
     * @return
     */
    @GetMapping(value = "/stream",produces = "text/html;charset=UTF-8")
    public Flux<String> stream(@RequestParam(value = "message",defaultValue = "给我讲个笑话") String message) {
        //prompt提示词
        Flux<String> output = chatClient.prompt()
                .user(message)
                //以流方式返回
                .stream()
                .content();
        return output ;
    }

    /**
     * chatModel是大模型独有的功能定制化配置。chatClient都是通用的，底层封装chatModel
     * @param message
     * @return
     */
    @GetMapping("/chatModel")
    public String chatModel(@RequestParam(value = "message",defaultValue = "给我讲个笑话") String message) {
        ChatResponse response = chatModel.call(
                new Prompt(
                        message,
                        OpenAiChatOptions.builder()
                                .withModel("gpt-4-32k")
                                .withTemperature(0.4)
                                .build()
                ));
        return response.getResult().getOutput().getContent() ;
    }
}

    