package com.example.nettydemo.service;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface TestService {

    public void dotest(FullHttpRequest req, FullHttpResponse rep);

}
