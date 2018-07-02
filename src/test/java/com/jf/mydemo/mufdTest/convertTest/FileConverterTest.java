package com.jf.mydemo.mufdTest.convertTest;

import com.jf.myDemo.common.utilities.office.OfficeHomeUtil;
import com.jf.myDemo.convert.converterUtils.MyLibreOfficeConverter;
import com.jf.myDemo.convert.converterUtils.MyOpenOfficeConverter;
import com.jf.myDemo.convert.converterUtils.OfficeSingletonUtil;
import org.apache.commons.io.FileUtils;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: wjie
 * @date: 2018/1/9 0009
 * @time: 10:40
 * To change this template use File | Settings | File and Templates.
 * 来自：https://github.com/sbraconnier/jodconverter/wiki/Using-Filters
 */

public class FileConverterTest {

    private String libreHtmlDir = "E:\\Users\\libreOffice\\2Html\\";
    //    private String libreHtmlDir = "E:\\Users\\libreOffice\\2Html";
    private String openHtmlDir = "E:\\Users\\openOffice\\2Html\\";
    //    private String openHtmlDir = "E:\\Users\\openOffice\\2Html";
    private String librePdfDir = "E:\\Users\\libreOffice\\2Pdf\\";
    private String openPdfDir = "E:\\Users\\openOffice\\2Pdf\\";


    private CountDownLatch countDownLatch = new CountDownLatch(8);

    private LocalOfficeManager localOfficeManager = null;
    private OfficeManager officeManager = null;

    private int[] openOfficePort = {8001, 8002, 8003};
    private int[] libreOfficePort = {8004, 8005, 8006};


    //    @Before
    public void getManager() {
        this.localOfficeManager = OfficeSingletonUtil.getOpenOfficeSingleton(OfficeHomeUtil.getOpenOfficeHome(), openOfficePort);
        this.officeManager = OfficeSingletonUtil.getLibreOfficeSingleton(OfficeHomeUtil.getLibreOfficeHome(), libreOfficePort);
    }

    @Test
    public void fileConverTest() throws Exception {
        /**
         * 常规转换测试
         */
        File resFile = new File("E:\\Users\\WordFiles\\test-docx.docx");
//        File pdfFile = new File(libreHtmlDir+"testDocx-pdf.pdf");
        File pdfFile = new File(librePdfDir + "testDocx-pdf.pdf");

//        MyOpenOfficeConverter.convertFile(resFile,pdfFile);
        MyLibreOfficeConverter.convertFile(resFile, pdfFile);
        /*Pdf2htmlEXUtil.pdf2html("D:\\Temp\\pdf2html\\pdf2htmlEX.exe",openHtmlDir+"testDocx-pdf.pdf",libreHtmlDir,"testDocx-pdf.html");*/
    }

    @Test
    public void libreFileConvertTest() throws OfficeException {
        /**
         * 多线程场景测试-libreOffice
         */
        try {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    File resFile = new File("E:\\Users\\WordFiles\\test-docx.docx");
                    File pdfFile = new File(librePdfDir + "testDocx-pdf.pdf");
                    try {
                        MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                }
            });
            Thread t2 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-doc.doc");
                File pdfFile = new File(librePdfDir + "testDoc-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t3 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-ppt.ppt");
                File pdfFile = new File(librePdfDir + "testPpt-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t4 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-pptx.pptx");
                File pdfFile = new File(librePdfDir + "testPptx-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t5 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-xls.xls");
                File pdfFile = new File(librePdfDir + "testXls-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t6 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-xlsx.xlsx");
                File pdfFile = new File(librePdfDir + "testXlsx-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t7 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-txt.txt");
                File pdfFile = new File(librePdfDir + "testTxt-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t8 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-大docx.docx");
                File pdfFile = new File(librePdfDir + "test大docx-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            t1.start();
            t2.start();
            t8.start();
            t3.start();
            t4.start();
            t5.start();
            t6.start();
            t7.start();
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            MyLibreOfficeConverter.getOfficeManager().stop();
        }
    }

    @Test
    public void openFileConvertTest() throws Exception, OfficeException {
        /**
         * 多线程场景测试-libreOffice
         */
        try {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    File resFile = new File("E:\\Users\\WordFiles\\test-docx.docx");
                    File pdfFile = new File(openPdfDir + "testDocx-pdf.pdf");
                    try {
                        MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                }
            });
            Thread t2 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-doc.doc");
                File pdfFile = new File(openPdfDir + "testDoc-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t3 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-ppt.ppt");
                File pdfFile = new File(openPdfDir + "testPpt-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t4 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-pptx.pptx");
                File pdfFile = new File(openPdfDir + "testPptx-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t5 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-xls.xls");
                File pdfFile = new File(openPdfDir + "testXls-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t6 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-xlsx.xlsx");
                File pdfFile = new File(openPdfDir + "testXlsx-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t7 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-txt.txt");
                File pdfFile = new File(openPdfDir + "testTxt-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t8 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-大docx.docx");
                File pdfFile = new File(openPdfDir + "test大docx-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            t1.start();
            t2.start();
            t8.start();
            t3.start();
            t4.start();
            t5.start();
            t6.start();
            t7.start();
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            MyOpenOfficeConverter.getLocalOfficeManager().stop();
        }
    }

    @Test
    public void fileNameTest() {
        String name = "temp.docx";
        System.out.println(name.substring(name.lastIndexOf(".")));
    }

    @Test
    public void fileAvailableTest() throws IOException {
        File inputFile = new File("E:\\Users\\WordFiles\\test-大docx.docx");
        File tempFile = new File("F:\\MyFtpServer\\temp\\temp-channel.docx");
        File tempFile2 = new File("F:\\MyFtpServer\\temp\\temp2-stream.docx");
        File tempFile3 = new File("F:\\MyFtpServer\\temp\\temp3-java7-files.docx");
        File tempFile4 = new File("F:\\MyFtpServer\\temp\\temp4-apache-commonsIO.docx");
//        copyFileUsingFileChannels(inputFile,tempFile);
        copyFileUsingStream(inputFile,tempFile2);
//        copyFileUsingJava7Files(inputFile,tempFile3);
//        copyFileUsingApacheCommonsIO(inputFile,tempFile4);
    }

    private static void copyFileUsingJava7Files(File source, File dest)
            throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    private static void copyFileUsingStream(File source, File dest){
        InputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(dest);
            //生成本地的临时文件
            byte[] fileBuffer = new byte[10240];
            int length = 0;
            while ((length = fis.read(fileBuffer, 0, fileBuffer.length)) != -1) {
//                fos.write(fileBuffer);
                fos.write(fileBuffer,0,length);
            }
        }catch (Exception e){
            try{
                fis.close();
                fos.close();
            }catch (Exception e1){
                e.printStackTrace();
            }

        }
    }

    private static void copyFileUsingApacheCommonsIO(File source, File dest)
            throws IOException {
        FileUtils.copyFile(source, dest);
    }


    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
}
