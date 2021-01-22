package com.example.nettydemo.except;

import com.example.nettydemo.enums.ErrorEnum;
import lombok.Getter;

@Getter
public class CmdbException extends RuntimeException{

    private int code;

    private String msg;

    public CmdbException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public CmdbException(ErrorEnum errorEnum){

        super(errorEnum.getMsg());

        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();

    }


}
