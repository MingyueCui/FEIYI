<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/10/31
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">


    <!-- <link rel="stylesheet" type="text/css" href="stylesheets/github-light.css" media="screen"> -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/print.css" media="print">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/simple-calendar.css">
    <script src="j<%=request.getContextPath()%>/avascripts/jquery.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/weui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/example.css">
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <title>活动信息一览</title>
    <style>
        .weui-cells-new {
            margin-top: 1.17647059em;
            background-color: #FFFFFF;
            line-height: 1.41176471;
            font-size: 17px;
            overflow: hidden;
            position: absolute;
            left: 0px;
            right: 0px;
            overflow-y: scroll;
            overflow-x: auto;
        }
        .title1{
            padding:5px;
            margin-left:10px;
        }
    </style>
</head>

<body>
<header ><h1 class="title1">活动一览</h1><h2><a href="<%=request.getContextPath()%>/Activity/back">回到主页</a></h2></header>
<button onclick="javascript:location.href='<%=request.getContextPath()%>/Activity/add'">添加活动</button>
<div class="result">
    <div class="weui-cells-new "  >
      <c:forEach items="${list}" var="item">
          <a class="weui-cell weui-cell_access" href="<%=request.getContextPath()%>/Activity/edit/${item.id}">
              <div class="weui-cell__bd">
                  <p>${item.title}</p>
              </div>
              <div class="weui-cell__ft">
                  ${item.fromDate}~${item.toDate}
              </div>
          </a>
      </c:forEach>
    </div>
</div>


<script type="text/javascript" src="<%=request.getContextPath()%>/javascripts/simple-calendar.js"></script>
<script>
    var myCalendar = new SimpleCalendar('#calendar');
</script>


</body>

</html>

