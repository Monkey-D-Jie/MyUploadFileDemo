package com.jf.myDemo.service;

import com.jf.myDemo.entities.FileInfoBean;
import com.jf.myDemo.interfaces.IFileService;
import com.jf.myDemo.mapper.IFileInfoMapper;
import com.jf.myDemo.mapper.IUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private IFileInfoMapper fileInfoMapper;
    @Autowired
    private IUserMapper userMapper;

    @Override
    public void addFileInfo(FileInfoBean fileInfoBean) {
        this.fileInfoMapper.insertFileInfo(fileInfoBean);
    }

    @Override
    public void getUserCount() {
        System.out.println("**********************************");
        Map<String,Object> parameterMap = new HashMap<>(2);
        parameterMap.put("sexid", 1);
        parameterMap.put("usercount", -1);
        System.out.println("调用存储过程获取到的数据信息为:"+this.userMapper.getUserCount(parameterMap));
        System.out.println("**********************************");

    }
}
