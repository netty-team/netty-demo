package com.example.nettydemo;

import com.example.nettydemo.classloader.lang.DynamicClassLoader;
import com.example.nettydemo.classloader.util.JarClazzUtil;
import com.example.nettydemo.netty.EchoServer;
import com.example.nettydemo.netty.EchoServerHandler;
import com.example.nettydemo.netty.FileServerHandler;
import com.example.nettydemo.pool.BussinessTheadPool;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class NettyDemoApplication implements CommandLineRunner {

    @Value("${netty.json.port}")
    private int jsonPort;

    @Value("${netty.file.port}")
    private int filePort;

    @Value("${netty.url}")
    private String url;

    @Value("${thredpool.coreSize}")
    private int coreSize;

    @Value("${thredpool.maxSize}")
    private int maxSize;

    @Value("${thredpool.queueCapacity}")
    private int queueCapacity;

    @Value("${thredpool.threadNamePrefix}")
    private String threadNamePrefix;

    @Value("${classloader.plugin.path}")
    private String pluginPath;

    @Autowired
    private EchoServer server;

    @Autowired
    private EchoServerHandler echoServerHandler;

    @Autowired
    private FileServerHandler fileServerHandler;

    private BussinessTheadPool theadPool;


    public static void main(String[] args) {
        SpringApplication.run(NettyDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //初始化加载外部jar包插件
        String[] jarList = JarClazzUtil.getJarList(pluginPath);
        if (jarList.length > 0) {
            JarClazzUtil.dynamicClassLoader = new DynamicClassLoader(jarList);
            JarClazzUtil.clazzMap = JarClazzUtil.getClassMap(jarList);
        }


        ChannelFuture startJson = server.start(url, jsonPort, echoServerHandler);
//        ChannelFuture startFile = server.start(url, filePort, fileServerHandler);

        //初始化线程池
//        initThreadPool();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {

                server.destroy();
//                theadPool = null;

            }
        });

        startJson.channel().closeFuture().syncUninterruptibly();
//        startFile.channel().closeFuture().syncUninterruptibly();

    }

    public void initThreadPool() {

        theadPool = BussinessTheadPool.Instance();
        theadPool.setCorePoolSize(coreSize);
        theadPool.setMaxPoolSize(maxSize);
        theadPool.setQueueCapacity(queueCapacity);
        theadPool.setThreadNamePrefix(threadNamePrefix);

        theadPool.initialize();
    }
}
