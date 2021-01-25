package com.example.nettydemo.enums;

import lombok.Getter;

@Getter
public enum HTTPMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    ;

    private String value;

    HTTPMethod(String value){
        this.value = value;
    }

}
