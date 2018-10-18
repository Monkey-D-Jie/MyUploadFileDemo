package com.jf.myDemo.interfaces;

import com.jf.myDemo.entities.FileInfoBean;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-27 15:46
 * @Description: 操作文件相关的接口类
 * To change this template use File | Settings | File and Templates.
 */

public interface IFileService {
    /**
     * 新增上传文件的信息
     *
     * @param fileInfoBean
     */
    void addFileInfo(FileInfoBean fileInfoBean);
    void getUserCount();
}
