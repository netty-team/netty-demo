package com.example.nettydemo.controller;

import com.example.nettydemo.annotation.FileHandler;
import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.enums.HTTPMethod;
import com.example.nettydemo.pool.BussinessTheadPool;
import com.example.nettydemo.service.TestService;
import com.example.nettydemo.service.impl.TestServiceImpl;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@NettyController(path = "/netty")
public class AnnotationTest {

    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    public static final int HTTP_CACHE_SECONDS = 60;

    private TestService service = new TestServiceImpl();

    @NettyRequestMapping(path = "/test", method = {HTTPMethod.GET, HTTPMethod.POST})
    public void test(FullHttpRequest req, FullHttpResponse rep){

        System.out.println();
        service.dotest(req, rep);
        System.out.println();
    }


    @NettyRequestMapping(path = "/download", method = {HTTPMethod.GET})
    @FileHandler
    public void downLoadTest(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse rep) {


    }

}
