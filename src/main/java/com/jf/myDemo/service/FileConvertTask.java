package com.jf.myDemo.service;

import com.jf.myDemo.common.utilities.file.MyFileUtils;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-07-03 15:19
 * @Description: 转换文件线程类
 * To change this template use File | Settings | File and Templates.
 */

public class FileConvertTask implements Runnable{

    private File convertFile;

    public FileConvertTask(){

    }
    public FileConvertTask(File file){
            this.convertFile = file;
    }


    @Override
    public void run() {
        try{
            String fileName = convertFile.getName();
            File outputFile = new File("F:\\MyFtpServer\\2pdfFile\\"+fileName.substring(fileName.indexOf(".")+1)+"-temp.pdf");
            MyFileUtils.converAndUploadFile(fileName,convertFile,outputFile);
            //删除掉临时文件
            if(convertFile != null){
                convertFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
