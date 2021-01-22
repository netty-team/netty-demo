package com.example.nettydemo.utils;

import com.example.nettydemo.vo.NettyVo;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;

public class ResponseUtils {

    public static void doJsonResult(NettyVo vo, FullHttpResponse response){

        String content = JacksonUtil.object2Jsonstr(vo);
        response.content().writeBytes(content.getBytes());

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json;charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

    }

    public static void redirectResult(FullHttpResponse response){

        response.setStatus(HttpResponseStatus.FOUND);
    }
}
