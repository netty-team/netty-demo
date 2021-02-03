package com.example.nettydemo.controller;

import com.example.nettydemo.annotation.FileHandler;
import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.entity.TaskDTO;
import com.example.nettydemo.enums.HTTPMethod;
import com.example.nettydemo.pool.BussinessTheadPool;
import com.example.nettydemo.service.TestService;
import com.example.nettydemo.service.impl.TestServiceImpl;
import com.example.nettydemo.utils.HttpClientUtils;
import com.example.nettydemo.utils.NettyVoUtils;
import com.example.nettydemo.utils.ResponseUtils;
import com.example.nettydemo.utils.SpringContextUtils;
import com.example.nettydemo.vo.NettyVo;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.sql.DataSource;
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
@Slf4j
public class AnnotationTest {

    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    public static final int HTTP_CACHE_SECONDS = 60;

    @Autowired
    private TestService service;
//    private TestService service = new TestServiceImpl();

    @Qualifier("primaryDataSource")
    @Autowired
    private DataSource db;

    @NettyRequestMapping(path = "/test", method = {HTTPMethod.GET, HTTPMethod.POST})
    public void test(TaskDTO dto){

        log.info("/test info");
        service.dotest(dto.getRequest(), dto.getResponse());
        log.debug("/test debug");


    }

    @Autowired
    @Qualifier(value = "okHttpClient")
    private OkHttpClient okHttpClient;

    @NettyRequestMapping(path = "/http", method = HTTPMethod.POST)
    public void httpTest(TaskDTO dto){

        FullHttpRequest request = dto.getRequest();
        FullHttpResponse response = dto.getResponse();
        String s = request.content().toString(CharsetUtil.UTF_8);

        HttpClientUtils utils = new HttpClientUtils(okHttpClient);
        String post = utils.post(s);

        NettyVo success = NettyVoUtils.success(post);
        ResponseUtils.doJsonResult(success, response);
    }

    @Autowired
    @Qualifier(value = "okHttpSSLClient")
    private OkHttpClient okHttpsClient;

    @NettyRequestMapping(path = "/https", method = HTTPMethod.POST)
    public void httpsTest(TaskDTO dto){

        FullHttpRequest request = dto.getRequest();
        FullHttpResponse response = dto.getResponse();
        String s = request.content().toString(CharsetUtil.UTF_8);

        HttpClientUtils utils = new HttpClientUtils(okHttpsClient);
        String post = utils.post(s);

        NettyVo success = NettyVoUtils.success(post);
        ResponseUtils.doJsonResult(success, response);
    }


    @NettyRequestMapping(path = "/download", method = {HTTPMethod.GET})
    @FileHandler
    public void downLoadTest(TaskDTO dto) {


    }

}
