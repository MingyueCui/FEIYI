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
            float: left; width:47%;
            text-align: left;
            padding-bottom: 7px;
        }



    </style>

</head>
<body class="templatemo_garden_bg" style="font-size:18px;">
<script>
    //alert("!");
    var answers=new Array("B","C","B","D","C","B","C","D","B","C");
    var num=1;
    var u_answers=new Array("0","0","0","0","0","0","0","0","0","0");
    var right=new Array();
    var wrong=new Array();
    var wrong_answer=new Array();
    function change()
    {
        for (var i = 0; i < answers.length; i++) {
            var radio = document.getElementsByName("q"+num);
            var radioLength = radio.length;
            for(var j = 0;j < radioLength;j++){
                if(radio[j].checked)
                {
                    var radioValue = radio[j].value;
                    u_answers[i]=radioValue;
                    //	alert("第"+i+"题目的答案："+radioValue);
                }
            }
            num++;
        }
        for (var i = 0; i < answers.length; i++) {
            if(answers[i].toString()==u_answers[i].toString()){
                right.push(i);
            }
            else if(answers[i].toString()!=u_answers[i].toString() && u_answers[i].toString()!="0"){
                wrong.push(i);
                wrong_answer.push(u_answers[i].toString());
            }

        }
        if ((right.length+wrong.length)!=10) {
            alert("题目未完成，请重新作答！");
            window.location.reload(true);
        }
        else if(right.length==10){
            alert("恭喜您，答对"+right.length+"道题目");
            window.location.href="contact.html?openid=${openid}";
        }
        else if (right.length>6) {
            alert("恭喜您，答对"+right.length+"道题目,确认错误题目后请留下您的联系方式");
            window.location.href="wrong.html?wrong="+wrong+"&wrong_answer="+wrong_answer+"&openid=${openid}";
        }
        else if((wrong.length>1 ||wrong.length===1 )&&(right.length+wrong.length)==10){
            alert("很遗憾，答题错误");
            $.ajax({
                url:"wrongResult",
                type:"post",
                data:{
                    validity:0 ,
                    openid:'${openid}'
                },
                success:function (result) {
                    window.location.href="wrong.html?wrong="+wrong+"&wrong_answer="+wrong_answer+"&openid=${openid}";
                }
            })
        }

    }
