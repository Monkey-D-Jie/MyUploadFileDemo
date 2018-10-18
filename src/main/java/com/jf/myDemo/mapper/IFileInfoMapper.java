package com.jf.myDemo.mapper;

import com.jf.myDemo.entities.FileInfoBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-07-04 16:29
 * @Description: 操作文件信息想的接口类
 * To change this template use File | Settings | File and Templates.
 */
@Component
public interface IFileInfoMapper {

    int insertFileInfo(FileInfoBean fileInfoBean);

    int batchInsertFileInfo(List<FileInfoBean> fileInfoBeans);
}
