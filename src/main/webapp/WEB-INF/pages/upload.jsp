<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/10/26
  Time: 12:17
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
    <script charset="utf-8" src="/javascripts/kindeditor-all.js"></script>
    <script charset="utf-8" src="/javascripts/kindeditor-all-min.js"></script>
    <script charset="utf-8" src="/javascripts/zh_CN.js"></script>
    <link rel="stylesheet" href="/javascripts/themes/default/default.css" />
    <script src="/javascripts/jquery.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/stylesheets/weui.css">
    <link rel="stylesheet" type="text/css" href="/stylesheets/example.css">
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <title>九九重阳</title>
<body>
<div class="page article js_show">
    <div class="page__bd">
        <div class="col-sm-7">
            <form name="news">
                <textarea name="content" id="content" style="width:550px;height:200px;"></textarea>
            </form>
            <input type="button" value="add" onclick="add()">
        </div>
    </div>

</div>
</body>
<script>
    KindEditor.ready(function(K) {
        var editor1 = K.create('textarea[name="content"]', {

            uploadJson : '<%=request.getContextPath()%>/Activity/fileUpload',
            fileManagerJson : '<%=request.getContextPath()%>/Activity/fileManager',
            allowFileManager : true,
            afterCreate : function() {
                var self = this;
                K.ctrl(document, 13, function() {
                    self.sync();
                    document.forms['news'].submit();
                });
                K.ctrl(self.edit.doc, 13, function() {
                    self.sync();
                    document.forms['news'].submit();
                });
            },
            afterBlur:function(){
                this.sync();
            }

        });
        // prettyPrint();
    });
    function add() {
        var content=$("#content").val();//正文
        console.log(content)
    }
</script>
</html>
