package com.ws.ai.springaiws.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}

    