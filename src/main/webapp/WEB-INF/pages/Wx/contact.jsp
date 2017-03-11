<%--
  Created by IntelliJ IDEA.
  User: 11369
  Date: 2017/1/21
  Time: 9:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <title>民俗知识趣味问答之春节篇</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="style_2.css" rel="stylesheet" type="text/css">
    <style>
        .opt{
            float: left; width:47%;
            text-align: left;
            padding-bottom: 7px;
        }
        hr{

        }
        .glyphicon:empty{    color: #d0bebe;}
    </style>
    <script>

        function change()
        {
            //alert("!!");
            var pw1=0;
            var pw2=0;
            var pw3=0;
            pw1=document.getElementById("input_name").value;
            pw2=document.getElementById("input_tel").value;
            pw3=document.getElementById("input_address").value;
            if(pw1!=0 && pw2!=0 && pw3!=0){
                //alert(pw2);
                window.location.href="warning.html";
            }
            else{
                alert("请填写完整的联系方式");
            }
        }
    </script>
</head>
<body class="templatemo_garden_bg" style="font-size:18px;">

<div id="main_container">
    <div class="container" id="contact">
        <div class="row col-wrap">
            <div id="left_container" class="col col-md-3 col-sm-12">
                <div class="templatemo_logo">
                    <img src="img/title1.png" style="width:80%;">
                </div>
            </div>
            <div id="right_container" class="col col-md-9 col-sm-12" style="color:black">
                <div class="row"><div class="col col-md-12"><h3>恭喜<a href="#"  class="right_container_title"  target="_blank"></a></h3></div></div>
                <div class="row">
                    <div class="col-md-12" >
                        <p style="text-align:left;color:black;"> 上海非遗祝您新春快乐！请选择奖品的领取方式:(建议截图保留)</p>
                        <div class="opt"><input type="radio" id="ziqu" name="way" value="ziqu" onclick="change_ziqu()"><label for="ziqu">自取&nbsp;</div>
                        <div class="opt"><input type="radio" name="way" value="B" id="youji" onclick="change_youji()"><label for="youji">邮寄&nbsp;</div>
                    </div>
                </div>

                <form role="form" action="<%=request.getContextPath()%>/Wx/submitResult" method="post" name="myForm" accept-charset="utf-8">
                <div class="row" name="mail_to" id="mail_to" style="display:none">
                    <div style="display: none">
                        <input type="text" name="openid" value="${openid}">
                        <input type="text" name="validity" value="1"><!-- 全对-->
                        <input type="text" name="getPrize" id="getPrize" value=""><!--邮寄 -->
                    </div>
                    <div class="col-md-5">
                        <div class="form-group left-inner-addon">
                            <span class="glyphicon glyphicon-user"></span>
                            <input name="name" type="text" class="form-control" id="input_name" placeholder="姓名">
                        </div>
                        <div class="form-group left-inner-addon">
                            <span class="glyphicon glyphicon-earphone"></span>
                            <input name="tel" type="text" class="form-control" id="input_tel" placeholder="联系方式">
                        </div>
                        <div class="form-group left-inner-addon">
                            <span class="glyphicon glyphicon-home"></span>
                            <input name="address" type="text" class="form-control" id="input_address" placeholder="邮寄地址">
                        </div>

                    </div>
                    <button  class="btn btn-primary float_r" name="b1" id="b1" onclick="submitForm(0)">完成</button>

                </div>
                <div class="row" name="zi_qu" id="zi_qu" style="display:none">
                    <div class="col-md-5" >
                        <div class="form-group left-inner-addon">
                            <hr>
                            自取地址是：徐汇区古宜路125号群众艺术馆文化遗产办公室2楼主题图书馆<hr>
                        </div>
                        <button class="btn btn-primary float_r" name="b2" id="b2" onclick="submitForm(1)">完成</button>

                    </div>


                </div>
                </form>
                <!-- row -->

                <script>
                    function change_youji() {
                        var way1 =document.getElementById("mail_to");
                        way1.style.display="block";
                        var way2 =document.getElementById("zi_qu");
                        way2.style.display="none";

                    }
                    function change_ziqu() {
                        var way1 =document.getElementById("zi_qu");
                        way1.style.display="block";
                        var way2 =document.getElementById("mail_to");
                        way2.style.display="none";

                    }
                    function submitForm(getPrize) {
                        var input_get = document.getElementById("getPrize")
                        input_get.value = getPrize//0 邮寄 1 自取
                        var myForm=document.getElementsByName("myForm")[0];
                        myForm.submit();
                    }
                </script>
            </div>
        </div>


    </div>
</div>
</body>

</html>

