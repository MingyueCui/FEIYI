<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2016/10/26
  Time: 9:48
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
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/stylesheet.css" media="screen">
    <!-- <link rel="stylesheet" type="text/css" href="stylesheets/github-light.css" media="screen"> -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/print.css" media="print">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/simple-calendar.css">
    <script src="<%=request.getContextPath()%>/javascripts/jquery.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/weui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/example.css">
    <!--[if lt IE 9]>
    <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <title>月份活动信息一览表</title>
    <style>
        #calendar {
            width: 100%
        }

        .clearfix {
            margin-bottom: 25px !important;
        }

        .body {
            background-color: #f8f8f8;
        }

        p {
            height: 50%;
            margin-bottom: 0;
        }
    </style>
</head>

<body>
<div id="content-wrapper">
    <div class="inner clearfix">
        <div id='calendar'> </div>
    </div>
</div>
<div class="page__bd">
    <div class="weui-search-bar" id="searchBar">
        <div class="weui-search-bar__form">
            <div class="weui-search-bar__box">
                <i class="weui-icon-search"></i>
                <input  class="weui-search-bar__input" id="searchInput" onchange="show()" placeholder="搜索" required="">
            </div>
        </div>
    </div>
</div>
<div class="result">
    <div id="content">

    </div>
    <div class="weui-dialog" id="no_result" style="display:none;">
        <div class="weui-dialog__bd">无相关结果，您可以继续输入或根据预览表查找</div>
        <div class="weui-dialog__ft">
            <a href="javascript:void(0)" class="weui-dialog__btn weui-dialog__btn_primary" id="know">确定</a>
        </div>
    </div>
</div>


<script type="text/javascript" src="<%=request.getContextPath()%>/javascripts/simple-calendar.js"></script>
<script>
    var myCalendar = new SimpleCalendar('#calendar');
    var currentYear = document.getElementById("now_year").value
    var currentMonth = document.getElementById("now_month").value
    $(document).ready(function () {
        var selectYear = $("#now_year").val()
        var selectMonth = $("#now_month").val()
        getList(selectYear,selectMonth)
    })
    $(".sc-item").click(function (e) {
        console.log(e)
        if(e.target.parentNode.classList.contains("sc-othermenth"))
            return false;
        var selectYear = $("#now_year").val()
        var selectMonth = $("#now_month").val()
        var selectDay = e.currentTarget.childNodes[0].innerHTML
        var query_date=selectYear+"-"+selectMonth+"-"+selectDay;
        $.ajax({
            url:"<%=request.getContextPath()%>/Activity/getListByDate",
            type:"get",
            data:{date:query_date},
            success:function (data) {
                if(data!="Parameter_Error"){
                    data=eval(data)
                    var content="";
                    $(data).each(function (index,element) {
                        content+="<a class='weui-cell weui-cell_access' id='"+element.id+"' href='<%=request.getContextPath()%>/Activity/info/"+element.id+"'>"+" <div class='weui-cell__bd'>" +
                                " <p id='title"+element.id+"'>"+element.title+"</p> </div> " +
                                "<div class='weui-cell__ft'>"+element.fromDate+"~"+element.toDate+" </div> </a>"
                    })
                    $("#content").html(content)
                }
            }
        })
    })
    $('.sc-select-year').change(function (e) {
       currentYear = document.getElementById("now_year").value
/*
        var selectMonth = document.getElementById("now_month").value
*/
        getList(currentYear,currentMonth)
    })
    $('.sc-select-month').change(function (e) {
/*
        var selectYear = document.getElementById("now_year").value
*/
        currentMonth = document.getElementById("now_month").value
        getList(currentYear,currentMonth)
    })
    $(".sc-yleft").click(function (e) {
        currentYear--
        getList(currentYear,currentMonth)
    })
    $(".sc-yright").click(function (e) {
        currentYear++
        getList(currentYear,currentMonth)
    })
    $(".sc-mleft").click(function (e) {
        if(currentMonth>1)
            currentMonth--
        else
            currentMonth=12
        getList(currentYear,currentMonth)
    })
    $(".sc-mright").click(function (e) {
        if(currentMonth<12)
            currentMonth++
        else
            currentMonth=1
        getList(currentYear,currentMonth)
    })
    function getList(selectYear,selectMonth) {
        $.ajax({
            url:"<%=request.getContextPath()%>/Activity/getListByMonth",
            type:"get",
            data:{year:selectYear,month:selectMonth},
            success:function (data) {
                $("#content").html("")
                if(data!="Parameter_Error"){
                    data=eval(data)
                    var activity=new Object();
                    $(data).each(function (index,element) {
                        activity[element.fromDate]=element.title
                    })
                    myCalendar._defaultOptions.mark=activity
                    myCalendar.updateMark(selectYear,selectMonth)
                }
            }
        })
    }
    function show() {
        /*var input=$("#searchInput").val()
        var show=document.getElementsByClassName("weui-cell weui-cell_access");
        var count=0
        $(show).each(function (index,element) {
            var id=element.id
            var content=document.getElementById("title"+id).innerHTML
            if(content.indexOf(input)>=0) {
                count++
                element.style.display = ""
            }
            else{
                element.style.display="none";
            }
        })
        console.log(count)
        var no_result=document.getElementById("no_result")
        if(count==0){
            no_result.style.display = ""
        }else {
            no_result.style.display = "none"
        }*/
        var input=$("#searchInput").val()
        $.ajax({
            url:"<%=request.getContextPath()%>/Activity/getListByTitle",
            type:"get",
            data:{title:input},
            success:function (data) {
                if(data!="Parameter_Error"){
                    data=eval(data)
                    var content="";
                    $(data).each(function (index,element) {
                        content+="<a class='weui-cell weui-cell_access' id='"+element.id+"' href='<%=request.getContextPath()%>/Activity/info/"+element.id+"'>"+" <div class='weui-cell__bd'>" +
                                " <p id='title"+element.id+"'>"+element.title+"</p> </div> " +
                                "<div class='weui-cell__ft'>"+element.fromDate+"~"+element.toDate+" </div> </a>"
                    })
                    $("#content").html(content)
                }
            }
        })
        $("#searchInput").val("")
    }
    $("#know").click(function (e) {
        var no_result=document.getElementById("no_result")
        no_result.style.display = "none"
    })
</script>


</body>

</html>
