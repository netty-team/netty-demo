package com.example.nettydemo.controller;

import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.enums.HTTPMethod;
import com.example.nettydemo.service.TestService;
import com.example.nettydemo.service.impl.TestServiceImpl;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

@NettyController(path = "/netty")
public class AnnotationTest {

    private TestService service = new TestServiceImpl();

    @NettyRequestMapping(path = "/test", method = {HTTPMethod.GET, HTTPMethod.POST})
    public void test(FullHttpRequest req, FullHttpResponse rep){

        System.out.println();
        service.dotest(req, rep);
        System.out.println();
    }

}
