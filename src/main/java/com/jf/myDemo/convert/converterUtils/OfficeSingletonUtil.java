package com.jf.myDemo.convert.converterUtils;

import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.jodconverter.office.LocalOfficeManager;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-05-08 11:02
 * @Description: 用双同步锁单例的方式创建Manager对象
 * To change this template use File | Settings | File and Templates.
 */

public class OfficeSingletonUtil {

    private static volatile LocalOfficeManager singletonOpen = null;
    private static volatile OfficeManager singletonLibre = null;


    private OfficeSingletonUtil() {
    }

    public static LocalOfficeManager getOpenOfficeSingleton(String url, int[] ports) {
        if (singletonOpen == null) {
            synchronized (OfficeSingletonUtil.class) {
                if (singletonOpen == null) {
                    //在多个端口上开启openOffice的进程，并设置其任务最大执行时间和等待最长时间为90s
                    singletonOpen = LocalOfficeManager.builder().taskQueueTimeout(90000).portNumbers(ports).officeHome(url).install().build();
                }
            }
        }
        return singletonOpen;
    }

    public static OfficeManager getLibreOfficeSingleton(String url, int[] ports) {
        if (singletonLibre == null) {
            synchronized (OfficeSingletonUtil.class) {
                if (singletonLibre == null) {
                    DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
                    // libreOffice的安装目录
                    configuration.setOfficeHome(url);
                    // 端口号--默认也是8100端口
                    //在多个端口上开启libreOffice的进程，并设置其任务最大执行时间和等待最长时间为90s
                    configuration.setPortNumbers(ports);
                    //         设置任务执行超时为10分钟
                    configuration.setTaskExecutionTimeout(1000 * 60 * 25L);
                    //         设置任务队列超时为24小时
                    configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
                    singletonLibre = configuration.buildOfficeManager();
                }
            }
        }
        return singletonLibre;
    }
}
