package com.jf.myDemo.common.utilities.file;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-07-02 15:49
 * @Description: 复制文件的工具类
 * To change this template use File | Settings | File and Templates.
 * 更详细的内容参阅：java复制文件的4种方式 - CSDN博客
 * https://blog.csdn.net/u014263388/article/details/52098719
 */

public class MyCopyFileUtils {

    /**
     * logger
     */
    private static Logger LOGGER = LoggerFactory.getLogger(MyCopyFileUtils.class.getName());


    /**
     * Java7 后，直接用其Files对象
     *
     * @param source 源文件
     * @param dest   复制文件
     * @throws IOException
     */

    public static void copyFileUsingJava7Files(File source, File dest)
            throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    /**
     * 传统流的形式，注意write()时的长度，不然会造成文件打不开的问题
     *
     * @param source 源文件
     * @param dest   复制文件
     */
    public static void copyFileUsingStream(File source, File dest) {
        InputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(dest);
            //生成本地的临时文件
            byte[] fileBuffer = new byte[10240];
            int length = 0;
            while ((length = fis.read(fileBuffer, 0, fileBuffer.length)) != -1) {
                //也可以用直接写全部的方式，但容易造成内存溢出--->为什么呢？
                //fos.write(fileBuffer);
                fos.write(fileBuffer, 0, length);
            }
        } catch (Exception e) {
            try {
                fis.close();
                fos.close();
            } catch (Exception e1) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 用apache的commons-io包中封装的方法
     *
     * @param source 源文件
     * @param dest   复制文件
     * @throws IOException
     */
    public static void copyFileUsingApacheCommonsIO(File source, File dest)
            throws IOException {
        FileUtils.copyFile(source, dest);
    }

    /**
     * Java NIO包括transferFrom方法,根据文档应该比文件流复制的速度更快
     *
     * @param source 源文件
     * @param dest   复制文件
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            if (inputChannel != null && outputChannel != null) {
                inputChannel.close();
                outputChannel.close();
            }
            LOGGER.info(source.getName() + ">>>----文件复制成功！----->>>" + dest.getName() + "--->>>" + "成功复制后的文件大小为:" + dest.length());
        }
    }
}
