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
    <link rel="stylesheet" href="static/js/layui/css/layui.css" />
    <!-- 引入mLoading的 css 样式 ----》就是因为没有把这个引入，所以没有动画效果-->
    <link rel="stylesheet" href="static/js/jquery.mloading.css" />

    <!-- 引入jquery 的支持 -->
    <script type="text/javascript" src="static/js/jquery/jquery-3.1.1.min.js"></script>
    <!-- 引入mloading 的支持 -->
    <script type="text/javascript" src="static/js/jquery.mloading.js"></script>
    <!-- 引入layui的javasrcipt的支持 -->
   <script type="text/javascript" src="static/js/layui/layui.all.js"></script>
</head>
<body>
<div style="width: 500px;height: auto;margin: 3px 3px 3px 3px;">
    <table class="gridtable" style="width: 490px;">
        <tr>
            <td align="right" width="100px">单文件上传</td>
            <td>
                1、<input type="file"  multiple="multiple" name="file" id="file">
                <input  type="button" value="上传文件"
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
<div id="log-container" style="height: 100%; overflow-y: scroll; background: #333; color: #aaa; padding: 10px;">
    <div>
        <div class="layui-progress layui-progress-big" lay-showPercent="yes">
            <div class="layui-progress-bar layui-bg-blue" lay-percent="50%"></div>
        </div>
    </div>
</div>
<div style="width: 500px;height: auto;margin: 3px 3px 3px 3px;">
    <table class="gridtable" style="width: 490px;">
        <tr>
            <td align="right" width="100px">批量文件上传</td>
            <td>
                <%--accept="image/gif,image/jpeg,image/jpg,image/png,image/svg"--%>
                1、<input  type="file" name="files" id="files"
                         style="margin-top: 3px;"><span class="img_info1"></span><br/>
                2、<input  type="file" name="files" id="files"
                         style="margin-top: 3px;"><span class="img_info2"></span><br/>
                3、<input  type="file" name="files" id="files"
                         style="margin-top: 3px;"><span class="img_info3"></span><br/>
                4、<input  type="file" name="files" id="files"
                         style="margin-top: 3px;"><span class="img_info4"></span><br/>
                <input type="button" value="批量上传文件" onclick="batchuploadpic()" style="margin-top: 4px;"></input>
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
    </table><br/>
    上传进度：<progress></progress><br/>
    <p id="progress">0 bytes</p>
    <p id="info"></p>
</div>
</body>
<!-- 引入自定义的js -->
<script type="text/javascript" src="static/js/my.js"></script>
<script>
    /*全局配置layui*/
    layui.config({
        dir: 'static/js/layui/', //layui.js 所在路径（注意，如果是script单独引入layui.js，无需设定该参数。），一般情况下可以无视
        version: false, //一般用于更新组件缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
        debug: false, //用于开启调试模式，默认false，如果设为true，则JS模块的节点会保留在页面
        base: '' //设定扩展的Layui组件的所在目录，一般用于外部组件扩展
    });
//    这里用来作webSocket接收数据的测试
    $(document).ready(function() {
        // 指定websocket路径
        var websocket = new WebSocket('ws://localhost:8080/ws');
        websocket.onmessage = function(event) {
            // 接收服务端的实时日志并添加到HTML页面中
            $("#log-container div").append(event.data + "<p> </p>");
            // 滚动条滚动到最低部
            $("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
            layer.open({
                title: '文件上传结果',
                content: '亲，文件已经成功上传咯喔 (*^▽^*)',
                offset: 'rb',
                icon: 1,
                btn: '我知道了',
                btnAlign: 'c',
                shade :0,
                time: 5000,
                anim: 2,
                closeBtn: 0
            });

        };
    });
</script>
</html>

