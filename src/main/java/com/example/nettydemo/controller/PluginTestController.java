package com.example.nettydemo.controller;

import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.classloader.lang.DynamicClassLoader;
import com.example.nettydemo.classloader.util.ReflectUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.lang.reflect.Method;

@NettyController(path = "/loader")
public class PluginTestController {
    @NettyRequestMapping(path = "/test")
    public void testClassLoader(FullHttpRequest request, FullHttpResponse response){
        try {
            String path = "D:\\class\\test.jar";


            Class<?> testService = new DynamicClassLoader(path)
                    .load("com.lxp.service.TestServiceImpl");

            //获取TestServiceImpl类对象的eat()方法
            Method eat = ReflectUtil.getMethod("eat", testService);
            if (eat != null) {
                eat.invoke(testService.newInstance());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