</script>
<div id="main_container">
    <div class="container" id="contact">
        <div class="row col-wrap">
            <div id="left_container" class="col col-md-3 col-sm-12">
                <div class="templatemo_logo">
                    <img src="img/title1.png " style="width:80%;z-index:1;">

                </div>
            </div>
            <div id="right_container" class="col col-md-9 col-sm-12">

                <div class="row">

                </div>
                <!--	<form role="form" action="#" method="post">-->
                <div class="row"  style="color:black">
                    <div class="col-md-12">
                        <div class="form-group left-inner-addon">
                            <br>	1、中国有一个传统习俗，农历腊月二十三日或二十四日要过“小年”，通常人们要吃（   ）。<br><br>
                            <div class="opt"><input type="radio" id="q1a" name="q1" value="A"><label for="q1a">A.元宵&nbsp;</div>
                            <div class="opt"><input type="radio" name="q1" value="B" id="q1b"><label for="q1b">B.饴糖&nbsp;</div>
                            <div class="opt"><input type="radio" name="q1" value="C" id="q1c"><label for="q1c">C.腊八粥&nbsp;</div>
                            <div class="opt"><input type="radio" name="q1" value="D" id="q1d"><label for="q1d">D.饺子&nbsp;</div><br><br>
                        </div><hr>
                        <div class="form-group left-inner-addon">
                            2、旧俗春节期间大小店铺从大年初一起关门，而在（   ）开市，俗以这一天为财神圣日，认为选择这一天开市必将招财进宝。<br><br>
                            <div class="opt">	<input type="radio" name="q2" value="A" id="q2a"><label for="q2a">A.大年初三&nbsp;</div>
                            <div class="opt"><input type="radio" name="q2" value="B" id="q2b"><label for="q2b">B.大年初四&nbsp;</div>
                            <div class="opt"><input type="radio" name="q2" value="C" id="q2c"><label for="q2c">C.大年初五&nbsp;</div>
                            <div class="opt"><input type="radio" name="q2" value="D" id="q2d"><label for="q2d">D.正月十五&nbsp;</div><br><br>
                        </div><hr>
                        <div class="form-group left-inner-addon">
                            3、“孩子孩子你别馋，过了腊八就是年：腊八粥你喝几天，哩哩啦啦二十三；二十三，糖瓜粘；二十四，扫尘日；二十五，炖豆腐；
                            二十六，炸羊肉；二十七，杀公鸡；二十八，（    ）；二十九，蒸馒头；三十晚上熬一宿；大年初一去拜年。<br><br>
                            <div class="opt"><input type="radio" name="q3" value="A" id="q3a"><label for="q3a">A.做花灯&nbsp;</div>
                            <div class="opt"><input type="radio" name="q3" value="B" id="q3b"><label for="q3b">B.把面发&nbsp;</div>
                            <div class="opt"><input type="radio" name="q3" value="C" id="q3c"><label for="q3c">C.贴春联&nbsp;</div>
                            <div class="opt"><input type="radio" name="q3" value="D" id="q3d"><label for="q3d">D.剃头发&nbsp;</div><br><br>
                        </div><hr>
                        <div class="form-group left-inner-addon">
                            4、年画是中国画的一种，始于古代的“门神画”，请问下面哪个地区不属于年画的重要产地？<br><br>
                            <div class="opt"><input type="radio" name="q4" value="A" id="q4a"><label for="q4a">A.苏州桃花坞&nbsp;</div>
                            <div class="opt"><input type="radio" name="q4" value="B" id="q4b"><label for="q4b">B.天津杨柳青&nbsp;</div>
                            <div class="opt"><input type="radio" name="q4" value="C" id="q4c"><label for="q4c">C.山东潍坊&nbsp;</div>
                            <div class="opt"><input type="radio" name="q4" value="D" id="q4d"><label for="q4d">D.四川成都&nbsp;</div><br><br>
                        </div><hr>
                        <div class="form-group left-inner-addon">
                            5、下面有四句诗，你知道哪一句与春节有关？
                            <br><br>
                            <div class="opt" style="width:100%"><input type="radio" name="q5" value="A" id="q5a"><label for="q5a">A.举头望明月，低头思故乡&nbsp;</div>
                            <div class="opt" style="width:100%"><input type="radio" name="q5" value="B" id="q5b"><label for="q5b">B.借问酒家何处有，牧童遥指杏花村&nbsp;</div>
                            <div class="opt" style="width:100%"><input type="radio" name="q5" value="C" id="q5c"><label for="q5c">C.千门万户瞳瞳日，总把新桃换旧符&nbsp;</div>
                            <div class="opt" style="width:100%"><input type="radio" name="q5" value="D" id="q5d"><label for="q5d">D.独在异乡为异客，每逢佳节倍思亲&nbsp;</div><br><br>
                        </div><hr>
                        <div class="form-group left-inner-addon">

                            <div class="form-group left-inner-addon">
                                6、南宋词人辛弃疾《青玉案》中“东风夜放花千树”、“风萧声动，玉壶光转，一夜鱼龙舞”描绘的是（   ）晚上的狂欢情景。
                                <br><br>
                                <div class="opt"><input type="radio" name="q6" value="A" id="q6a"><label for="q6a">A.中秋节&nbsp;</div>
                                <div class="opt"><input type="radio" name="q6" value="B" id="q6b"><label for="q6b">B.元宵节&nbsp;</div>
                                <div class="opt"><input type="radio" name="q6" value="C" id="q6c"><label for="q6c">C.七夕节&nbsp;</div>
                                <div class="opt"><input type="radio" name="q6" value="D" id="q6d"><label for="q6d">D.除夕&nbsp;</div><br><br>
                            </div><hr>
                            <div class="form-group left-inner-addon">
                                7、上海商人（   ）将月历和年画结合起来，选用传统年画或新式风情题材，配上公历纪元和阴历的月份，名为“月份牌年画”，这是年画的新形式。<br>
                                <br>
                                <div class="opt"><input type="radio" name="q7" value="A" id="q7a"><label for="q7a">A.杭穉英&nbsp;</div>
                                <div class="opt">	<input type="radio" name="q7" value="B" id="q7b"><label for="q7b">B.李慕白&nbsp;</div>
                                <div class="opt">	<input type="radio" name="q7" value="C" id="q7c"><label for="q7c">C.郑曼陀&nbsp;</div>
                                <div class="opt">	<input type="radio" name="q7" value="D" id="q7d"><label for="q7d">D.周暮桥&nbsp;</div><br><br>
                            </div><hr>
                            <div class="form-group left-inner-addon">
                                8、在中国最古老的文字甲骨文中，“年”的写法为上面部分为“禾”字，下面部分为“人”字。沉甸甸的“禾”压弯了“人”的腰，寓意丰收后的庆祝。
                                请问你能从下面的甲骨文中，把“年”找出来吗？
                                <br><br>
                                <div class="opt"><input type="radio" name="q8" value="A" id="q8a"><label for="q8a">A.<img src="img/8-1.jpg" style="width:80px;height:80px"></div>
                                <div class="opt"><input type="radio" name="q8" value="B" id="q8b"><label for="q8b">B.<img src="img/8-2.jpg" style="width:80px;height:80px"></div>
                                <div class="opt"><input type="radio" name="q8" value="C" id="q8c"><label for="q8c">C.<img src="img/8-3.jpg" style="width:80px;height:80px"></div>
                                <div class="opt"><input type="radio" name="q8" value="D" id="q8d"><label for="q8d">D.<img src="img/8-4.jpg" style="width:80px;height:80px"></div>
                            </div><hr>
                            <div class="form-group left-inner-addon">
                                9、最初的“城隍”不是神，而是指城郊外面的护城壕。“城隍”最早的含义是由水庸（即指沟）衍化而来的，古代人最早信奉的护城沟渠神是“水庸神”，以后逐渐演变为城郊的守护神，即城隍神。
                                上海城隍庙中供奉的城隍老爷是元末明初的士大夫（    ）。
                                <br><br>
                                <div class="opt"><input type="radio" name="q9" value="A" id="q9a"><label for="q9a">A.刘伯温&nbsp;</div>
                                <div class="opt"><input type="radio" name="q9" value="B" id="q9b"><label for="q9b">B.秦裕伯&nbsp;</div>
                                <div class="opt"><input type="radio" name="q9" value="C" id="q9c"><label for="q9c">C.王沂&nbsp;</div>
                                <div class="opt"><input type="radio" name="q9" value="D" id="q9d"><label for="q9d">D.顾野王&nbsp;</div><br><br>
                            </div><hr>
                            <div class="form-group left-inner-addon">
                                10、所谓“老八样”是流传在浦东民间的菜谱，包含“八菜、八炒、八分盆” ，常用在红白喜事、重要节日、老人祝寿等场合。下面那个菜品不属于“老八样”之列？
                                <br><br>
                                <div></div>
                                <div class="opt"><input type="radio" name="q10" value="A" id="q10a"><label for="q10a" >A.扣三丝<br><img src="img/10-1.jpg" style="width:40%"></div>
                                <div class="opt"><input type="radio" name="q10" value="B" id="q10b"><label for="q10b" >B.红焖元蹄<br><img src="img/10-2.jpg" style="width:40%"></div>
                                <div class="opt"><input type="radio" name="q10" value="C" id="q10c"><label for="q10c" >C.清蒸鲈鱼<br><img src="img/10-3.jpg" style="width:40%"></div>
                                <div class="opt"><input type="radio" name="q10" value="D" id="q10d"><label for="q10d" >D.蒸三鲜<br><img src="img/10-4.jpg" style="width:40%"></div><br><br>
                            </div><hr>
                        </div>
                        <div class="form-group left-inner-addon">
                            <button  class="btn btn-primary" onclick="change()" >提交</button>
                            <button type="reset" class="btn btn-default float_r">重做</button>
                        </div>
                    </div> <!-- row -->
                    <!--	</form>-->
                </div>
            </div>


        </div>
    </div>
</body>

