package com.jf.myDemo.common.utilities.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-27 16:14
 * @Description: 通过Spring上下文获取到交由Spring管理的bean类
 * To change this template use File | Settings | File and Templates.
 */

public class SpringContextUtil implements ApplicationContextAware {
    /**
     * 容器变量
     */
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * 主要是这个方法在起作用
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 获取对象
     * @return  Object 一个以所给名字注册的bean的实例 (service注解方式，自动生成以首字母小写的类名为bean name)
     */
    public static Object getBean(String name) throws BeansException{
        return applicationContext.getBean(name);
    }
}
