package com.jf.myDemo.common.utilities.file;

import com.jf.myDemo.convert.converterUtils.MyLibreOfficeConverter;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-27 15:10
 * @Description: 文件的操作类
 * To change this template use File | Settings | File and Templates.
 */

public class MyFileUtils {
    private static int offset = 149;
    private static String enCodeStr = "utf-8";
    private static Logger LOGGER = LoggerFactory.getLogger(MyFileUtils.class.getName());

    /**
     * 获取文件类型
     */
    public static final HashMap<String, String> FILE_TYPE_MAP = new HashMap<>(150);

    static {
        getReverseAllFileType();
    }

    public static void getAllFileType(){
        FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg"); //JPEG (jpg)
        FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png"); //PNG (png)
        FILE_TYPE_MAP.put("47494638396126026f01", "gif"); //GIF (gif)
        FILE_TYPE_MAP.put("49492a00227105008037", "tif"); //TIFF (tif)
        FILE_TYPE_MAP.put("424d228c010000000000", "bmp"); //16色位图(bmp)
        FILE_TYPE_MAP.put("424d8240090000000000", "bmp"); //24位位图(bmp)
        FILE_TYPE_MAP.put("424d8e1b030000000000", "bmp"); //256色位图(bmp)
        FILE_TYPE_MAP.put("41433130313500000000", "dwg"); //CAD (dwg)
        FILE_TYPE_MAP.put("3c21444f435459504520", "html"); //HTML (html)
        FILE_TYPE_MAP.put("3c21646f637479706520", "htm"); //HTM (htm)
        FILE_TYPE_MAP.put("48544d4c207b0d0a0942", "css"); //css
        FILE_TYPE_MAP.put("696b2e71623d696b2e71", "js"); //js
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); //Rich Text Format (rtf)
        FILE_TYPE_MAP.put("38425053000100000000", "psd"); //Photoshop (psd)
        FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml"); //Email [Outlook Express 6] (eml)
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc"); //MS Excel 注意：word、msi 和 excel的文件头一样
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "vsd"); //Visio 绘图
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); //MS Access (mdb)
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
        FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf"); //Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); //rmvb/rm相同
        FILE_TYPE_MAP.put("464c5601050000000900", "flv"); //flv与f4v相同
        FILE_TYPE_MAP.put("00000020667479706d70", "mp4");
        FILE_TYPE_MAP.put("49443303000000002176", "mp3");
        FILE_TYPE_MAP.put("000001ba210001000180", "mpg"); //
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); //wmv与asf相同
        FILE_TYPE_MAP.put("52494646e27807005741", "wav"); //Wave (wav)
        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");
        FILE_TYPE_MAP.put("4d546864000000060001", "mid"); //MIDI (mid)
        FILE_TYPE_MAP.put("504b0304140000000800", "zip");
        FILE_TYPE_MAP.put("526172211a0700cf9073", "rar");
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
        FILE_TYPE_MAP.put("504b03040a0000000000", "jar");
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe");//可执行文件
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");//jsp文件
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf");//MF文件
        FILE_TYPE_MAP.put("3c3f786d6c2076657273", "xml");//xml文件
        FILE_TYPE_MAP.put("494e5345525420494e54", "sql");//xml文件
        FILE_TYPE_MAP.put("7061636b616765207765", "java");//java文件
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat");//bat文件
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz");//gz文件
        FILE_TYPE_MAP.put("6c6f67346a2e726f6f74", "properties");//bat文件
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");//bat文件
        FILE_TYPE_MAP.put("49545346030000006000", "chm");//bat文件
        FILE_TYPE_MAP.put("04000000010000001300", "mxp");//bat文件
        FILE_TYPE_MAP.put("504b0304140006000800", "docx");//docx文件
        //WPS文字wps、表格et、演示dps都是一样的
