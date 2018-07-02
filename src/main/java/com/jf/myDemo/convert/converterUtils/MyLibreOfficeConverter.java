package com.jf.myDemo.convert.converterUtils;

import com.jf.myDemo.common.utilities.office.OfficeHomeUtil;
import com.jf.myDemo.common.utilities.txt.WriteTxtKit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-25 17:22
 * @Description: libreOffice专用的装换类
 * To change this template use File | Settings | File and Templates.
 */

public class MyLibreOfficeConverter {


    private static Logger logger = LogManager.getLogger(MyLibreOfficeConverter.class);

    public static OfficeManager getOfficeManager() {
        return officeManager;
    }

    private static volatile OfficeManager officeManager = null;
    //, 8005, 8006----->
    private static final int[] libreOfficePort = {8004};

    private static File outPutFile = null;

    public static void convertFile(File inputfile, File outputfile) {
        String LibreOffice_HOME = OfficeHomeUtil.getLibreOfficeHome();
        System.out.println("-------------获取到的LibreOffice_HOME---->>>" + LibreOffice_HOME+"********inputfile的大小:"+inputfile.length());
        String fileName = inputfile.getName();
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + "文件" + inputfile.getName());
        System.out.println(fileName.substring(fileName.lastIndexOf(".")));
        if (fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".txt")) {
            try {
                String txtContent = WriteTxtKit.getFileContentFromCharset(inputfile,"GBK");
                WriteTxtKit.saveFile2Charset(inputfile,"utf-8",txtContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        // libreOffice的安装目录
//        configuration.setOfficeHome(new File(LibreOffice_HOME));
        configuration.setOfficeHome(LibreOffice_HOME);
        // 端口号--默认也是8100端口
        configuration.setPortNumber(8200);
        configuration.setTaskExecutionTimeout(1000 * 60 * 25L);
//         设置任务执行超时为10分钟
        configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
//         设置任务队列超时为24小时
        OfficeManager officeManager = configuration.buildOfficeManager();
        officeManager.start();
        logger.info(new Date().toString() + "----开始转换......");
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.getFormatRegistry();
        try {
            converter.convert(inputfile, outputfile);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("转换失败");
        } finally {
//            System.out.println("*****************---officeManager关闭了---*****************");
//            officeManager.stop();
        }
        logger.info(new Date().toString() + "----转换结束....");
    }
    public static void convertFileThread(File inputFile, File outFile){
        try {
            synchronized (MyLibreOfficeConverter.class) {
                if (officeManager == null) {
                    officeManager = OfficeSingletonUtil.getLibreOfficeSingleton(OfficeHomeUtil.getLibreOfficeHome(), libreOfficePort);
                    if (officeManager != null && !officeManager.isRunning()) {
                        officeManager.start();
                        logger.info("libreOffice*******************officeManager对象开启成功，只有一次******************libreOffice");
                    }
                }
            }
            // Convert
            logger.info(new Date().toString()+"---->>"+Thread.currentThread().getName()+"---libreOffice----------开始转换-----------libreOffice");
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            converter.getFormatRegistry();
            String fileName = inputFile.getName();
            File file = null;
            if (fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".txt")) {
                String txtContent = WriteTxtKit.getFileContentFromCharset(inputFile, "GBK");
                file = new File("E:\\Users\\WordFiles\\temp\\"+fileName);
                file.createNewFile();
                WriteTxtKit.saveFile2Charset(file, "UTF-8", txtContent);
                converter.convert(file, outFile);
                file.delete();
            }else{
                converter.convert(inputFile, outFile);
            }
            logger.info(new Date().toString() +"---->>"+Thread.currentThread().getName()+  "---libreOffice----------转换结束-----------libreOffice");
        } catch (OfficeException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
