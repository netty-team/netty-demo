package com.example.nettydemo.scheduler;

import com.example.nettydemo.netty.EchoServer;
import com.example.nettydemo.pool.BussinessTheadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PoolScheduler {

    @Autowired
    private EchoServer echoServer;

    @Scheduled(cron = "${scheduler.cron}")
    public void excute(){
        BussinessTheadPool instance = BussinessTheadPool.Instance();

        System.out.println("1");
    }


}
