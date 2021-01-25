package com.example.nettydemo.test;

import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class ReflectTest {

    public static void main(String[] args) {
        getRequestMappingMethod("com.example.nettydemo.controller", "/class/hhh");
    }

    /**
     * @param scanPackage 需要扫描的包路径
     */
    private static void getRequestMappingMethod(String scanPackage, String path) {
        //设置扫描路径
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(scanPackage))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));

        Set<Class<?>> nettyConstructors = reflections.getTypesAnnotatedWith(NettyController.class);
        nettyConstructors.forEach(nettyController -> {

            NettyController controllerAnnation = nettyController.getAnnotation(NettyController.class);
            String subPath = controllerAnnation.path();
            if (path.startsWith(subPath)){

                Method[] declaredMethods = nettyController.getDeclaredMethods();
                for (Method method: declaredMethods){

                    NettyRequestMapping annotation = method.getAnnotation(NettyRequestMapping.class);
                    if (annotation != null){

                        String methodPath = annotation.path();
                        if (path.endsWith(methodPath)){

                            try {

                                method.invoke(nettyController.newInstance(), "hha", "sdfsd");

                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }

            }


        });

//        //扫描包内带有@RequiresPermissions注解的所有方法集合
//        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequiresPermissions.class);
//
//        List<Auth> list = new ArrayList<>();
//        Date now = new Date();
//
//        //循环获取方法
//        methods.forEach(method -> {
//
//            //用于保存方法的请求类型
//            String methodType = "";
//
//            //获取类上的@RequestMapping注解的值，作为请求的基础路径
//            String authUrl = method.getDeclaringClass().getAnnotation(RequestMapping.class).value()[0];
//
//            //获取方法上的@PutMapping,@GetMapping,@PostMapping,@DeleteMapping注解的值，作为请求路径,并区分请求方式
//            if (method.getAnnotation(PutMapping.class) != null) {
//                methodType = "put";
//                if (method.getAnnotation(PutMapping.class).value().length > 0) {
//                    authUrl = method.getAnnotation(PutMapping.class).value()[0];
//                }
//            } else if (method.getAnnotation(GetMapping.class) != null) {
//                methodType = "get";
//                if (method.getAnnotation(GetMapping.class).value().length > 0) {
//                    authUrl = method.getAnnotation(GetMapping.class).value()[0];
//                }
//            } else if (method.getAnnotation(PostMapping.class) != null) {
//                methodType = "post";
//                if (method.getAnnotation(PostMapping.class).value().length > 0) {
//                    authUrl = method.getAnnotation(PostMapping.class).value()[0];
//                }
//            } else if (method.getAnnotation(DeleteMapping.class) != null) {
//                if (method.getAnnotation(DeleteMapping.class).value().length > 0) {
//                    authUrl = method.getAnnotation(DeleteMapping.class).value()[0];
//                }
//            }
//
//            //使用Auth对象来保存值
//            Auth auth = new Auth();
//            auth.setMethodType(methodType);
//            auth.setAuthUniqueMark(method.getAnnotation(RequiresPermissions.class).value()[0]);
//            auth.setAuthUrl(authUrl);
//            auth.setAuthName(method.getDeclaringClass().getAnnotation(Api.class).value() + "-" + method.getAnnotation(ApiOperation.class).value());
//            auth.setCreateTime(now);
//            list.add(auth);
//        });
//        //TODO 输出到控制台,此处存数据库即可
//        System.out.println(JSON.toJSONString(list));
    }
}