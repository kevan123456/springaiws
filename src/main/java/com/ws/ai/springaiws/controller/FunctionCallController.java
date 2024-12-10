package com.ws.ai.springaiws.controller;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yunhua
 * @date 2024-12-10
 * @see
 * @since 1.0.0
 */
@RestController
@RequestMapping("functionCall")
public class FunctionCallController {
    @Autowired
    OpenAiChatModel chatModel ;


    @GetMapping("/chat")
    public String generation(@RequestParam(value = "message",defaultValue = "杭州有多少个叫王顺的人") String message) {
        OpenAiChatOptions aiChatOptions = OpenAiChatOptions.builder()
                //设置实现了Function接口的beanName
                .withFunction("locationNameFunction")
                .withModel(OpenAiApi.ChatModel.GPT_4_TURBO.getValue())
                .build() ;
        ChatResponse response = chatModel.call(new Prompt(message,aiChatOptions)) ;
        return response.getResult().getOutput().getContent() ;
    }
}

    