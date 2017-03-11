<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/11/24
  Time: 22:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script charset="utf-8" src="script/kindeditor-min.js"></script>
    <script charset="utf-8" src="lang/zh_CN.js"></script>
    <script type="text/javascript" src="script/jquery.min.js"></script>
    <script type="text/javascript" src="script/bootstrap.min.js"></script>
    <link rel="stylesheet" href="themes/default/default.css" />
    <link href="style/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="style/bootstrap.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="style/site.css">
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>滚动新闻</title>
    <script>
        KindEditor.ready(function(K) {
            K.create('textarea[name="content"]', {
                autoHeightMode : true,
                afterCreate : function() {
                    this.loadPlugin('autoheight');
                }
            });
        });
    </script>
</head>

<body>
<jsp:include page="head.jsp"></jsp:include>
<div class="left vertical-nav">
    <ul>
        <li class="home">
            <a href="#">首页</a>
        </li>
        <li >
            <a href="memberManage.html">二级用户管理</a>
        </li>
        <li >
            <a href="rollNews.html">发布滚动新闻</a>
        </li>
        <li class="active">
            <a href="/back/activityList.html">查看滚动新闻</a>
        </li>
        <li >
            <a href="noticeManage.html">发布公告</a>
        </li>
        <li>
            <a href="noticeStats.html">查看公告</a>
        </li>
        <li>
            <a href="fileManage.html">上传文件</a>
        </li>
        <li >
            <a href="fileStats.html">文件管理</a>
        </li>
        <li >
            <a href="setAdmin.html">设置管理员</a>
        </li>
    </ul>
</div>
<div class="right">
    <div class="container news-container">
        <div class="row">
            <div class="col-md-12">
                <div id="myTabContent" class="tab-content news-content">
                    <div class="tab-pane fade in active" id="posted">
                        <div class="row tool-row shadow">
                            <div class="col-md-12">
                                <form class="form-horizontal" role="form">
                                    <div class="col-md-4 col-md-offset-1">
                                        <div class="form-group">
                                            <div class="col-sm-5">
                                                <label for="title" class="control-label">标题：</label>
                                            </div>
                                            <div class="col-sm-7">
                                                <input class="form-control" id="title">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-1 col-md-offset-1">
                                        <button class="btn btn-default">查询</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="row newsList">
                            <div class="col-md-12">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>标题</th>
                                        <th>发布时间</th>
                                        <th>开始时间</th>
                                        <th>结束时间</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                   <c:forEach items="${list}" var="item">
                                       <tr class="black">
                                           <td>${item.title}</td>
                                           <td class="grey">${item.createDate}</td>
                                           <td>${item.fromDate}</td>
                                           <td>${item.toDate}</td>
                                           <td>
                                               <a class="form-btn" data-toggle="modal" data-target="#preview">查看</a>
                                               <a class="form-btn" href="rollNews.html">编辑</a>
                                               <a class="form-btn" href="rollNews.html">删除</a>
                                       </tr>
                                   </c:forEach>
                                    </tbody>
                                </table>
                                <div class="page">
                                    <span class="page-show">1/5</span>
                                    <input type="submit"  value="" class="next-btn">
                                    <input class="page-input">
                                    <button type="submit" class="page-btn">跳转</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="delete" tabindex="-1" role="dialog"
                         aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog" style="width:400px;">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal"
                                            aria-hidden="true">×
                                    </button>
                                </div>
                                <div class="modal-body">
                                    下架成功！
                                </div>
                                <div class="modal-footer">
                                    <div class="row">
                                        <div class="col-sm-4 col-sm-offset-4">
                                            <button type="button" class="btn btn-default btn-width"
                                                    data-dismiss="modal">
                                                确定
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="success" tabindex="-1" role="dialog"
                         aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal"
                                            aria-hidden="true">×
                                    </button>
                                </div>
                                <div class="modal-body modal-body1">
                                    发布成功！
                                </div>
                                <div class="modal-footer">
                                    <div class="row">
                                        <div class="col-sm-4 col-sm-offset-4">
                                            <button type="button" class="btn btn-default btn-width"
                                                    data-dismiss="modal">
                                                确定
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</body>
<script type="text/javascript">
    function open_draft(){
        $('#myTab a[href="#drafts"]').tab('show')
    }
    function open_edit(){
        $('#myTab a[href="#new"]').tab('show')
    }
</script>
</html>
