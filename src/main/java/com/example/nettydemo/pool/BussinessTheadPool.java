package com.example.nettydemo.pool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class BussinessTheadPool {

    private static BussinessTheadPool bussinessTheadPool;

    private ThreadPoolTaskExecutor executor;

    private int corePoolSize = 5;

    private int maxPoolSize = 200;

    private int queueCapacity = 10000;

    private String threadNamePrefix = "cmdb-thread-";

    private BussinessTheadPool(){
        executor = new ThreadPoolTaskExecutor();
    }

    public static BussinessTheadPool Instance(){

        if (bussinessTheadPool == null){
            bussinessTheadPool = new BussinessTheadPool();
        }

        return bussinessTheadPool;
    }

    /**
     * 配置核心线程数
     * @param size
     */
    public void setCorePoolSize(int size){
        this.corePoolSize = size;
    }

    /**
     * 配置最大线程数
     * @param size
     */
    public void setMaxPoolSize(int size){
        this.maxPoolSize = size;
    }

    /**
     * 配置队列大小
     * @param capacity
     */
    public void setQueueCapacity(int capacity){
        this.queueCapacity = capacity;
    }

    /**
     * 配置线程池中的线程的名称前缀
     * @param threadNamePrefix
     */
    public void setThreadNamePrefix(String threadNamePrefix){
        this.threadNamePrefix = threadNamePrefix;
    }

    public void initialize(){

        log.info("start bussiness thead Pool");
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(threadNamePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
    }

    public Future<?> submit(Runnable task){
        return executor.submit(task);
    }

    public <T> Future<T> submit(Callable<T> task){
        return executor.submit(task);
    }


}
