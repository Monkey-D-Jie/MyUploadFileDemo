package com.jf.myDemo.service;

import com.jf.myDemo.entities.FileInfoBean;
import com.jf.myDemo.interfaces.IFileService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-27 15:49
 * @Description: 操作文件的服务类
 * To change this template use File | Settings | File and Templates.
 */
@Service("fileService")
public class FileService implements IFileService{
    @Override
    public void addFileInfo(FileInfoBean fileInfoBean) {
//        System.out.println("临时打印：上传到FTP服务器的文件信息:"+fileInfoBean.toString());
    }
}
