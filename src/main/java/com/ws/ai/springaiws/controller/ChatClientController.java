package com.ws.ai.springaiws.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.FileOutputStream;

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
    //自动配置类
    @Autowired
    private ChatModel chatModel ;
    //自动配置类
    @Autowired
    private OpenAiImageModel openAiImageModel;
    @Autowired
    private OpenAiAudioSpeechModel openAiAudioSpeechModel ;

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

    /**
     * 文字转图片
     * @param message
     * @return
     */
    @GetMapping("/imageModel")
    public String imageModel(@RequestParam(value = "message",defaultValue = "画一只猫") String message) {
        ImageResponse response = openAiImageModel.call(
                new ImagePrompt(message,
                        OpenAiImageOptions.builder()
                                .withQuality("hd")
                                .withModel(OpenAiImageApi.DEFAULT_IMAGE_MODEL)
                                .withN(1)
                                .withHeight(1024)
                                .withWidth(1024).build())

        );

        return response.getResult().getOutput().getUrl() ;
    }

    /**
     * 文字转语音
     * @param message
     * @return
     */
    @GetMapping("/text2Audio")
    public String text2Audio(@RequestParam(value = "message",defaultValue = "hello,大家好我是王顺") String message) {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .withModel("tts-1")
                //语音的声音
                .withVoice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .withSpeed(1.0f)
                .build();

        SpeechPrompt speechPrompt = new SpeechPrompt(message, speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);
        byte[] body = response.getResult().getOutput() ;
        try {
            writeByteArray2Mp3(body,System.getProperty("user.dir")) ;
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
        return "ok" ;
    }

    public static void writeByteArray2Mp3(byte[] body,String dir) throws Exception{
        FileOutputStream fos = new FileOutputStream(dir+"/wangshun.mp3") ;
        fos.write(body);
        fos.close();
    }
}

    