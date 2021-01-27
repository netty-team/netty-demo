package com.example.nettydemo.classloader.util;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClazzUtil {

    /**
     * 获取目录下的所有jar包列表
     *
     * @param jarPath
     */
    public static Map<String, Map<String, String>> getJarList(String jarPath) throws Exception {
        Map<String, Map<String, String>> jarMap = new HashMap<>();
        File file = new File(jarPath);
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
                Map<String, String> clazzMap = getJarName(jarAllPath);
                jarMap.put(jarAllPath, clazzMap);
            }

        }
        return jarMap;
    }


    /**
     * 获取jar包中所有的类全名
     *
     * @param jarFile
     * @return
     * @throws Exception
     */
    public static Map<String, String> getJarName(String jarFile) throws Exception {
        Map<String, String> clazzMap = new HashMap<>();

        //通过jarFile和JarEntry得到所有的类
        JarFile jar = new JarFile(jarFile);
        //返回zip文件条目的枚举
        Enumeration<JarEntry> enumFiles = jar.entries();
        JarEntry entry;

        //测试此枚举是否包含更多的元素
        while (enumFiles.hasMoreElements()) {
            entry = enumFiles.nextElement();
            if (!entry.getName().contains("META-INF")) {
                String classFullName = entry.getName();
                if (classFullName.endsWith(".class")) {
                    //去掉后缀.class
                    String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
                    //打印类名
//                    System.out.println("全类名:" + className);

                    clazzMap.put(className, className);
                }
            }
        }
        return clazzMap;
    }



    /**
     * 测试
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //jar包路径
        String jarPath = "D:\\class\\";

        Map<String, Map<String, String>> jarList = getJarList(jarPath);
        System.out.println();

    }

}