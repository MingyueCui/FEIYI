<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2017/2/22
  Time: 22:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>活动预约</title>
    <link href="<%=request.getContextPath()%>/Wx/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/Wx/css/theme.css" rel="stylesheet" type="text/css">

</head>
<body  style="font-size:18px;">
<div class="container" id="contact">
    <div class="box1"></div>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">${activity.title}预约</h3>
        </div>
        <div class="panel-body">
            <div class="row color2" name="label" id="label" style="display:${bookInfo==null?'block':'none'}">
                <div class="col-sm-12">
                    <div class="form-group left-inner-addon">
                        <span class="glyphicon glyphicon-user"></span>
                        <input name="name" type="text" class="form-control" id="input_name" placeholder="姓名">
                    </div>
                    <div class="form-group left-inner-addon">
                        <span class="glyphicon glyphicon-earphone"></span>
                        <input name="tel" type="text" class="form-control" id="input_tel" placeholder="联系方式">
                    </div>
                    <button  class="btn btn-primary " name="b1" id="b1" onclick="check()" >完成</button>
                </div>
            </div>
            <span style="display:${bookInfo==null?'none':'block'}" id="end" >	您已成功预约，我们会尽快和您联系。</span>
        </div>
    </div>

</div>
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascripts/jquery.js"></script>
<script>
    function check()
    {
        var input_name=document.getElementById("input_name").value;
        var input_tel=document.getElementById("input_tel").value;
        if(input_name!=0 && input_tel!=0){
            if((/^1\d{10}$/.test(input_tel))){
                $.ajax({
                    url:"<%=request.getContextPath()%>/Wx/bookActivity",
                    type:"post",
                    data:{openid:'${openid}',aid:${activity.id},username:input_name,phoneNum:input_tel},
                    success:function (data) {
                        if(data == "success") {
                            alert("预约成功");
                            var way1 =document.getElementById("end");
                            way1.style.display="block";
                            var way2 =document.getElementById("label");
                            way2.style.display="none";
                        }else{
                            alert("预约失败");
                        }
                    }
                })
            }
            else{
                alert("请填写正确的联系方式");
            }
        }
        else{
            alert("请填写完整的联系方式");
        }
    }
</script>
</html>

