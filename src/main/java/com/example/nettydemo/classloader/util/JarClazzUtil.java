package com.example.nettydemo.classloader.util;

import com.example.nettydemo.classloader.lang.DynamicClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class JarClazzUtil {

    //利用此对象从虚拟内存中获取类对象，实现动态更新
    public static DynamicClassLoader dynamicClassLoader = null;

    //存放插件的类以及对应的key
    public static Map<String, String> clazzMap = new HashMap<>();

    /**
     * 获取目录下的所有jar包列表
     *
     * @param jarPath
     */
    public static String[] getJarList(String jarPath) throws Exception {
        List<String> list = new ArrayList<>();
        File file = new File(jarPath);
        String[] array = {};

        if (file.exists()) {
            File[] files = file.listFiles();
            for (File fe : files) {
                if (fe.getName().endsWith(".jar")) {
                    String name = fe.getName();
                    String jarAllPath;
                    if (jarPath.endsWith(File.separator)) {
                        jarAllPath = jarPath + name;
                    } else {
                        jarAllPath = jarPath + File.separator + name;
                    }
                    list.add(jarAllPath);
                }
            }
            array = list.toArray(new String[list.size()]);
        } else {
            log.warn("------------插件路径" + jarPath + "不存在，请重新配置！");
        }

        return array;
    }


    /**
     * 获取jar包中所有的类全名
     *
     * @param jarFile
     * @return
     * @throws Exception
     */
    public static Map<String, String> getClassMap(String[] jarFile) throws Exception {
        Map<String, String> clazzMap = new HashMap<>();

        for (String jf : jarFile) {
            //通过jarFile和JarEntry得到所有的类
            JarFile jar = new JarFile(jf);
            //返回zip文件条目的枚举
            Enumeration<JarEntry> enumFiles = jar.entries();
            JarEntry entry;

            //判断此枚举是否包含更多的元素
            while (enumFiles.hasMoreElements()) {
                entry = enumFiles.nextElement();
                if (!entry.getName().contains("META-INF")) {
                    String classFullName = entry.getName();
                    if (classFullName.endsWith(".class")) {
                        //去掉后缀.class
                        String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");

                        Class<?> aClass = dynamicClassLoader.loadClass(className);
                        Field[] fields = aClass.getFields();

                        boolean flag = false;
                        for (Field fd : fields) {
                            if (fd.getName().equals("PLUGIN")) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            log.warn("------------------------类：" + className + " 没有定义字段:PLUGIN");
                        } else {
                            //每一个插件定义统一的字段常量（PLUGIN）
                            Field field = aClass.getField("PLUGIN");
                            Object fieldValue = ReflectUtil.getFieldValue(field, aClass);
                            clazzMap.put(fieldValue.toString(), className);
                        }
                    }
                }
            }
        }

        return clazzMap;
    }


    /**
     * 测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //jar包路径
        String jarPath = "D:\\file\\aa";


        File file = new File(jarPath);
        if (!file.exists()) {
            System.out.println("--------------文件不存在！");
        }



        /*String[] jarList = getJarList(jarPath);
        //可以实现只加载一次，下次有更新的话，可以再次调用改方法的接口
        DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(jarList);
*/
        /*Class<?> testA = dynamicClassLoader.load("com.lxp.service.TestA");

        Field field = testA.getField("PLUGIN");


        Object fieldValue = ReflectUtil.getFieldValue(field, testA);*/
        System.out.println();

    }

}