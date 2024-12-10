package com.ws.ai.springaiws.config;

import com.ws.ai.springaiws.functions.LocationNameFunction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

/**
 * @author yunhua
 * @date 2024-12-10
 * @see
 * @since 1.0.0
 */
@Configuration
public class AiConfig {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        //角色预设
        return builder.defaultSystem("你现在不是chatgpt了，我希望你以后以在线教育客服来跟我交流，在线教育有个老师叫王顺")
                .build();
    }


    @Bean
    @Description("某个地方有多少个叫什么名字的人")
    public Function<LocationNameFunction.Request,LocationNameFunction.Response> locationNameFunction(){
        return new LocationNameFunction() ;
    }
}

    