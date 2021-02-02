package com.example.nettydemo.controller;

import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.classloader.lang.DynamicClassLoader;
import com.example.nettydemo.classloader.util.JarClazzUtil;
import com.example.nettydemo.classloader.util.ReflectUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Method;
import java.util.Map;

@NettyController(path = "/loader")
public class PluginTestController {

    @Value("${classloader.plugin.path}")
    private String pluginPath;

    @NettyRequestMapping(path = "/test")
    public void testClassLoader(FullHttpRequest request, FullHttpResponse response) {
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

    /**
     * 重新加载插件
     *
     * @param request
     * @param response
     */
    @NettyRequestMapping(path = "/reload")
    public void reloadClass(FullHttpRequest request, FullHttpResponse response) {
        try {
            String[] jarList = JarClazzUtil.getJarList(pluginPath);
            JarClazzUtil.dynamicClassLoader = new DynamicClassLoader(jarList);
            JarClazzUtil.clazzMap = JarClazzUtil.getClassMap(jarList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NettyRequestMapping(path = "/map")
    public void testMap(FullHttpRequest request, FullHttpResponse response) {
        try {
            Map<String, String> clazzMap = JarClazzUtil.clazzMap;
            //根据调用插件传的key值获取类的全限定名
            String oracle = clazzMap.get("oracle");
            Class<?> aClass = JarClazzUtil.dynamicClassLoader.loadClass(oracle);
            //调用插件的方法
            Method method = ReflectUtil.getMethod("getValue", aClass);
            Object invoke = method.invoke(aClass.newInstance());
            //返回数据invoke
            System.out.println(invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
