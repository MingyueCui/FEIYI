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
    <link href="style1.css" rel="stylesheet" type="text/css">
    <style>
        .opt{
            float: left; width:50%;
            text-align: left;
            padding-bottom: 15px;
        }

        .glyphicon:empty{    color: #d0bebe;}
    </style>
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
                <div class="row"><div class="col col-md-12"><h3>错误题目如下:<a href="#"  class="right_container_title"  target="_blank"></a></h3></div></div>
                <div class="row">
                    <div class="col-md-12"  >
                        <script>
                            function GetRequest() {
                                var url = location.search; //获取url中"?"符后的字串
                                var theRequest = new Object();
                                if (url.indexOf("?") != -1) {
                                    var str = url.substr(1);
                                    strs = str.split("&");
                                    for(var i = 0; i < strs.length; i ++) {
                                        theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
                                    }
                                }
                                return theRequest;
                            }
                            var Request = new Object();
                            Request = GetRequest();
                            var wrong;
                            var wrong_answer;
                            var answers=new Array("B","C","B","D","C","B","C","D","B","C");
                            var content=new Array("1、中国有一个传统习俗，农历腊月二十三日或二十四日要过“小年”，通常人们要吃（   ）",
                                    "2、旧俗春节期间大小店铺从大年初一起关门，而在（   ）开市，俗以这一天为财神圣日，认为选择这一天开市必将招财进宝。",
                                    "3、“孩子孩子你别馋，过了腊八就是年：腊八粥你喝几天，哩哩啦啦二十三；二十三，糖瓜粘；二十四，扫尘日；二十五，炖豆腐；二十六，炸羊肉；二十七，杀公鸡；二十八，（    ）；二十九，蒸馒头；三十晚上熬一宿；大年初一去拜年。",
                                    "4、年画是中国画的一种，始于古代的“门神画”，请问下面哪个地区不属于年画的重要产地？",
                                    "5、下面有四句诗，你知道哪一句与春节有关？",
                                    "6、南宋词人辛弃疾《青玉案》中“东风夜放花千树”、“风萧声动，玉壶光转，一夜鱼龙舞”描绘的是（   ）晚上的狂欢情景。",
                                    "7、上海商人（   ）将月历和年画结合起来，选用传统年画或新式风情题材，配上公历纪元和阴历的月份，名为“月份牌年画”，这是年画的新形式。",
                                    "8、在中国最古老的文字甲骨文中，“年”的写法为上面部分为“禾”字，下面部分为“人”字。沉甸甸的“禾”压弯了“人”的腰，寓意丰收后的庆祝。请问你能从下面的甲骨文中，把“年”找出来吗？",
                                    "9、最初的“城隍”不是神，而是指城郊外面的护城壕。“城隍”最早的含义是由水庸（即指沟）衍化而来的，古代人最早信奉的护城沟渠神是“水庸神”，以后逐渐演变为城郊的守护神，即城隍神。上海城隍庙中供奉的城隍老爷是元末明初的士大夫（    ）",
                                    "10、所谓“老八样”是流传在浦东民间的菜谱，包含“八菜、八炒、八分盆” ，常用在红白喜事、重要节日、老人祝寿等场合。下面那个菜品不属于“老八样”之列？"
                            );
                            var c_A=new Array("元宵","大年初三","做花灯","苏州桃花坞","举头望明月，低头思故乡","中秋节","杭穉英","img/8-1.jpg","刘伯温","img/10-1.jpg");
                            var c_B=new Array("饴糖","大年初四","把面发","天津杨柳青","借问酒家何处有，牧童遥指杏花村","元宵节","李慕白","img/8-2.jpg","秦裕伯","img/10-2.jpg");
                            var c_C=new Array("腊八粥 ","大年初五","贴春联","山东潍坊","千门万户瞳瞳日，总把新桃换旧符","七夕节","郑曼陀","img/8-3.jpg","王沂","img/10-3.jpg");
                            var c_D=new Array("饺子","正月十五"," 剃头发","四川成都","独在异乡为异客，每逢佳节倍思亲","除夕","周暮桥","img/8-4.jpg","顾野王","img/10-4.jpg");
                            wrong = Request["wrong"];
                            wrong_answer=Request["wrong_answer"];
                            //alert(wrong);
                            //alert(wrong_answer);
                            var w_q=new Array();
                            var w_a=new Array();
                            w_q = wrong.split(',');
                            w_a=wrong_answer.split(',');

                            for (var i = 0; i < w_q.length; i++) {
                                //alert(content[parseInt(w_q[i])]);
                                if(parseInt(w_q[i])!=7 && parseInt(w_q[i])!=9){
                                    var text='<div class="form-group left-inner-addon" style="color:black"><br>'+content[parseInt(w_q[i])]+'<br><br><div class="opt">A.'
                                            +c_A[parseInt(w_q[i])]+'&nbsp;</div><div class="opt">B.'+c_B[parseInt(w_q[i])]+'&nbsp;</div><div class="opt">C.'
                                            +c_C[parseInt(w_q[i])]+'&nbsp;</div><div class="opt">D.'+c_D[parseInt(w_q[i])]+'&nbsp;</div><br><br><div class="opt">正确答案是:'
                                            +answers[parseInt(w_q[i])]+'</div><div class="opt" style="color:red">您的答案是：'+w_a[i]+'<br></div><div class="opt"style="width:100%"><hr></div></div>';
                                    //alert(text);
                                }
                                else if(parseInt(w_q[i])==7 ){
                                    var text='<div class="form-group left-inner-addon"><br>'+
                                            '8、在中国最古老的文字甲骨文中，“年”的写法为上面部分为“禾”字，下面部分为“人”字。沉甸甸的“禾”压弯了“人”的腰，寓意丰收后的庆祝。'+
                                            '请问你能从下面的甲骨文中，把“年”找出来吗？'+
                                            '  <br><br>'+
                                            '  <div class="opt">A.<img src="img/8-1.jpg" style="width:80px;height:80px"></div>'+
                                            '  <div class="opt">B.<img src="img/8-2.jpg" style="width:80px;height:80px"></div>'+
                                            '  <div class="opt">C.<img src="img/8-3.jpg" style="width:80px;height:80px"></div>'+
                                            '  <div class="opt">D.<img src="img/8-4.jpg" style="width:80px;height:80px"></div><br><br><div class="opt">正确答案是:'+
                                            answers[parseInt(w_q[i])]+'</div><div class="opt" style="color:red">您的答案是：'+w_a[i]+' </div><div class="opt"style="width:100%"><hr></div></div>';
                                }
                                else if(parseInt(w_q[i])==9 ){
                                    var text='<div class="form-group left-inner-addon"><br>'+
                                            '10、所谓“老八样”是流传在浦东民间的菜谱，包含“八菜、八炒、八分盆” ，常用在红白喜事、重要节日、老人祝寿等场合。'+
                                            '下面那个菜品不属于“老八样”之列？'+
                                            '  <br><br>'+
                                            '  <div class="opt">A.<img src="img/10-1.jpg" style="width:80px;height:80px"></div>'+
                                            '  <div class="opt">B.<img src="img/10-2.jpg" style="width:80px;height:80px"></div>'+
                                            '  <div class="opt">C.<img src="img/10-3.jpg" style="width:80px;height:80px"></div>'+
                                            '  <div class="opt">D.<img src="img/10-4.jpg" style="width:80px;height:80px"></div><br><br><div class="opt">正确答案是:'+
                                            answers[parseInt(w_q[i])]+'</div><div class="opt" style="color:red">您的答案是：'+w_a[i]+' </div><div class="opt"style="width:100%"><hr></div></div>';
                                }
                                document.write(text);
                                //document.getElementById("timu").innerHTML = text;
                            }
                            if(w_q.length<4){
                                var text1='<div class="form-group left-inner-addon">'+'<button  class="btn btn-primary float_r"  ><a href="contact.html?openid=${openid}" style="color:white;">填写联系方式</a></button>'
                                        +'	</div>';
                                document.write(text1);
                            }

                        </script>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
</body>

</html>

