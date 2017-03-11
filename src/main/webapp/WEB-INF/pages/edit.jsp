<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/10/31
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/10/26
  Time: 16:49
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

    <!-- <link rel="stylesheet" type="text/css" href="stylesheets/github-light.css" media="screen"> -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/print.css" media="print">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/simple-calendar.css">
    <script charset="utf-8" src="<%=request.getContextPath()%>/javascripts/kindeditor-all.js"></script>
    <script charset="utf-8" src="<%=request.getContextPath()%>/javascripts/kindeditor-all-min.js"></script>
    <script charset="utf-8" src="<%=request.getContextPath()%>/javascripts/zh_CN.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/javascripts/time.js"></script>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/javascripts/themes/default/default.css" />
    <script src="<%=request.getContextPath()%>/javascripts/jquery.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/weui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/example.css">
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <style>
        h1{padding:5px;margin:25px;font-size: 35px}
        .add_button{

            margin-top: 20px;
            margin-right: 10px;
            position: absolute;
            width: 10%;

        }
        .time select {
            background: transparent;
            width: auto;;
            padding: 5px;
            font-size: 16px;
            border: 1px solid #ccc;
            height: 34px;
            -webkit-appearance: none; /*for chrome*/
        }
        .time  {


        }
        .time{
            width: auto;
            background: transparent;
            position: relative;
            float: left;
            padding: 5px;
            margin: 10px 25px 30px 25px;
            -webkit-appearance: none; /*for chrome*/
        }
    </style>
    <title>修改活动</title>
<body>
<header><h1>修改活动</h1></header>
<form name="news">
    <div name="ac_name" style="padding:5px;margin:25px">活动标题：<input type="text" id="title" name="title" value="${item.title}"></div>
    <div id="start_date" class="time">活动开始日期：
        <select id="start_year"></select>
        <select id="start_Month"></select>
        <select id="start_Day"></select>
    </div>
    <div id="stop_date" class="time">活动结束日期：
        <select id="stop_year"></select>
        <select id="stop_Month"></select>
        <select id="stop_Day"></select>
    </div>
    <div class="page__bd" style="padding:5px;margin:25px;">
        <div class="col-sm-7">

            <textarea name="content" id="content" style="display: block;height: 350px;  width: 100%;">${item.content}</textarea>
            <input type="button" value="修改" onclick="add()" class="button">
            <input type="button" value="删除" onclick="deleteActivity(${item.id})" class="button">
        </div>
    </div>

</form>

</body>
<script>
    $(document).ready(function () {
        $("#start_year").find("option[value="+'${item.fromYear}'+"]").attr("selected",true);
        $("#start_Month").find("option[value="+'${item.fromMonth}'+"]").attr("selected",true);
        $("#start_Day").find("option[value="+'${item.fromDay}'+"]").attr("selected",true);
        $("#stop_year").find("option[value="+'${item.toYear}'+"]").attr("selected",true);
        $("#stop_Month").find("option[value="+'${item.toMonth}'+"]").attr("selected",true);
        $("#stop_Day").find("option[value="+'${item.toDay}'+"]").attr("selected",true);
    })
    KindEditor.ready(function(K) {
        var editor1 = K.create('textarea[name="content"]', {

            uploadJson : '/Activity/fileUpload',
            fileManagerJson : '/Activity/fileManager',
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
        var title=$("#title").val()
        var content=$("#content").val();//正文
        var start_year=$("#start_year").val()
        var start_month=$("#start_Month").val()
        var start_day=$("#start_Day").val()
        var stop_year=$("#stop_year").val()
        var stop_month=$("#start_Month").val()
        var stop_day=$("#stop_Day").val()
        $.ajax({
            url:"<%=request.getContextPath()%>/Activity/edit",
            type:"post",
            data:{
                id:${item.id},
                fromDate:start_year+"-"+start_month+"-"+start_day,
                todate:stop_year+"-"+stop_month+"-"+stop_day,
                title:title,
                content:content
            },
            success:function (data) {
                if(data!="Parameter_Error"){
                    location.href="<%=request.getContextPath()%>/Activity/list"
                }else{
                    alert("修改失败")
                }
            }
        })
    }
    function  deleteActivity(id) {
        $.ajax({
            url:"<%=request.getContextPath()%>/Activity/delete",
            type:"post",
            data:{
                id:id
            },
            success:function (data) {
                location.href="<%=request.getContextPath()%>/Activity/list"
            }
        })
    }
</script>
<script type="text/javascript">
    var selYear1 = window.document.getElementById("start_year");
    var selMonth1 = window.document.getElementById("start_Month");
    var selDay1 = window.document.getElementById("start_Day");
    // 新建一个DateSelector类的实例，将三个select对象传进去
    new DateSelector(selYear1, selMonth1, selDay1, 2016, 10, 25);
    var selYear2 = window.document.getElementById("stop_year");
    var selMonth2 = window.document.getElementById("stop_Month");
    var selDay2 = window.document.getElementById("stop_Day");
    // 新建一个DateSelector类的实例，将三个select对象传进去
    new DateSelector(selYear2, selMonth2, selDay2, 2016, 10, 25);

</script>
</html>

