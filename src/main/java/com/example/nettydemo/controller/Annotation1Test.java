package com.example.nettydemo.controller;

import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.enums.HTTPMethod;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

@NettyController(path = "/netty")
public class Annotation1Test {

    @NettyRequestMapping(path = "/ui", method = HTTPMethod.GET)
    public void dotest(FullHttpRequest req, FullHttpResponse rep){
        System.out.println("Annotation1 测试");
    }

}
