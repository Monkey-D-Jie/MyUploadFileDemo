package com.jf.mydemo.mufdTest.convertTest;

import com.jf.myDemo.common.utilities.office.OfficeHomeUtil;
import com.jf.myDemo.convert.converterUtils.MyLibreOfficeConverter;
import com.jf.myDemo.convert.converterUtils.MyOpenOfficeConverter;
import com.jf.myDemo.convert.converterUtils.OfficeSingletonUtil;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.zip.DataFormatException;

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
    public void fileConverTest() throws DataFormatException {
        /**
         * 常规转换测试
         */
        File resFile = new File("E:\\Users\\WordFiles\\test-docx.docx");
//        File pdfFile = new File(libreHtmlDir+"testDocx-pdf.pdf");
        File pdfFile = new File(openPdfDir+"testDocx-pdf.pdf");

        MyOpenOfficeConverter.convertFile(resFile,pdfFile);
//        MyLibreOfficeConverter.convertFile(resFile,pdfFile);
        /*Pdf2htmlEXUtil.pdf2html("D:\\Temp\\pdf2html\\pdf2htmlEX.exe",openHtmlDir+"testDocx-pdf.pdf",libreHtmlDir,"testDocx-pdf.html");*/
    }

    @Test
    public void libreFileConvertTest() throws DataFormatException, OfficeException {
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
                    } catch (DataFormatException e) {
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
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t3 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-ppt.ppt");
                File pdfFile = new File(librePdfDir + "testPpt-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t4 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-pptx.pptx");
                File pdfFile = new File(librePdfDir + "testPptx-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t5 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-xls.xls");
                File pdfFile = new File(librePdfDir + "testXls-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t6 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-xlsx.xlsx");
                File pdfFile = new File(librePdfDir + "testXlsx-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t7 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-txt.txt");
                File pdfFile = new File(librePdfDir + "testTxt-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t8 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-大docx.docx");
                File pdfFile = new File(librePdfDir + "test大docx-pdf.pdf");
                try {
                    MyLibreOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
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
    public void openFileConvertTest() throws DataFormatException, OfficeException {
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
                    } catch (DataFormatException e) {
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
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t3 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-ppt.ppt");
                File pdfFile = new File(openPdfDir + "testPpt-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t4 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-pptx.pptx");
                File pdfFile = new File(openPdfDir + "testPptx-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t5 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-xls.xls");
                File pdfFile = new File(openPdfDir + "testXls-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t6 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-xlsx.xlsx");
                File pdfFile = new File(openPdfDir + "testXlsx-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t7 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-txt.txt");
                File pdfFile = new File(openPdfDir + "testTxt-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            Thread t8 = new Thread(() -> {
                File resFile = new File("E:\\Users\\WordFiles\\test-大docx.docx");
                File pdfFile = new File(openPdfDir + "test大docx-pdf.pdf");
                try {
                    MyOpenOfficeConverter.convertFileThread(resFile, pdfFile);
                } catch (DataFormatException e) {
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
}
