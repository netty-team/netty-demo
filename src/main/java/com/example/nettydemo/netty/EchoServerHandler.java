package com.example.nettydemo.netty;

import com.example.nettydemo.annotation.FileHandler;
import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.entity.TaskDTO;
import com.example.nettydemo.enums.ErrorEnum;
import com.example.nettydemo.enums.HTTPMethod;
import com.example.nettydemo.utils.NettyVoUtils;
import com.example.nettydemo.utils.ResponseUtils;
import com.example.nettydemo.utils.SpringContextUtils;
import com.example.nettydemo.vo.NettyVo;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/***
 * 服务端自定义业务处理handler
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private String content = "";
    private final static String LOC = "302";
    private final static String NOT_FOND = "404";
    private final static String BAD_REQUEST = "400";
    private final static String INTERNAL_SERVER_ERROR = "500";
    private static Map<String, HttpResponseStatus> mapStatus = new HashMap<String, HttpResponseStatus>();
    private static Map<String, List<Class<?>>> controllerMap = new HashMap<>();

    static {
        mapStatus.put(LOC, HttpResponseStatus.FOUND);
        mapStatus.put(NOT_FOND, HttpResponseStatus.NOT_FOUND);
        mapStatus.put(BAD_REQUEST, HttpResponseStatus.BAD_REQUEST);
        mapStatus.put(INTERNAL_SERVER_ERROR, HttpResponseStatus.INTERNAL_SERVER_ERROR);


        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.example.nettydemo.controller"))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));

        Set<Class<?>> nettyConstructors = reflections.getTypesAnnotatedWith(NettyController.class);
        nettyConstructors.forEach(nettyController -> {

            NettyController annotation = nettyController.getAnnotation(NettyController.class);
            String path = annotation.path();

            List<Class<?>> classList = controllerMap.get(path);
            if (classList == null){
                classList = new ArrayList<>();
                controllerMap.put(path, classList);
            }
            classList.add(nettyController);
        });
    }

    /**
     * 对每一个传入的消息都要调用；
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {

            FullHttpRequest request = (FullHttpRequest) msg;
            boolean keepaLive = HttpUtil.isKeepAlive(request);
            HttpMethod method = request.method();
            log.info("method:" + method);
            log.info("uri:" + request.uri());
            String uri = request.uri().replace("/", "").trim();

            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            if (mapStatus.get(uri) != null) {
                httpResponse.setStatus(mapStatus.get(uri));
                httpResponse.content().writeBytes(mapStatus.get(uri).toString().getBytes());
            } else {
                dispatcher(ctx, request, httpResponse);
            }

            //重定向处理
            if (httpResponse.status().equals(HttpResponseStatus.FOUND)) {
                httpResponse.headers().set(HttpHeaderNames.LOCATION, "https://www.baidu.com/");
            }

            if (keepaLive) {
                httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(httpResponse);
            } else {
                ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    public void dispatcher(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response){

        String uri = request.uri();
        String reqMethodName = request.method().name();

        TaskDTO dto = new TaskDTO();
        dto.setDeail(false);

        controllerMap.forEach((k, v) -> {

            if (uri.startsWith(k)){
                String subK = uri.substring(k.length());

                boolean isDeail = false;

                for (Class<?> clazz: v){

                    //处理完毕 跳出循环
                    if (isDeail){
                        break;
                    }

                    Object bean = SpringContextUtils.getBean(clazz);
                    Method[] methods = clazz.getMethods();

                    for (Method method: methods){

                        NettyRequestMapping annotation = method.getAnnotation(NettyRequestMapping.class);
                        if (annotation != null){

                            String methodPath = annotation.path();
                            HTTPMethod[] httpMethodArray = annotation.method();

                            //判断方法路径是否一致
                            if (subK.equals(methodPath)){

                                // 判断请求方式是否包含
                                boolean flag = false;
                                for(int i = 0; i < httpMethodArray.length; i++){

                                    if (reqMethodName.equals(httpMethodArray[i].getValue())){
                                        flag = true;
                                    }
                                }

                                if (!flag){
                                    continue;
                                }

                                try {

                                    FileHandler fileHandler = method.getAnnotation(FileHandler.class);
                                    if (fileHandler != null){
                                        method.invoke(bean, ctx, request, response);
                                        isDeail = true;
                                        dto.setDeail(true);
                                        break;
                                    }else {
                                        method.invoke(bean, request, response);
                                        isDeail = true;
                                        dto.setDeail(true);
                                        break;
                                    }

                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }
            }
        });

        if (!dto.isDeail()){
            log.info("没有找到适配的处理方法：{} {}", reqMethodName, uri);

            NettyVo nettyVo = NettyVoUtils.failed(ErrorEnum.REQUEST_INVALID);
            ResponseUtils.doJsonResult(nettyVo, response);


        }

    }


    /**
     * 通知ChannelInboundHandler最后一次对channelRead()的调用时当前批量读取中的的最后一条消息。
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间，有异常抛出时会调用。
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
