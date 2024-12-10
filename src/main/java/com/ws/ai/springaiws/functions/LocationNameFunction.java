package com.ws.ai.springaiws.functions;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * @author yunhua
 * @date 2024-12-10
 * @see
 * @since 1.0.0
 */
public class LocationNameFunction implements Function<LocationNameFunction.Request,LocationNameFunction.Response> {


    /**
     * 接受GPT提取后的信息
     * @param request
     * @return
     */
    @Override
    public Response apply(Request request) {
        if(StringUtils.isBlank(request.name)||StringUtils.isBlank(request.location)){
            return new Response("参数缺失，无需function-call，正常响应即可。。") ;
        }
        //调用指定的接口，目前为了测试写死
        return new Response("有10个");
    }

    /**
     * 密封类，负责告诉GPT要提取哪些关键信息
     */
    public record Request(String name,String location){
    }

    /**
     * 最终响应
     */
    public record Response(String message){

    }
}

    