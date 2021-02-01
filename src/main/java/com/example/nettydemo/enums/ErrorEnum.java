package com.example.nettydemo.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {

    REQUEST_INVALID(600, "【CMDB】无效的请求，没有找到对应的处理方法"),

    REQUEST_CONTENT_TYPE(601, "【CMDB】Content-Type缺失"),

    REQUEST_INVALID_CT(602, "【CMDB】无效的Content-Type类型"),

    REQUEST_INVALID_METHOD(603, "【CMDB】无效的请求方式"),




    ;

    private int code;

    private String msg;

    ErrorEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
