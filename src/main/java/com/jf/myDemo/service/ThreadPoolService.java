package com.jf.myDemo.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-07-03 17:21
 * @Description: 交由spring管理的线程池类
 * To change this template use File | Settings | File and Templates.
 */
@Service("threadPoolService")
public class ThreadPoolService {

    public ThreadPoolExecutor getThreadPoolExecutor() {
        //Thread创建工厂
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-%d").build();
        //拒绝策略
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, Integer.MAX_VALUE,60L, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), threadFactory, handler);
        return threadPoolExecutor;
    }

}
