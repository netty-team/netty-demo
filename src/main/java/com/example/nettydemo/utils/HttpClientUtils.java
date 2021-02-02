package com.example.nettydemo.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class HttpClientUtils {

    private OkHttpClient client;

    public HttpClientUtils(OkHttpClient client){
        this.client = client;
    }


    /**
     * get请求
     * @param requestJson
     * @return
     */
    public String get(String requestJson){

        log.info("执行GET方法");
        log.debug("request json: {}", requestJson);

        StringBuilder sb = new StringBuilder();

        Map map = JacksonUtil.json2Map(requestJson);
        String url = (String) map.get("url");
        sb.append(url);

        Map<String, String> param = (Map<String, String>) map.get("param");
        sb.append("?");
        if (param != null && !param.isEmpty()){
            param.forEach((k, v) -> {

                sb.append(k);
                sb.append("=");
                sb.append(v);
                sb.append("&");

            });
        }

        String requestURL = sb.substring(0, sb.length()-1);
        log.debug("request url: GET {}", requestURL);

        Map<String, String> headers = (Map<String, String>) map.get("headers");
        Headers.Builder headerBuild = new Headers.Builder();
        if (headers != null && !headers.isEmpty()){
            headers.forEach((k, v) -> {
                headerBuild.add(k, v);
            });
        }

        Request request = new Request.Builder()
                .url(requestURL)
                .headers(headerBuild.build())
                .build();

        Response response = null;

        try {

            response = client.newCall(request).execute();
            log.debug("response info: StateCode={}, body={}", response.code(), response.body().string());

            if (response.isSuccessful()) {
                return response.body().string();
            }

        }catch (IOException e){
//            e.printStackTrace();
            log.error(e.getMessage());

            return null;
        }

        return null;
    }

    /**
     * Post请求
     * @param requestJson
     * @return
     */
    public String post(String requestJson){


        log.info("执行POST方法");
        log.debug("request json: {}", requestJson);

        StringBuilder sb = new StringBuilder();
        Request.Builder requestBuilder = new Request.Builder();

        Map map = JacksonUtil.json2Map(requestJson);
        String url = (String) map.get("url");
        sb.append(url);

        Map<String, String> param = (Map<String, String>) map.get("param");
        sb.append("?");
        if (param != null && !param.isEmpty()){
            param.forEach((k, v) -> {

                sb.append(k);
                sb.append("=");
                sb.append(v);
                sb.append("&");

            });
        }

        String requestURL = sb.substring(0, sb.length()-1);
        log.debug("request url: POST {}", requestURL);
        requestBuilder.url(requestURL);

        Map<String, String> headers = (Map<String, String>) map.get("headers");
        Headers.Builder headerBuild = new Headers.Builder();
        if (headers != null && !headers.isEmpty()){
            headers.forEach((k, v) -> {
                headerBuild.add(k, v);
            });
        }
        requestBuilder.headers(headerBuild.build());

        Map<String, String> formdata = (Map<String, String>) map.get("formdata");
        if (formdata != null && !formdata.isEmpty()){
            FormBody.Builder formBuilder = new FormBody.Builder();

            formdata.forEach((k, v) -> {
                formBuilder.add(k, v);
            });

            requestBuilder.post(formBuilder.build());

        }

        String requestBody = (String) map.get("requestBody");
        if (requestBody != null){

            requestBuilder.post(RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                    requestBody));

        }


        Request request = requestBuilder.build();
        Response response = null;

        try {

            response = client.newCall(request).execute();
            String string = response.body().string();
            log.debug("response info: StateCode={}, body={}", response.code(), string);

            if (response.isSuccessful()) {
                return string;
            }

        }catch (IOException e){
//            e.printStackTrace();
            log.error(e.getMessage());

            return null;
        }

        return null;


    }

}
