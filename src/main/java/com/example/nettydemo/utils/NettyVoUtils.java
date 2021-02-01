package com.example.nettydemo.utils;

import com.example.nettydemo.enums.ErrorEnum;
import com.example.nettydemo.vo.NettyVo;

public class NettyVoUtils {

    public static NettyVo doNetty(int code, String msg, Object data){

        NettyVo vo = new NettyVo();
        vo.setCode(0);
        vo.setMsg(msg);
        vo.setData(data);

        return vo;
    }

    public static NettyVo success(){
        return doNetty(0,"调用成功", null);
    }

    public static NettyVo success(Object data){
        return doNetty(0, "调用成功", data);
    }

    public static NettyVo success(String msg, Object data){
        return doNetty(0, msg, data);
    }

    public static NettyVo failed(int code){
        return doNetty(code, "调用失败", null);
    }

    public static NettyVo failed(int code, String msg){
        return doNetty(code, msg, null);
    }

    public static NettyVo failed(int code, String msg, String data){
        return doNetty(code, msg, data);
    }

    public static NettyVo failed(ErrorEnum requestInvalid) {
        return failed(requestInvalid.getCode(), requestInvalid.getMsg());
    }
}
