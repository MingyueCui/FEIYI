<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/10/31
  Time: 15:39
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
    <script src="<%=request.getContextPath()%>/javascripts/jquery.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/weui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/example.css">
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <title>活动信息一览</title>
    <style>
        .menu{
            position: relative;
            width: 40%;
            padding: 10px;
            float: left;
            text-align: center;
            color: black;
            top:30%;
        }
        .body{
            background-color: #f8f8f8;
        }
    </style>
</head>

<body>
<div class="menu"><a href="<%=request.getContextPath()%>/Activity/add"><img src="<%=request.getContextPath()%>/images/icon_nav_article.png" width="200px" height="200px"><br>新增活动</a></div>
<div class="menu"><a href="<%=request.getContextPath()%>/Activity/list"><img src="<%=request.getContextPath()%>/images/icon_nav_search.png" width="200px" height="200px"><br>查看活动</a></div>

</body>

</html>
