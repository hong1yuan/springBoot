package com.spring.springboot.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池
 * @author LiLin
 *
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

    /**
     * 线程池维护线程的最少数量
     */
    private int dealUserDataCorePoolSize = 20;
    /**
     * 线程池维护线程的最大数量
     */
    private int dealUserDataMaxPoolSize = 100;
    /**
     * 线程池所使用的缓冲队列
     */
    private int dealUserDataQueueCapacity = 1000;

    /**
     * 线程池维护线程的最少数量
     */
    private int filterUsersetCorePoolSize = 20;
    /**
     * 线程池维护线程的最大数量
     */
    private int filterUsersetMaxPoolSize = 100;
    /**
     * 线程池所使用的缓冲队列
     */
    private int filterUsersetQueueCapacity = 1000;
    
    /**
     * 线程池维护线程的最少数量
     */
    private int queryHbaseCorePoolSize = 20;
    /**
     * 线程池维护线程的最大数量
     */
    private int queryHbaseMaxPoolSize = 100;
    /**
     * 线程池所使用的缓冲队列
     */
    private int queryHbaseQueueCapacity = 1000;
    
    /**
     * 用户集数据处理线程池配置
     */
    @Bean
    public Executor dealUserDataExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(dealUserDataCorePoolSize);
        executor.setMaxPoolSize(dealUserDataMaxPoolSize);
        executor.setQueueCapacity(dealUserDataQueueCapacity);
        executor.setThreadNamePrefix("dealUserDataExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
    
    /**
     * 用户集数据过滤线程池配置
     */
    @Bean
    public Executor filterUsersetExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(filterUsersetCorePoolSize);
        executor.setMaxPoolSize(filterUsersetMaxPoolSize);
        executor.setQueueCapacity(filterUsersetQueueCapacity);
        executor.setThreadNamePrefix("filterUsersetExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
    
    /**
     * 查询Hbase数据线程池配置
     */
    @Bean
    public Executor queryHbaseExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(queryHbaseCorePoolSize);
        executor.setMaxPoolSize(queryHbaseMaxPoolSize);
        executor.setQueueCapacity(queryHbaseQueueCapacity);
        executor.setThreadNamePrefix("queryHbaseExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
