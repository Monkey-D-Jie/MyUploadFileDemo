package com.jf.myDemo.common.utilities.file;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-06-27 15:10
 * @Description: 文件的操作类
 * To change this template use File | Settings | File and Templates.
 */

public class FileUtils {
    private static int offset = 149;
    private static String enCodeStr = "utf-8";

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

    /**
     * @param 根据文件名从FTP读取输出Byte[]
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
                    InputStream in = null;
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
     * @param filter   过滤器
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
     * @param path 文件路径
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
     * @param fileConEh
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
     * @param end     结束索引
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

    public static void main(String[] agrs) {
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();
        long estimatedTime = endTime - startTime;
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(estimatedTime);


    }
}
