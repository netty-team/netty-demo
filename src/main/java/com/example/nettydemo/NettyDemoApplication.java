package com.example.nettydemo;

import com.example.nettydemo.netty.EchoServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyDemoApplication implements CommandLineRunner {

    @Value("${netty.port}")
    private int port;

    @Value("${netty.url}")
    private String url;

    @Autowired
    private EchoServer server;


    public static void main(String[] args) {
        SpringApplication.run(NettyDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        ChannelFuture start = server.start(url, port);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {

                server.destroy();

            }
        });

        start.channel().closeFuture().syncUninterruptibly();
    }
}
