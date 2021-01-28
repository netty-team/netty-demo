package com.example.nettydemo.controller;

import com.example.nettydemo.annotation.NettyController;
import com.example.nettydemo.annotation.NettyRequestMapping;
import com.example.nettydemo.classloader.lang.DynamicClassLoader;
import com.example.nettydemo.classloader.util.ReflectUtil;
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
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NettyController(path = "/class")
public class CmdbTest {

    @NettyRequestMapping(path = "/hhh", method = HTTPMethod.GET)
    public void testAnnotation(FullHttpRequest req, FullHttpResponse rep) {

        for (int j = 1; j <= 5; j++) {

            double count = Math.pow(10, j);

            System.out.println("开始时间------" + new Date());
            long time = new Date().getTime();
            for (double i = 0; i < count; i++) {

                try {
                    Class<?> springContextUtilsClass = Class.forName("com.example.nettydemo.utils.SpringContextUtils");
                    Method getBean = springContextUtilsClass.getMethod("getBean", String.class);
                    DataSource dataSource = (DataSource) getBean.invoke(springContextUtilsClass.newInstance(), "primaryDataSource");


                    Connection con = null;
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
//            String sql = "insert into test values(?,?,?)";
                    String sql = "select * from test";

                    try {
                        con = dataSource.getConnection();
                        preparedStatement = con.prepareStatement(sql);
                        resultSet = preparedStatement.executeQuery();
                        int row = resultSet.getRow();
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String name = resultSet.getString("name");
                            String age = resultSet.getString("aget");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        con.close();
                        preparedStatement.close();
                        resultSet.close();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("当前" + count + "次压测耗时：--------" + (new Date().getTime() - time));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("本地测试结束。。。。");

    }


    @NettyRequestMapping(path = "/plugin", method = HTTPMethod.GET)
    public void testPlugin(FullHttpRequest req, FullHttpResponse rep) {
        for (int j = 1; j <= 5; j++) {

            double count = Math.pow(10, j);

            System.out.println("开始时间------" + new Date());
            long time = new Date().getTime();
            for (double i = 0; i < count; i++) {
                try {
                    Class<?> aClass = DynamicClassLoader.getClazz("D:\\class\\test-a.jar", "com.lxp.service.TestA");
                    Method testA = ReflectUtil.getMethod("testAnnotation", aClass);
                    testA.invoke(aClass.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("当前" + count + "次压测耗时：--------" + (new Date().getTime() - time));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("插件测试结束。。。。");




    }

    public void doTask(FullHttpRequest request, FullHttpResponse response) {

        try {

            String contentTypes = request.headers().get("Content-Type");
            if (contentTypes == null) {
                throw new CmdbException(ErrorEnum.REQUEST_CONTENT_TYPE);
            }
            String[] contentArrays = contentTypes.split(";");

            String contentType = contentArrays[0];

            switch (contentType) {
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

        } catch (CmdbException e1) {
            e1.printStackTrace();
            NettyVo vo = NettyVoUtils.failed(e1.getCode(), e1.getMessage());

//            ResponseUtils.doJsonResult(vo, response);
            ResponseUtils.redirectResult(response);
            return;

        } catch (Exception e) {
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
