<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/9 0009
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>MyUploadFileDemo</title>
    <!-- 引入css样式 -->
    <link rel="stylesheet" href="static/js/layui/css/layui.css">
    <!-- 引入layui的javasrcipt的支持 -->
    <script type="text/javascript" src="static/js/layui/layui.js"></script>
    <!-- 引入jquery 的支持 -->
    <script type="text/javascript" src="static/js/jquery/jquery-3.1.1.min.js"></script>
    <!-- 引入mloading 的支持 -->
    <script type="text/javascript" src="static/js/jquery.mloading.js"></script>
    <script type="text/javascript" src="static/js/jquery.mloading.css"></script>

</head>
<body>
<div style="width: 500px;height: auto;margin: 3px 3px 3px 3px;">
    <table class="gridtable" style="width: 490px;">
        <tr>
            <td align="right" width="100px">单图片上传</td>
            <td>
                1、<input type="file" name="file" id="file">
                <input accept="image/gif,image/jpeg,image/jpg,image/png,image/svg" type="button" value="上传图片"
                       onclick="uploadpic()"></input>
            </td>
        </tr>
        <tr>
            <td align="right">预览</td>
            <td>
                <img class="myimg" style="width: 150px;height: 150px;margin: 4px 4px 4px 4px;"/>
            </td>
        </tr>
    </table>
</div>

<div style="width: 500px;height: auto;margin: 3px 3px 3px 3px;" hidden="true">
    <table class="gridtable" style="width: 490px;">
        <tr>
            <td align="right" width="100px">批量图片上传</td>
            <td>
                1、<input accept="image/gif,image/jpeg,image/jpg,image/png,image/svg" type="file" name="files" id="files"
                         style="margin-top: 3px;"><span class="img_info1"></span><br/>
                2、<input accept="image/gif,image/jpeg,image/jpg,image/png,image/svg" type="file" name="files" id="files"
                         style="margin-top: 3px;"><span class="img_info2"></span><br/>
                3、<input accept="image/gif,image/jpeg,image/jpg,image/png,image/svg" type="file" name="files" id="files"
                         style="margin-top: 3px;"><span class="img_info3"></span><br/>
                4、<input accept="image/gif,image/jpeg,image/jpg,image/png,image/svg" type="file" name="files" id="files"
                         style="margin-top: 3px;"><span class="img_info4"></span><br/>
                <input type="button" value="批量上传图片" onclick="batchuploadpic()" style="margin-top: 4px;"></input>
            </td>
        </tr>
        <tr>
            <td align="right">预览</td>
            <td>
                <div>
                    <table class="gridtable">
                        <tr>
                            <td>预览图1</td>
                            <td><img class="myimg1" style="width: 80px;height: 50px;margin: 4px 4px 4px 4px;"/></td>
                            <td>预览图2</td>
                            <td><img class="myimg2" style="width: 80px;height: 50px;margin: 4px 4px 4px 4px;"/></td>
                        </tr>
                        <tr>
                            <td>预览图3</td>
                            <td><img class="myimg3" style="width: 80px;height: 50px;margin: 4px 4px 4px 4px;"/></td>
                            <td>预览图4</td>
                            <td><img class="myimg4" style="width: 80px;height: 50px;margin: 4px 4px 4px 4px;"/></td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
<!-- 引入自定义的js -->
<script type="text/javascript" src="static/js/my.js"></script>
</html>
