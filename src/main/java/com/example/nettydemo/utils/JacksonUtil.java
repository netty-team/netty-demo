package com.example.nettydemo.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JacksonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(JacksonUtil.class);

    public static <T> T json2Object(String json, Class<T> clazz){

        T t = null;

        try {
            t = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("jackson 执行失败");
        }

        return t;

    }

    public static <T,S> T json2Object(String json, Class<T> tClass, Class<S> sClass){

        T t = null;

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(tClass, sClass);
        try {
            t = objectMapper.readValue(json, javaType);
        }catch (IOException e) {
            e.printStackTrace();
            return t;
        }

        return t;
    }

    public static String object2Jsonstr(Object obj){

        String objStr = "";
        try {
            objStr = objectMapper.writeValueAsString(obj);

            log.info(obj.getClass().getSimpleName() + ":" + objStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return objStr;
    }

    public static Map json2Map(String jsonStr){
        Map map = null;
        log.info(jsonStr);
        try{
            map = objectMapper.readValue(jsonStr, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static <T> List<T> json2List(String str, Class<T> clazz){

        List<T> result= null;

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);

        try {
            result = objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将yaml字符串转为json字符串
     * @param yamlStr
     * @return
     */
    public static String yaml2jsonStr(String yamlStr){

        Yaml yaml = new Yaml();
        Map<String, Object> yamlMap = (Map<String, Object>) yaml.load(yamlStr);

        String str;
        try {
            str = objectMapper.writeValueAsString(yamlMap);
        }catch (IOException e){
            e.printStackTrace();
            str = "";
        }

        return str;
    }

    /**
     * 将yaml字符串转换为对应的api对象
     * @param yamlStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T yaml2Obj(String yamlStr, Class<T> clazz){

        T t = null;

        String str = yaml2jsonStr(yamlStr);
        try {
            t = objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    /**
     * 将map转为对象
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T map2Object(Map map, Class<T> clazz){

        T t = null;
        String str = object2Jsonstr(map);

        return json2Object(str, clazz);

    }

    /**
     * json字符串 转 yaml字符串
     * @param jsonStr
     * @return
     */
    public static String json2yaml(String jsonStr){

        String str = "";
        Map<String, String> map = json2Map(jsonStr);

        try {

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);

            Yaml yaml = new Yaml(options);
            str = yaml.dump(map);

        }catch (Exception e){
            e.printStackTrace();
        }

        return str;


    }

    /**
     * 对象转yaml字符串
     * @param obj
     * @return
     */
    public static String obj2Yaml(Object obj){

        String jsonStr = object2Jsonstr(obj);
        if (jsonStr != null){

            return json2yaml(jsonStr);

        }
        return null;
    }

}
