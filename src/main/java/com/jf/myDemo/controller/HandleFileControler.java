package com.jf.myDemo.controller;

import com.jf.myDemo.common.utilities.date.DateFormatKit;
import com.jf.myDemo.common.utilities.file.FileUtils;
import com.jf.myDemo.entities.FileInfoBean;
import com.jf.myDemo.interfaces.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

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
    /** logger */
    private  Logger LOGGER = LoggerFactory.getLogger(HandleFileControler.class.getName());

    @Autowired
    private IFileService fileService;

    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    public void uploadFile(@RequestParam("file") CommonsMultipartFile file, HttpServletResponse response){
        String fileName = file.getOriginalFilename();
        try {
            this.LOGGER.info("-------上传文件的信息"+fileName+",contentType:"+file.getContentType()+"文件的真实类型为:"+FileUtils.getFileType(file.getInputStream())+"--------");
            String id = java.util.UUID.randomUUID().toString();
            FileUtils.uploadFTPForIns(file,id);

            FileInfoBean fileInfoBean = new FileInfoBean();
            fileInfoBean.setId(id);
            fileInfoBean.setStatus("A");
            fileInfoBean.setFileName(file.getOriginalFilename());
            fileInfoBean.setDate(DateFormatKit.convert(DateFormatKit.DATE_FORMAT,new Date()));
            this.fileService.addFileInfo(fileInfoBean);
            response.getWriter().write(fileInfoBean.getId());
            response.getWriter().flush();
            this.LOGGER.debug("-----文件上传成功，且信息已录入数据库-----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件ID获取图片输出流
     * @param request
     * @param response
     */
    @RequestMapping("/getfile")
    public void getSlide(HttpServletRequest request, HttpServletResponse response){

        response.setHeader("Content-Type","image/png");//设置响应的媒体类型，这样浏览器会识别出响应的是图片
        String fileid = request.getParameter("fileid");
        try {
            response.getOutputStream().write(FileUtils.getFileByte(fileid));
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
