<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/10/26
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Architects+Daughter' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" href="/stylesheets/stylesheet.css" media="screen">
    <!-- <link rel="stylesheet" type="text/css" href="stylesheets/github-light.css" media="screen"> -->
    <link rel="stylesheet" type="text/css" href="/stylesheets/print.css" media="print">
    <link rel="stylesheet" type="text/css" href="/stylesheets/simple-calendar.css">
    <script src="/javascripts/jquery.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/stylesheets/weui.css">
    <link rel="stylesheet" type="text/css" href="/stylesheets/example.css">
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <title>${object.title}</title>
<body>
<div class="page article js_show">
    <div >
        <a href="javascript:history.go(-1)" style="padding: 20px 15px;
  font-size: 15px;">返回前页</a>
    </div>
   <%-- <div class="page__hd">
        <h1 class="page__title">${object.title}</h1>
        <p class="page__desc">开始时间:${object.fromDate}<br>结束时间:${object.toDate}</p>
    </div>--%>
    <div class="page__bd">
        <article class="weui-article">
            <h1>${object.title}</h1>
            <section>
                ${object.content}
            </section>
        </article>
    </div>
<%--    <div >
        <a href="javascript:history.go(-1)" style="padding: 20px 15px;
  font-size: 15px;">返回前页</a>
    </div>--%>
</div>
</body>
</html>

