<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>  
<%  
    String path = request.getContextPath();  
    String basePath = request.getScheme() + "://"  
            + request.getServerName() + ":" + request.getServerPort()  
            + path + "/";  
%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
    <head>  
        <base href="<%=basePath%>">  
        <title>barcode</title>  
        <meta http-equiv="pragma" content="no-cache">  
        <meta http-equiv="cache-control" content="no-cache">  
        <meta http-equiv="expires" content="0">  
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">  
        <meta http-equiv="description" content="This is my page">  
    </head>  
    <body>  
        <table border="1">  
            <tr>  
                <td>  
                    <h1>  
                       QR_jpg
                    </h1>  
                    <img  
                        src="<%=request.getContextPath()%>/zxingQRTestServlet?msg=1234飒飒阿萨爱心该打&format=jpg&method=show"  
                        height="300px" width=300px />  
                        <a target="_blank" href="<%=request.getContextPath()%>/zxingQRTestServlet?msg=1234飒飒阿萨爱心该打&format=jpg&method=download" >测试图片下载！</a>
                </td>  
                   <td>  
                    <h1>  
                      QR_jpg_image
                    </h1>  
                    <img  
                        src="<%=request.getContextPath()%>/zxingQRTestServlet?msg=1234飒飒阿萨爱心该打&format=jpg&method=show&imageUrl=8023.jpg"  
                        height="300px" width=300px />  
                         <a target="_blank" href="<%=request.getContextPath()%>/zxingQRTestServlet?msg=1234飒飒阿萨爱心该打&format=jpg&method=download&imageUrl=8023.jpg" >测试图片下载！</a>
                </td>
                 <td>  
                    <h1>  
                      Bar_jpg_CODE_128
                    </h1>  
                    <img  
                        src="<%=request.getContextPath()%>/barCodeServlet?msg=12345673&format=CODE_128&type=jpg&mehtod=show"  
                        height="300px" width=300px />  
                         <a target="_blank" href="<%=request.getContextPath()%>/barCodeServlet?msg=12345673&format=CODE_128&type=jpg&method=download" >测试图片下载！</a>
                </td>  
                  <td>  
                    <h1>  
                      Bar_jpg_CODE_128_123456
                    </h1>  
                    <img  
                        src="<%=request.getContextPath()%>/barCodeServlet?msg=12345673&format=CODE_128&type=jpg&mehtod=show&imageStr=123456"  
                        height="300px" width=300px />  
                         <a target="_blank" href="<%=request.getContextPath()%>/barCodeServlet?msg=12345673&format=CODE_128&type=jpg&method=download&imageStr=123456" >测试图片下载！</a>
                </td>
            </tr>  
        </table>  
    </body>  
</html>  