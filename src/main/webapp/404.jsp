<%--
  Created by IntelliJ IDEA.
  User: dong
  Date: 2015/2/3
  Time: 18:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>404报错页面</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/notfound.css">
</head>

<body>
<div class="maincontent">
  <div class="nofound_info">
    <i></i>
    <span>&nbsp;&nbsp;呀，你访问的页面不在地球<br/>&nbsp;&nbsp;看看其他的吧！</span>
    <div class="clear"></div>
    <%--<a href="" target="_blank">返回首页</a>--%>
  </div>
</div>
<div class="bottom_info"></div>
</body>
</html>
