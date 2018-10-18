package com.jf.myDemo.controller;

import com.google.gson.GsonBuilder;
import com.jf.myDemo.common.utilities.date.DateFormatKit;
import com.jf.myDemo.common.utilities.file.MyCopyFileUtils;
import com.jf.myDemo.common.utilities.file.MyFileUtils;
import com.jf.myDemo.common.utilities.office.DocumentPreviewUtil;
import com.jf.myDemo.common.utilities.spring.SpringContextUtil;
import com.jf.myDemo.convert.converterUtils.MyLibreOfficeConverter;
import com.jf.myDemo.entities.FileInfoBean;
import com.jf.myDemo.interfaces.IFileService;
import com.jf.myDemo.service.FileConvertTask;
import com.jf.myDemo.service.ThreadPoolService;
import com.jf.myDemo.websocket.WebsocketHandler;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    /**
     * logger
     */
    private Logger LOGGER = LoggerFactory.getLogger(HandleFileControler.class.getName());

    @Autowired
    private IFileService fileService;

    private String contentType = null;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public void uploadFile(@RequestParam("file") CommonsMultipartFile file, HttpServletResponse response) {
        String fileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        String contentType = file.getContentType();
        try {
            this.contentType = file.getContentType();
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
            File tempFile = new File("F:\\MyFtpServer\\temp\\tempFile-upload."+FilenameUtils.getExtension(fileName));
            MyCopyFileUtils.copyFileUsingFileChannels(resultFile,tempFile);
            List<File> list = new ArrayList<>();
            list.add(tempFile);
//            convertFiletask(list);
            convertFiletaskV2(list,response);

            FileInfoBean fileInfoBean = new FileInfoBean();
            fileInfoBean.setId(id);
            fileInfoBean.setStatus("A");
            fileInfoBean.setFileName(file.getOriginalFilename());
            fileInfoBean.setDate(DateFormatKit.convert(DateFormatKit.DATE_FORMAT, new Date()));
            this.fileService.addFileInfo(fileInfoBean);
            PrintWriter out = response.getWriter();
            out.write(DocumentPreviewUtil.DOCX_ID);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/batchUploadFile", method = RequestMethod.POST)
    public void batchUploadFile(@RequestParam("files") CommonsMultipartFile[] files, HttpServletResponse response) {
        List<String> idList = new ArrayList<>(8);
        List<File> fileList = new ArrayList<>(6);
        StringBuilder ids = new StringBuilder("");
        int index = 1;
        try {
            for (CommonsMultipartFile file : files
                    ) {
                String fileName = file.getOriginalFilename();
                String id = java.util.UUID.randomUUID().toString();

                //转码文件
                /**
                 * 得到文件的实例
                 */
                DiskFileItem diskFileItem = (DiskFileItem) file.getFileItem();
                File resultFile = diskFileItem.getStoreLocation();
                File tempFile = new File("F:\\MyFtpServer\\temp\\tempFile-" + index+ fileName.substring(fileName.indexOf(".")));
                this.LOGGER.info("文件" + fileName + "的resultFile结果为:" + resultFile.getName() + "**********" + "生成的临时文件为:" + tempFile.getName());
                //复制好要转换的文件
                MyCopyFileUtils.copyFileUsingFileChannels(resultFile, tempFile);
                fileList.add(tempFile);
                /**
                 * 得到临时文件在本地的临时存储位置
                 */
                idList.add(id);
                ids.append(DocumentPreviewUtil.DOCX_ID + ",");
                FileInfoBean fileInfoBean = new FileInfoBean();
                fileInfoBean.setId(id);
                fileInfoBean.setStatus("A");
                fileInfoBean.setFileName(file.getOriginalFilename());
                fileInfoBean.setDate(DateFormatKit.convert(DateFormatKit.DATE_FORMAT, new Date()));
                /**
                 * 更好的处理方法：把传过来的文件都放到一个list中，直接扔到server层对应方法中去做事情。这里直接简化
                 */
                this.fileService.addFileInfo(fileInfoBean);
                index++;
            }
            //转码上传的文件----->什么时候算完呢？？
            convertFiletask(fileList);
            String idResult = ids.toString();
            idResult = idResult.substring(0, idResult.length() - 1);
            System.out.println("最终的id合集为:" + idResult);
            response.getWriter().write(ids.toString());
            PrintWriter out = response.getWriter();
            WebsocketHandler.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson("访问test接口")));
            out.println("访问test接口");
            response.getWriter().flush();
            if (MyLibreOfficeConverter.getOfficeManager() != null) {
                MyLibreOfficeConverter.getOfficeManager().stop();
            }
            this.LOGGER.debug("-----批量文件上传成功，且信息已录入数据库-----");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertFiletask(List<File> files) {
        /**
         * 用线程池的形式去执行文件的转码任务
         * 每隔 2 秒 就去 查看任务执行完没有
         */
        ThreadPoolExecutor threadPoolExecutor = ((ThreadPoolService) SpringContextUtil.getBean("threadPoolService")).getThreadPoolExecutor();
        for (int i = 0; i < files.size(); i++) {
            FileConvertTask task = new FileConvertTask(files.get(i));
            threadPoolExecutor.execute(task);
        }
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(false).build());
        executorService.scheduleAtFixedRate(() -> {
                long taskCount = threadPoolExecutor.getCompletedTaskCount();
                if(taskCount == files.size()){
                    executorService.shutdown();
                    LOGGER.info("******************传入的文件已全部转换完毕***************");
                }
        }, 0, 3000, TimeUnit.MILLISECONDS);
    }

    private void convertFiletaskV2(List<File> files,HttpServletResponse response) {
        /**
         * 用线程池的形式去执行文件的转码任务
         * 每隔 2 秒 就去 查看任务执行完没有
         */
        ThreadPoolExecutor threadPoolExecutor = ((ThreadPoolService) SpringContextUtil.getBean("threadPoolService")).getThreadPoolExecutor();
        for (int i = 0; i < files.size(); i++) {
            FileConvertTask task = new FileConvertTask(files.get(i));
            threadPoolExecutor.execute(task);
        }
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(false).build());
        executorService.scheduleAtFixedRate(() -> {
            long taskCount = threadPoolExecutor.getCompletedTaskCount();
            if(taskCount == files.size()){
                try{
                    executorService.shutdown();
                    //用webSocket发送消息给前端页面

                    WebsocketHandler.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson("上传文件任务执行完成*")));
                    LOGGER.info("******************传入的文件已全部转换完毕***************");

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 根据文件ID获取图片输出流
     *
     * @param request
     * @param response
     */
    @RequestMapping("/getfile")
    public void getFile(HttpServletRequest request, HttpServletResponse response) {
        //设置响应的媒体类型，这样浏览器会识别出响应的是图片
        response.setHeader("Content-Type", "image/png");
        //文档类文件预览
//        response.setHeader("Content-Type",this.contentType);
        this.LOGGER.debug("-----文件上传成功，文档类型值为:-----" + this.contentType);
        String fileid = request.getParameter("fileid");
        try {
            response.getOutputStream().write(MyFileUtils.getFileByte(fileid));
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.LOGGER.info("-----文件上传成功，预览图已在前端显示-----");
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
