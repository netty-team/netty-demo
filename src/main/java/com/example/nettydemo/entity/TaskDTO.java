package com.example.nettydemo.entity;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Data;

@Data
public class TaskDTO {

    private boolean isDeail;

    private FullHttpRequest request;

    private FullHttpResponse response;

}
