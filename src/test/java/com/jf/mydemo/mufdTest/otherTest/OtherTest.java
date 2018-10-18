package com.jf.mydemo.mufdTest.otherTest;

import com.jf.myDemo.interfaces.IFileService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-28 09:50
 * @Description: 其他的测试类
 * To change this template use File | Settings | File and Templates.
 */

public class OtherTest {

    @Autowired
    private IFileService fileService;
//            = (IFileService)SpringContextUtil.getBean( "fileService");

    @Test
    public void printTest(){
        System.out.println("测试打印出的信息是否为乱码");
    }

    @Test
    public void procedureTest(){
        if(this.fileService != null){
            this.fileService.getUserCount();
        }else{
            System.out.println("fileService对象注入失败，请核查");
        }
    }
}
