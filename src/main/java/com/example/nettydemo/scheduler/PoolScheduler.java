package com.example.nettydemo.scheduler;

import com.example.nettydemo.pool.BussinessTheadPool;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PoolScheduler {
    @Scheduled(cron = "0 57 17 * * ?")
    public void excute(){
        BussinessTheadPool instance = BussinessTheadPool.Instance();

        System.out.println("1");
    }


}
