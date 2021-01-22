package com.example.nettydemo.controller;

import com.example.nettydemo.enums.ErrorEnum;
import com.example.nettydemo.except.CmdbException;
import com.example.nettydemo.utils.NettyVoUtils;
import com.example.nettydemo.utils.ResponseUtils;
import com.example.nettydemo.vo.NettyVo;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CmdbTest {

    public void doTask(FullHttpRequest request, FullHttpResponse response){

        try {

            String contentTypes = request.headers().get("Content-Type");
            if (contentTypes == null){
                throw new CmdbException(ErrorEnum.REQUEST_CONTENT_TYPE);
            }
            String[] contentArrays = contentTypes.split(";");

            String contentType = contentArrays[0];

            switch (contentType){
                case "application/json":
                    doJson(request, response);
                    break;
                case "application/x-www-form-urlencoded":
                    doQuery(request, response);
                    break;
                case "multipart/form-data":
                    doInput(request, response);
                    break;
                default:
                    throw new CmdbException(ErrorEnum.REQUEST_INVALID_CT);
            }

        }catch (CmdbException e1){
            e1.printStackTrace();
            NettyVo vo = NettyVoUtils.failed(e1.getCode(), e1.getMessage());

//            ResponseUtils.doJsonResult(vo, response);
            ResponseUtils.redirectResult(response);
            return;

        }catch (Exception e){
            e.printStackTrace();
            NettyVo vo = NettyVoUtils.failed(600, e.getMessage());

            ResponseUtils.doJsonResult(vo, response);
            return;
        }


    }

    private void doInput(FullHttpRequest request, FullHttpResponse response) {
    }

    private void doQuery(FullHttpRequest request, FullHttpResponse response) {
    }

    private void doJson(FullHttpRequest request, FullHttpResponse response) {

        Map map = new HashMap();
        map.put("1", "erere");

        NettyVo vo = NettyVoUtils.success("成功", map);

        ResponseUtils.doJsonResult(vo, response);

        
    }


}