//        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "wps");
        //ppt
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "ppt");
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");

        FILE_TYPE_MAP.put("3c68746d6c20786d6c6e", "htm");//猎聘、智联简历。htm
        FILE_TYPE_MAP.put("46726f6d3a3cd3c920cd", "mht");//51job简历。mht

        FILE_TYPE_MAP.put("6D6F6F76", "mov"); //Quicktime (mov)
        FILE_TYPE_MAP.put("FF575043", "wpd"); //WordPerfect (wpd)
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); //Outlook Express (dbx)
        FILE_TYPE_MAP.put("2142444E", "pst"); //Outlook (pst)
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); //Quicken (qdf)
        FILE_TYPE_MAP.put("E3828596", "pwl"); //Windows Password (pwl)
        FILE_TYPE_MAP.put("2E7261FD", "ram"); //Real Audio (ram)
    }

    public static void getReverseAllFileType(){
        //反着来
        FILE_TYPE_MAP.put("xls", "D0CF11E0");  //MS Word
        FILE_TYPE_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        FILE_TYPE_MAP.put("ppt", "D0CF11E0");  //ppt

        FILE_TYPE_MAP.put("docx", "504B0304140006000800");  //docx
        FILE_TYPE_MAP.put("xlsx", "504B0304140006000800");  //xlsx
        FILE_TYPE_MAP.put("pptx", "504B0304140006000800");  //pptx

        FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)
        FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)
        FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)
        FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)
        FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)
        FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)
        FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)
        FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        FILE_TYPE_MAP.put("xml", "3C3F786D6C");
        FILE_TYPE_MAP.put("zip", "504B0304140000000800");
        FILE_TYPE_MAP.put("rar", "52617221");
        FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)
        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)


        FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)
        FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)
        FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)
        FILE_TYPE_MAP.put("avi", "41564920");
        FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)
        FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)
        FILE_TYPE_MAP.put("mpg", "000001BA");  //
        FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)
    }
    //通过配置文件读取FTP配置
    //暂写死
    private static String ftp_ip = "192.168.51.13";//服务IP
    private static int ftp_port = 2121;//端口
    private static String ftp_user = "Jacky";//账号
    private static String ftp_password = "123456";//密码
    private static String ftp_upload = "/upload";//上传目录


    /**
     * 把目录下所有文件，上传文件到FTP服务
     *
     * @param hostname 配置主机名称
     * @param path     ftp远程文件路径
     * @throws Exception 抛出的异常
     */
    public static int uploadFTPForDir(String hostname, String ftppath, String path) throws Exception {
        int re = 0;
        File file = new File(path);
        if (!file.isDirectory()) {
            throw new Exception("找不到目录为：" + path);
        }

        FTPClient c = new FTPClient();

        int reply;
        try {
            //链接ftp服务
            c.connect(ftp_ip, ftp_port);
            //登陆
            boolean b = c.login(ftp_user, ftp_password);
            if (!b) {
                throw new Exception("登陆FTP不成功！");
            }
            reply = c.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                c.disconnect();
            }
            c.changeWorkingDirectory(ftppath);
            //上传文件
            File[] f = file.listFiles();
            InputStream input = null;
            int count = 0;

            for (int i = 0; i < f.length; i++) {
                if (!f[i].isFile() || f[i].getName().endsWith(".eh") || f[i].getName().endsWith(".wf") || f[i].getName().endsWith(".txt")) {
                    c.setFileType(FTP.ASCII_FILE_TYPE);
                } else {
                    c.setFileType(FTP.BINARY_FILE_TYPE);
                }
                input = new FileInputStream(f[i]);
                boolean b2 = c.storeFile(encoding(f[i].getName(), enCodeStr, "iso-8859-1"), input);
                input.close();
                count = (b2) ? count + 1 : count;
            }
            c.logout();
            re = f.length - count;
            //判断是否所有文件上传成功
            if (re > 0) {
                throw new Exception("总共" + f.length + "文件，有" + (re) + "文件上传不成功！");
            }
        } catch (IOException e) {
            throw new Exception("未能连接上Ftp服务器！");
        } finally {
            if (c.isConnected()) {
                try {
                    c.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return re;
    }

    /**
     * 文件流生成文件上传到FTP
     *
     * @param file CommonsMultipartFile
     * @param id   生成文件命名
     * @throws Exception 抛出的异常
     */
    public static int uploadFTPForIns(CommonsMultipartFile file, String id) throws Exception {
        int re = 0;
        FTPClient c = new FTPClient();
        int reply;
        try {
            //链接ftp服务
            c.connect(ftp_ip, ftp_port);
            //登陆
            boolean b = c.login(ftp_user, ftp_password);
            if (!b) {
                throw new Exception("登陆FTP不成功！");
            }
            reply = c.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                c.disconnect();
            }
            c.changeWorkingDirectory(ftp_upload);

            if (file.getOriginalFilename().endsWith(".eh") || file.getOriginalFilename().endsWith(".wf") || file.getOriginalFilename().endsWith(".txt")) {
                c.setFileType(FTP.ASCII_FILE_TYPE);
            } else {
                c.setFileType(FTP.BINARY_FILE_TYPE);
            }
            InputStream input = file.getInputStream();
            boolean b2 = c.storeFile(encoding(id, enCodeStr, "iso-8859-1"), input);
            input.close();
            c.logout();
        } catch (IOException e) {
            throw new Exception("未能连接上Ftp服务器！");
        } finally {
            if (c.isConnected()) {
                try {
                    c.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return re;
    }

    public static int uploadFTPForInsV2(File file, String id) throws Exception {
        int re = 0;
        FTPClient c = new FTPClient();
        int reply;
        try {
            //链接ftp服务
            c.connect(ftp_ip, ftp_port);
            //登陆
            boolean b = c.login(ftp_user, ftp_password);
            if (!b) {
                throw new Exception("登陆FTP不成功！");
            }
            reply = c.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                c.disconnect();
            }
            c.changeWorkingDirectory(ftp_upload);

            if (file.getName().endsWith(".eh") || file.getName().endsWith(".wf") || file.getName().endsWith(".txt")) {
                c.setFileType(FTP.ASCII_FILE_TYPE);
            } else {
                c.setFileType(FTP.BINARY_FILE_TYPE);
            }
            FileInputStream fis = new FileInputStream(file);
            InputStream input = new InputStream() {
                @Override
                public int read() throws IOException {
                    return fis.read();
                }
            };
            boolean b2 = c.storeFile(encoding(id, enCodeStr, "iso-8859-1"), input);
            input.close();
            c.logout();
        } catch (IOException e) {
            throw new Exception("未能连接上Ftp服务器！");
        } finally {
            if (c.isConnected()) {
                try {
                    c.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return re;
    }

    /**
     */
    public static byte[] getFileByte(String fileid) throws Exception {

        byte[] bytes = null;
        FTPClient c = new FTPClient();
        int reply;
        try {
            //链接ftp服务
            c.connect(ftp_ip, ftp_port);
            //登陆
            boolean b = c.login(ftp_user, ftp_password);
            if (!b) {
                throw new Exception("登陆FTP不成功！");
            }
            reply = c.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                c.disconnect();
            }
            c.changeWorkingDirectory(ftp_upload);
            //windows环境下需设置UTF-8编码
            c.setControlEncoding(enCodeStr);
            FTPFile[] fs = c.listFiles();
            for (FTPFile f : fs) {
                if (!f.isFile()) {
                    continue;
                }
                if (f.getName().equals(fileid)) {
                    InputStream in;
                    c.setBufferSize(1024);
                    c.setFileType(FTPClient.BINARY_FILE_TYPE);
                    fileid = new String(fileid.getBytes("UTF-8"), "ISO-8859-1");
                    in = c.retrieveFileStream(fileid);
                    bytes = input2byte(in);
                    in.close();
                }
            }
            c.logout();
        } catch (IOException e) {
            throw new Exception("未能连接上Ftp服务器！");
        } finally {
            if (c.isConnected()) {
                try {
                    c.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

        }
        return bytes;
    }

    public static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();

        swapStream.close();

        return in2b;
    }

    /**
     * 下载文件
     *
     * @param hostname 主机
     * @param ftppath  远程路径
     * @return 返回文件名称
     */
    public static String[] getFTPFileForDir(String hostname, String ftppath, String loaclpath) throws Exception {
        String[] fileName = new String[0];

        FTPClient c = new FTPClient();
        int reply;
        try {
            //链接ftp服务
            c.connect(ftp_ip, ftp_port);
            //登陆
            boolean b = c.login(ftp_user, ftp_password);
            if (!b) {
                throw new Exception("登陆FTP不成功！");
            }
            reply = c.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                c.disconnect();
            }
            c.changeWorkingDirectory(ftppath);
            //windows环境下需设置UTF-8编码
            c.setControlEncoding(enCodeStr);
            FTPFile[] fs = c.listFiles();
            createDir(loaclpath);
            for (FTPFile f : fs) {
                if (!f.isFile()) {
                    continue;
                }
                File f_loc = new File(loaclpath + f.getName());
                OutputStream os = new FileOutputStream(f_loc);
                c.retrieveFile(encoding(f.getName(), enCodeStr, "iso-8859-1"), os);
                os.close();
            }
            c.logout();
        } catch (IOException e) {
            throw new Exception("未能连接上Ftp服务器！");
        } finally {
            if (c.isConnected()) {
                try {
                    c.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

        }
        return fileName;
    }


    /**
     * 读取FTP文件
     *
     * @return 返回文件内容
     */
    public static String readFTPFile(FTPClient c, String fileName) {
        String fileCon = "";
        InputStream is = null;
        try {
            is = c.retrieveFileStream(fileName);
            if (is == null) {
                return null;
            }
            int len = is.available();
            if (len > 0) {
                byte[] b = new byte[len];
                is.read(b);
                fileCon = new String(b, "GBK");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileCon;
    }

    /**
     * 重命名FTP文件
     *
     * @param hostname 主机
     * @param ftppath  远程路径
     * @param ftppath  远程路径bak
     * @param fileList 需要重命名的文件
     * @return 返回文件名称
     * @throws Exception
     */
    public static void reNameFTPFile(String hostname, String ftppath, String ftppath_bak, List<String> fileList) throws Exception {
        FTPClient c = new FTPClient();
        int reply;
        try {
            //链接ftp服务
            c.connect(ftp_ip, ftp_port);
            //登陆
            boolean b = c.login(ftp_user, ftp_password);
            if (!b) {
                throw new Exception("登陆FTP不成功！");
            }
            reply = c.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                c.disconnect();
            }
            c.changeWorkingDirectory(ftppath);
            boolean isC = createFtpDir(c, ftppath_bak);
            if (!isC) {
                throw new Exception("备份文件夹[" + ftppath_bak + "]创建失败！");
            }
            //把文件移入备份文件夹
            for (String fileName : fileList) {
                boolean re_b = c.rename(encoding(fileName, enCodeStr, "iso-8859-1"), encoding(ftppath_bak + fileName, enCodeStr, "iso-8859-1"));
                System.out.println(re_b);
            }
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (c.isConnected()) {
                try {
                    c.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    private static boolean createFtpDir(FTPClient c, String dir) throws IOException {
        String _dir = c.printWorkingDirectory();
        if (c.changeWorkingDirectory(dir)) {
            c.changeWorkingDirectory(_dir);
            return true;
        } else {
            boolean b = c.makeDirectory(dir);
            c.changeWorkingDirectory(_dir);
            return b;
        }
    }

    /**
     * 写文件
     *
     * @param fileCon 文件内容
     * @param path    写入文件的路径
     */
    public static void writhFile(String fileCon, String path) {
        //创建路径
        createDir(path);
        //写文件
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            if (!f.isFile()) {
                f.createNewFile();
            }
            fos = new FileOutputStream(f);
            byte[] b = fileCon.getBytes("GBK");
            fos.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param fileCon
     * @param path
     * @param encode  按编码写入文件
     */
    public static void writhFile(String fileCon, String path, String encode) {

        if (encode != null) {

            //创建路径
            createDir(path);
            //写文件
            File f = new File(path);
            FileOutputStream fos = null;
            try {
                if (!f.isFile()) {
                    f.createNewFile();
                }
                fos = new FileOutputStream(f);
                byte[] b = fileCon.getBytes(encode);
                fos.write(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            writhFile(fileCon, path);
        }

    }

    /**
     * 创建路径，存在时不创建，不存在时创建
     *
     * @param path 路径
     */
    private static void createDir(String path) {
        //检验路径是否存在
        String _path = "";
        if (path.endsWith("/")) {
            _path = path;
        } else {
            String[] paths = path.split("/");

            for (int i = 0; i < paths.length - 1; i++) {
                if (paths[i].length() > 0) {
                    _path = _path + paths[i] + "/";
                }
            }
        }
        File dir = new File(_path);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建路径，存在时不创建，不存在时创建
     *
     * @param path 路径
     */
    public static boolean delDir(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean b = delDir(path + children[i]);
                if (!b) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 获取时间的微秒值，字符串（yyyyMMddHHmmssSSSSSS）
     *
     * @return
     */
    public static String getMicroTime() {
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date d = new Date();
        String str = f.format(d);
        String sss = str.substring(14);
        int micro = Integer.parseInt(sss) / 2 + offset;
        offset = offset + 3;
        if (offset > 500) {
            offset = 149;
        }
        return str + "" + micro;
    }


    /**
     * 解析WF文件内容
     *
     * @return
     */
    public static String[] parseWfFileContent(String fileConWf, String bdName, String[] key) {
        String[] value = new String[key.length];
        String bdBegin = "<" + bdName + ">";
        String bdEnd = "</" + bdName + ">";
        int beginIndex = 0;
        int endIndex = 0;
        //数据头
        beginIndex = fileConWf.indexOf(bdBegin);
        endIndex = fileConWf.indexOf(bdEnd);
        //数据体
        String dataStr = fileConWf.substring(beginIndex + bdBegin.length() + 1, endIndex);
        //String[] dataStrs = dataStr.trim().split("[|\\n|\\r]");
        for (int i = 0; i < key.length; i++) {
            value[i] = getValue(dataStr, key[i]);
        }
        return value;
    }

    /**
     * 获取字符串的<key value/>的value
     *
     * @param context
     * @param key     不包括key
     * @return
     */
    private static String getValue(String context, String key) {
        String re = "";
        int beginIndex = context.indexOf(key);
        if (beginIndex >= 0) {
            String temp = context.substring(beginIndex);
            String[] temps = temp.split("[\\n|\\r]+");
            if (temps.length > 0) {
                re = temps[0].trim().substring(key.length() + 1);
            }
        }
        return re.replace("'", "");//去除时间带的点
    }

    /**
     * 编码转换
     *
     * @param obj
     * @param getCode
     * @param setCode
     * @return
     */
    public static String encoding(Object obj, String getCode, String setCode) {
        try {
            if (obj == null)
                return "";
            else
                return new String(obj.toString().getBytes(getCode), setCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取文件类型
     * ps:流会关闭
     *正着来 由文件头编码获取到文件类型
     * @param inputStream
     * @return
     */
    public static String getFileType(InputStream inputStream) {
        return FILE_TYPE_MAP.get(getFileHeader(inputStream));
    }

    /**
     * 获取文件的真实类型
     *反着来：先获得文件类型，然后查看对应文件头信息是否正确
     * @param inputStream
     * @return
     */
    public static String getFileRealSuffixType(InputStream inputStream) {
        return getFileSuffixType(inputStream);
    }

    public static String getFileHeader(InputStream inputStream) {
        String value = null;
        try {
            byte[] b = new byte[10];
        /*int read() 从此输入流中读取一个数据字节。
        *int read(byte[] b) 从此输入流中将最多 b.length 个字节的数据读入一个 byte 数组中。
        * int read(byte[] b, int off, int len) 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
        */
            inputStream.read(b, 0, b.length);
            value = bytesToHexString(b).toLowerCase();
        } catch (Exception e) {
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        LOGGER.info("***********************上传文件的文件头为："+value);
        return value;
    }

    public static String getFileSuffixType(InputStream inputStream) {
        String value = null;
        try {
            byte[] b = new byte[10];
        /*int read() 从此输入流中读取一个数据字节。
        *int read(byte[] b) 从此输入流中将最多 b.length 个字节的数据读入一个 byte 数组中。
        * int read(byte[] b, int off, int len) 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
        */
            inputStream.read(b, 0, b.length);
            value = getFileTypeByStream(b);
        } catch (Exception e) {
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        LOGGER.info("***********************上传文件的文件真实类型为："+value);
        return value;
    }

    /**
     * 将要读取文件头信息的文件的byte数组转换成string类型表示
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    /**
     * Created on 2010-7-2
     * <p>Discription:[isImage,判断文件是否为图片]</p>
     * @param file
     * @return true 是 | false 否
     * @author:[shixing_11@sina.com]
     */
    public static final boolean isImage(File file){
        boolean flag = false;
        try
        {
            BufferedImage bufreader = ImageIO.read(file);
            int width = bufreader.getWidth();
            int height = bufreader.getHeight();
            if(width==0 || height==0){
                flag = false;
            }else {
                flag = true;
            }
        }
        catch (IOException e)
        {
            flag = false;
        }catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * Created on 2010-7-1
     * <p>Discription:[getFileTypeByStream]</p>
     * @param b
     * @return fileType
     * @author:[shixing_11@sina.com]
     */
    public final static String getFileTypeByStream(byte[] b)
    {
        String filetypeHex = String.valueOf(bytesToHexString(b));
        Iterator<Map.Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
        while (entryiterator.hasNext()) {
            Map.Entry<String,String> entry =  entryiterator.next();
            String fileTypeHexValue = entry.getValue();
            if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String getMimeType(String fileUrl) throws java.io.IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileUrl);
        return type;
    }

    public static synchronized File getConvertFile(String fileName,File inputFile,File outPutFile){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File tempFile = null;
        if(inputFile != null){
            try{
                    tempFile = new File("F:\\MyFtpServer\\temp\\tempFile"+fileName.substring(fileName.indexOf(".")));
                    if(!outPutFile.exists()){
                        outPutFile.createNewFile();
                    }
                    tempFile.createNewFile();
                    MyCopyFileUtils.copyFileUsingFileChannels(inputFile,tempFile);
                    LOGGER.info("本地临时文件已生成，临时文件所在目录---->>>"+tempFile.getPath()+"");
                    //转换该文件
                    System.out.println("*************转换后文件的大小为:"+tempFile.length());
                    MyLibreOfficeConverter.convertFile(tempFile,outPutFile);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } finally{
                try{
                    if(fis != null && fos != null){
                        fis.close();
                        fos.close();
                    }
                    if(tempFile != null){
                        //删除掉临时文件
//                        tempFile.delete();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }else{
            LOGGER.info("***********************传入的文件不合法！");
        }
        return outPutFile;
    }

    public static void main(String[] agrs) {
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();
        long estimatedTime = endTime - startTime;
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(estimatedTime);
    }
}
