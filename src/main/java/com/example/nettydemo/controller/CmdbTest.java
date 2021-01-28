package com.example.nettydemo.controller;

import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.enums.ErrorEnum;
import com.example.nettydemo.enums.HTTPMethod;
import com.example.nettydemo.except.CmdbException;
import com.example.nettydemo.utils.NettyVoUtils;
import com.example.nettydemo.utils.ResponseUtils;
import com.example.nettydemo.vo.NettyVo;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@NettyController(path = "/class")
public class CmdbTest {

    @NettyRequestMapping(path = "/hhh", method = HTTPMethod.GET)
    public void testAnnotation(FullHttpRequest req, FullHttpResponse rep){

        System.out.println("hello world: " + req + " : " + rep);

        try {
            Class<?> springContextUtilsClass = Class.forName("com.example.nettydemo.utils.SpringContextUtils");
            Method getBean = springContextUtilsClass.getMethod("getBean", String.class);
            DataSource dataSource = (DataSource)getBean.invoke(springContextUtilsClass.newInstance(), "primaryDataSource");


            Connection con = null;
            PreparedStatement preparedStatement = null;
            String sql = "insert into test values(?,?,?)";

            try {
                con = dataSource.getConnection();
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1,21);
                preparedStatement.setString(2,"lixinping");
                preparedStatement.setString(3,"18");
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }  finally {
                con.close();
                preparedStatement.close();
            }

            System.out.println();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
