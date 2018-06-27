package com.jf.myDemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-27 09:55
 * @Description: 操作文件的控制层类
 * To change this template use File | Settings | File and Templates.
 */
@Controller
@RequestMapping(("/handleFile"))
public class HandleFileControler {
    @RequestMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file,String data){
        return null;
    }
}
