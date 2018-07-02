package com.jf.myDemo.controller;

import com.jf.myDemo.common.utilities.date.DateFormatKit;
import com.jf.myDemo.common.utilities.file.MyFileUtils;
import com.jf.myDemo.common.utilities.office.DocumentPreviewUtil;
import com.jf.myDemo.convert.converterUtils.MyLibreOfficeConverter;
import com.jf.myDemo.entities.FileInfoBean;
import com.jf.myDemo.interfaces.IFileService;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
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
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String contentType = null;

    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    public void uploadFile(@RequestParam("file") CommonsMultipartFile file, HttpServletResponse response){
        String fileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        try {
            this.contentType = file.getContentType();
            this.LOGGER.info("-------上传文件的信息---\n"+fileName+",contentType:"+contentType+",\n文件的真实类型为:"+ MyFileUtils.getFileRealSuffixType(file.getInputStream())+"---MimeType:"+ MyFileUtils.getMimeType(fileName)+"----extension:"+extension);
            String id = java.util.UUID.randomUUID().toString();
            //转码文件
            /**
             * 得到文件的实例
             */
            DiskFileItem diskFileItem = (DiskFileItem) file.getFileItem();
            /**
             * 得到临时文件在本地的临时存储位置
             */
            File resultFile = diskFileItem.getStoreLocation();
            File outputFile = new File("F:\\MyFtpServer\\temp\\temp.pdf");
            outputFile = MyFileUtils.getConvertFile(fileName,resultFile,outputFile);
            MyFileUtils.uploadFTPForInsV2(outputFile,id);

            FileInfoBean fileInfoBean = new FileInfoBean();
            fileInfoBean.setId(id);
            fileInfoBean.setStatus("A");
            fileInfoBean.setFileName(file.getOriginalFilename());
            fileInfoBean.setDate(DateFormatKit.convert(DateFormatKit.DATE_FORMAT,new Date()));
            this.fileService.addFileInfo(fileInfoBean);
            response.getWriter().write(DocumentPreviewUtil.DOCX_ID);
            response.getWriter().flush();
            this.LOGGER.debug("-----文件上传成功，且信息已录入数据库-----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/batchUploadFile",method = RequestMethod.POST)
    public void batchUploadFile(@RequestParam("files") CommonsMultipartFile[] files, HttpServletResponse response){
        List<String> idList = new ArrayList<>(8);
        StringBuilder ids = new StringBuilder("");
        try {
            for (CommonsMultipartFile file: files
                    ) {
                String fileName = file.getOriginalFilename();
                this.LOGGER.info("-------上传文件的信息"+fileName+",contentType:"+file.getContentType()+"文件的真实类型为:"+ MyFileUtils.getFileType(file.getInputStream())+"--------");
                String id = java.util.UUID.randomUUID().toString();

                //转码文件
                /**
                 * 得到文件的实例
                 */
                DiskFileItem diskFileItem = (DiskFileItem) file.getFileItem();
                /**
                 * 得到临时文件在本地的临时存储位置
                 */
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            System.out.println(Thread.currentThread().getName()+"的转码和上传文件任务正在执行中***********");
                            File resultFile = diskFileItem.getStoreLocation();
                            File outputFile = new File("F:\\MyFtpServer\\temp\\"+DateFormatKit.convert(DateFormatKit.DATE_FORMAT_THREE,new Date())+"-temp.pdf");
                            MyFileUtils.getConvertFile(fileName,resultFile,outputFile);
                            MyFileUtils.uploadFTPForInsV2(outputFile,id);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
//                MyFileUtils.uploadFTPForIns(file,id);
                idList.add(id);
                ids.append(DocumentPreviewUtil.DOCX_ID+",");
                FileInfoBean fileInfoBean = new FileInfoBean();
                fileInfoBean.setId(id);
                fileInfoBean.setStatus("A");
                fileInfoBean.setFileName(file.getOriginalFilename());
                fileInfoBean.setDate(DateFormatKit.convert(DateFormatKit.DATE_FORMAT,new Date()));
                /**
                 * 更好的处理方法：把传过来的文件都放到一个list中，直接扔到server层对应方法中去做事情。这里直接简化
                 */
                this.fileService.addFileInfo(fileInfoBean);
            }
            String idResult = ids.toString();
            idResult = idResult.substring(0,idResult.length()-1);
            System.out.println("最终的id合集为:"+idResult);
            response.getWriter().write(ids.toString());
            response.getWriter().flush();
            MyLibreOfficeConverter.getOfficeManager().stop();
            this.LOGGER.debug("-----批量文件上传成功，且信息已录入数据库-----");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 根据文件ID获取图片输出流
     * @param request
     * @param response
     */
    @RequestMapping("/getfile")
    public void getFile(HttpServletRequest request, HttpServletResponse response){
        //设置响应的媒体类型，这样浏览器会识别出响应的是图片
        response.setHeader("Content-Type","image/png");
        //文档类文件预览
//        response.setHeader("Content-Type",this.contentType);
        this.LOGGER.debug("-----文件上传成功，文档类型值为:-----"+this.contentType);
        String fileid = request.getParameter("fileid");
        try {
            response.getOutputStream().write(MyFileUtils.getFileByte(fileid));
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.LOGGER.debug("-----文件上传成功，预览图已在前端显示-----");
    }

    /*
     * 下载多个文件
     */
    @RequestMapping(value = "/download")
    public void downloadFiles(HttpServletRequest response) {
        /*String str= request.getParameter("rows");//下载文件信息，包括文件名、存储路径等
        JSONArray path=(JSONArray) JSONArray.parse(request.getParameter("rows"));
        Path paths[]=new Path[path.size()];
        paths = JSONArray.parseArray(str, Path.class).toArray(paths);
        String uri = "d:"+ File.separator + "mldn.zip";//临时文件存储路径
        File zipFile = new File(uri) ;   // 定义压缩文件名称
        ZipOutputStream zipOut = null;// 声明压缩流对象
        InputStream input = null;
        //将要压缩的文件加入到压缩输出流中
        try {
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int i = 0;i<paths.length;i++){
            File file = new File(paths[i].getUri()+File.separator+paths[i].getFilename());
            try {
                input = new FileInputStream(file) ;// 定义文件的输入流
                zipOut.putNextEntry(new ZipEntry(file.getName())) ; // 设置ZipEntry对象
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //将文件写入到压缩文件中
        int temp = 0 ;
        try {
            while((temp=input.read())!=-1){ // 读取内容
                zipOut.write(temp) ;    // 写到压缩文件中
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                input.close() ;
                zipOut.close() ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(zipFile));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-Disposition", "attachment;filename=" + zipFile.getName());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            zipFile.delete();        //将生成的服务器端文件删除
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }
}
