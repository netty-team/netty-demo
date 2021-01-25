package com.example.nettydemo.service.impl;

import com.example.nettydemo.service.TestService;
import com.example.nettydemo.utils.NettyVoUtils;
import com.example.nettydemo.utils.ResponseUtils;
import com.example.nettydemo.vo.NettyVo;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.HashMap;
import java.util.Map;

public class TestServiceImpl implements TestService {


    @Override
    public void dotest(FullHttpRequest req, FullHttpResponse rep) {

        Map<String, Object> params = new HashMap<>();
        params.put("xx", 1);
        params.put("test", "2323");

        NettyVo vo = NettyVoUtils.success(params);
        ResponseUtils.doJsonResult(vo, rep);

    }
}
