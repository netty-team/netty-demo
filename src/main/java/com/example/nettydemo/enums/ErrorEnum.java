package com.example.nettydemo.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {

    REQUEST_CONTENT_TYPE(601, "【CMDB】Content-Type缺失"),

    REQUEST_INVALID_CT(602, "【CMDB】无效的Content-Type类型")


    ;

    private int code;

    private String msg;

    ErrorEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
