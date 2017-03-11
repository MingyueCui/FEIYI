package com.springapp.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springapp.classes.SHA1;
import com.springapp.entity.WxArticles;
import com.springapp.entity.WxMaterial;
import com.springapp.entity.WxNews;
import com.springapp.entity.WxUser;
import com.springapp.filter.EmojiFilter;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 11369 on 2016/10/25.
 */
@Controller
@RequestMapping(value = "")
public class WxController  extends BaseController{
    private static final String APP_ID="wx03dab806a8de5b9a";
    private static final String APP_SECRET="4ef30e728d1f7d0efe9d00bc4f1401d6";
    private static final String DOMAIN="feiyi.ecnucpp.com";
    private static final String MCH_ID = "1253261801";
    private static String TOKEN = "WeiXin";

    @RequestMapping(value = "/Wx/getUserList")
    @ResponseBody
    public String getUserList() throws IOException {
        String ACCESS_TOKEN = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+ACCESS_TOKEN;
        String getReturn = getJSONString(url);
        JSONObject jsonObject = JSONObject.parseObject(getReturn);
        String returnString = "";
        if(jsonObject.get("errcode")==null) {
            Integer total = (Integer) jsonObject.get("total");
            Integer count = (Integer) jsonObject.get("count");
            JSONObject data = jsonObject.getJSONObject("data");
            if(data==null) {
                returnString = "没人关注啊";
                return returnString;
            }
            String openid = data.getString("openid");
            if(openid==null){
                returnString = "没人关注啊";
                return returnString;
            }
            JSONArray openidList = JSON.parseArray(openid);
            for(Object object : openidList){
                String openID = object.toString();
                JSONObject user = getUserInfo(ACCESS_TOKEN, openID);
                returnString+=user.toString();
                user.remove("privilege");
                toUser(user , openID);
            }
            returnString = "一共有" + total + "个人关注，获得" + count + "个用户信息";
        }
        return returnString;
    }


    public void toUser(JSONObject user ,String openid){
        WxUser wxUser=userDao.getByOpenID(openid);
        if(wxUser==null){
            wxUser =  JSONObject.toJavaObject(user, WxUser.class);
            wxUser.setOpenid(openid);
            wxUser.setNickname(EmojiFilter.filterEmoji(wxUser.getNickname()));
            try {
                if(wxUser.getOpenid()!=null&&!wxUser.getOpenid().equals(""))
                    userDao.save(wxUser);
            }catch (Exception e){
                wxUser=new WxUser();
                wxUser.setOpenid(openid);
                wxUser.setNickname("包含特殊字符");
                if(wxUser.getOpenid()!=null&&!wxUser.getOpenid().equals(""))
                    userDao.save(wxUser);
            }

        }/*else{
            WxUser temp = JSONObject.toJavaObject(user, WxUser.class);
            wxUser.setOpenid(openid);
            wxUser.setNickname(EmojiFilter.filterEmoji(temp.getNickname()));
            try {
                if(wxUser.getOpenid()!=null&&!wxUser.getOpenid().equals(""))
                    userDao.update(wxUser);
            }catch (Exception e){
                if(wxUser.getOpenid()!=null&&!wxUser.getOpenid().equals(""))
                    wxUser.setNickname("包含特殊字符");
                userDao.update(wxUser);
            }

        }*/
    }
    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取字节数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        conn.disconnect();
        System.out.println("info:" + url + " download success");
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
    @RequestMapping(value = "/Wx/GetOpenId")
    public void getOpenId(HttpServletRequest request, HttpServletResponse response, String returnUrl) throws IOException {
        String code = request.getParameter("code");
        HttpSession session = request.getSession();
        boolean isValidCode = true;
        String serviceUrl = URLEncoder.encode(
                "http://" + DOMAIN + request.getContextPath() + "/Wx/GetOpenId" + "?returnUrl=" + returnUrl, "utf-8");
        System.out.print(code+returnUrl);
        //检查是否已验证或者验证是否通过
        if (code == null || code.equals("authdeny")) {
            isValidCode = false;
        }
        //如果session未空或者取消授权，重定向到授权页面
        if ((!isValidCode) && session.getAttribute("openid") == null) {
            StringBuilder oauth_url = new StringBuilder();
            oauth_url.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
            oauth_url.append("appid=").append(APP_ID);
            oauth_url.append("&redirect_uri=").append(serviceUrl);
            oauth_url.append("&response_type=code");
            oauth_url.append("&scope=snsapi_userinfo");
            oauth_url.append("&state=1#wechat_redirect");
            //return "redirect:" + oauth_url;
            response.sendRedirect(oauth_url.toString());
        }
        //如果用户同意授权并且，用户session不存在，通过OAUTH接口调用获取用户信息
        if (isValidCode && session.getAttribute("openid") == null) {
            JSONObject obj = getAccessToken(APP_ID, APP_SECRET, code);
            String token = obj.getString("access_token");
            String openid = obj.getString("openid");
            System.out.print("token:"+token+"openid:"+openid);

            WxUser wxUser = userDao.getByOpenID(openid);
            if(wxUser != null) {
                JSONObject user = getUserInfo(token, openid);
                user.remove("privilege");
                toUser(user, openid);
            }
           /* WxUser wxUser=userDao.getByOpenID(openid);
            if(wxUser==null){
                wxUser =  JSONObject.toJavaObject(user, WxUser.class);
                wxUser.setOpenid(openid);
                wxUser.setNickname(EmojiFilter.filterEmoji(wxUser.getNickname()));
                try {
                    if(wxUser.getOpenid()!=null&&!wxUser.getOpenid().equals(""))
                        userDao.save(wxUser);
                }catch (Exception e){
                    wxUser=new WxUser();
                    wxUser.setOpenid(openid);
                    wxUser.setNickname("包含特殊字符");
                    if(wxUser.getOpenid()!=null&&!wxUser.getOpenid().equals(""))
                        userDao.save(wxUser);
                }

            }else{
                WxUser temp = JSONObject.toJavaObject(user, WxUser.class);
                wxUser.setOpenid(openid);
                wxUser.setNickname(EmojiFilter.filterEmoji(temp.getNickname()));
                try {
                    if(wxUser.getOpenid()!=null&&!wxUser.getOpenid().equals(""))
                        userDao.update(wxUser);
                }catch (Exception e){
                    if(wxUser.getOpenid()!=null&&!wxUser.getOpenid().equals(""))
                        wxUser.setNickname("包含特殊字符");
                    userDao.update(wxUser);
                }

            }*/
            session.setAttribute("user", wxUser);
            session.setAttribute("openid", openid);
            //return "redirect:https://www.baidu.com/s?wd=" + openid;
            response.sendRedirect(returnUrl);
        }
        /*print(response,  "isValidCode:"+isValidCode+"????"+"openid:"+session.getAttribute("openid"));*/
        //return "redirect:http://www.bing.com/";
    }

    private static String getJSONString(String url) throws IOException {
        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuilder jsonBuilder = new StringBuilder();
        String lines;
        while ((lines = reader.readLine()) != null) {
            jsonBuilder.append(lines);
        }
        reader.close();
        connection.disconnect();
        return jsonBuilder.toString();
    }

    private static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static String postJSONString(String pathUrl, String requestString) {
        try {
            //建立连接
            URL url = new URL(pathUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            //设置连接属性
            httpConn.setDoOutput(true);// 使用 URL 连接进行输出
            httpConn.setDoInput(true);// 使用 URL 连接进行输入
            httpConn.setUseCaches(false);// 忽略缓存
            httpConn.setRequestMethod("POST");// 设置URL请求方法
            // 设置请求属性
            // 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            byte[] requestStringBytes = requestString.getBytes("utf-8");
            httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
            httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");

            // 建立输出流，并写入数据
            OutputStream outputStream = httpConn.getOutputStream();
            outputStream.write(requestStringBytes);
            outputStream.close();
            // 获得响应状态
            int responseCode = httpConn.getResponseCode();

            if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
                // 当正确响应时处理数据
                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                // 处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine);
                }
                responseReader.close();
                httpConn.disconnect();
                return sb.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取授权令牌
     */
    public static JSONObject getAccessToken(String appid, String secret,
                                            String code) throws IOException {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
        urlBuilder.append("appid=" + appid);
        urlBuilder.append("&secret=").append(secret);
        urlBuilder.append("&code=").append(code);
        urlBuilder.append("&grant_type=authorization_code");
        String url = urlBuilder.toString();
        String jsonString = getJSONString(url);
        JSONObject jsonObj = JSONObject.parseObject(jsonString);
        return jsonObj;
    }

    /**
     * 获取用户信息
     * @param token
     * @param openid
     * @return
     * @throws IOException
     */
    public static JSONObject getUserInfo(String token, String openid) throws IOException {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://api.weixin.qq.com/cgi-bin/user/info?");
        urlBuilder.append("access_token=" + token);
        urlBuilder.append("&openid=").append(openid);
        urlBuilder.append("&lang=zh_CN");
        String url = urlBuilder.toString();
        String jsonString = getJSONString(url);
        JSONObject jsonObj = JSONObject.parseObject(jsonString);
        return jsonObj;
    }

    /**
     * 获得接口调用凭据
     * @return
     * @throws IOException
     */
    public String getAccessToken() throws IOException {
        String url = String.format(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                APP_ID, APP_SECRET);
        String jsonString = getJSONString(url);
        JSONObject jsonObj = JSONObject.parseObject(jsonString);
        String accessToken = jsonObj.getString("access_token");
        //return "redirect:https://www.baidu.com/s?wd=" + accessToken;
        return accessToken;
    }

    //@RequestMapping(value = "/Wx/GetQC")
    public String getQC(String scene_id) throws IOException {
        String accessToken = getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s", accessToken);
        String jsonString = String.format("{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %s}}}", scene_id);
        String postBackJsonString = postJSONString(url, jsonString);
        JSONObject postBackJsonObj = JSONObject.parseObject(postBackJsonString);
        String ticket = postBackJsonObj.getString("ticket");
        String qCUrl = String.format("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s", URLEncoder.encode(ticket, "utf-8"));
        return qCUrl;
    }

    @RequestMapping(value = "/Wx/getQRcoder", method = RequestMethod.GET)
    @ResponseBody
    public String getQRcoder(String agent,HttpSession session) throws IOException {
        String url = getQC(agent);
        String realPath = session.getServletContext().getRealPath("/WEB-INF/pages/Wx/AgentQRCode/");
        downLoadFromUrl(url, agent + ".jpg", realPath);
        return "success";
    }
    //http://blog.csdn.net/lhzjj/article/details/11678129


    //从输入流读取post参数
    private String readStreamParameter(ServletInputStream in) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    @RequestMapping(value = "/Wx/Receive")
    public void receiveGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 随机字符串，从微信用户获取
        String echostr = request.getParameter("echostr");
        if (checkSignature(request)) {
            response.getWriter().print(echostr);
            return;
        }
        response.getWriter().print("Error");
    }

    @RequestMapping(value = "/Wx/Receive", method = RequestMethod.POST)
    public void receive(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        String postStr = null;
        try {
            postStr = readStreamParameter(request.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //this.print(response, postStr);
        if (null != postStr && !postStr.isEmpty()) {
            Document document = null;
            try {
                document = DocumentHelper.parseText(postStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null == document) {
                //this.print(response, "");
                return;
            }
            Element root = document.getRootElement();
            String fromUsername = root.elementText("FromUserName");
            String toUsername = root.elementText("ToUserName");
            //String content = root.elementTextTrim("Content");
            String msgType = root.elementTextTrim("MsgType");//扫二维码关注：event
            String event = root.elementTextTrim("Event");//扫二维码关注：subscribe
            String eventKey = root.elementText("EventKey");//扫二维码关注：qrscene_
            String ticket = root.elementText("Ticket");//扫二维码关注

            String content = root.elementText("Content");
            System.out.print("out******************************" + content + msgType);
            if (eventKey!=null&&eventKey.equals("questionKey")) {
                System.out.print("questionKey******************************" + content + msgType);
                String time = new Date().getTime() + "";
                String answerMsg = "<xml>" +
                        "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" +
                        "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                        "<CreateTime>" + time + "</CreateTime>" +
                        "<MsgType><![CDATA[text]]></MsgType>" +
                        "<Content><![CDATA[正在建设中]]></Content>" +
                        "</xml>";
                this.print(response, answerMsg);
            }else  /*if (msgType.equals("event") && event!=null&&event.equals("subscribe")) */{
                System.out.print("subscribe" + content + msgType);
                String time = new Date().getTime() + "";
                String answerMsg = "<xml>" +
                        "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" +
                        "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                        "<CreateTime>" + time + "</CreateTime>" +
                        "<MsgType><![CDATA[text]]></MsgType>" +
                        "<Content><![CDATA[欢迎关注上海非遗]]></Content>" +
                        "</xml>";
                this.print(response, answerMsg);
            } /*else if (content.equals("2") || content.equals("二")) {
                    String time = new Date().getTime() + "";
                    String answerMsg = "<xml>" +
                            "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" +
                            "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                            "<CreateTime>" + time + "</CreateTime>" +
                            "<MsgType><![CDATA[text]]></MsgType>" +
                            "<Content><![CDATA[【2】禅心妈妈月子饮食调养方案为根据妈妈的体质和调理需求，结合妇产科学、中医学、营养学、食疗学、康复学、烹饪学的六个领域权威专家的经验，为每一位妈妈量身定制的。方案中的三大阶段，12周调理重点，84天、252餐饮均有相应的理论基础和实践验证。每周的调理顺序，调理理念，每一餐的功效均有不同。请参照此标准安排每天饮食，不要随意更改菜品顺序，添加或者删减食材内容，或改换为其他菜品，以免影响调养效果。如有疑问，请联系禅心妈妈微信公众号：chanxin51 \n" +
                            "禅心妈妈月子调养食材盒是与每一位妈妈个性化的调养方案配套使用的，其中包含粥、饭、汤、茶、糊的所有核心配方食材，便于家人的操作。在使用过程中，粥、饭均只需将材料清洗后放入蒸锅中蒸熟即可；茶类则需要放入养生茶壶中烧开后，煮沸后继续沸腾10分钟即可，也可以放入保温杯中泡水；所有汤包都是先放入清水中煮20分钟后，取出汤包后留汤汁作为汤底使用。汤品中可以添加其他时蔬，选材请在配套餐册的《应季时蔬附录页》当中选取项]]></Content>" +
                            "</xml>";
                    this.print(response, answerMsg);
                } else if (content.equals("3") || content.equals("三")) {
                    String time = new Date().getTime() + "";
                    String answerMsg = "<xml>" +
                            "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" +
                            "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                            "<CreateTime>" + time + "</CreateTime>" +
                            "<MsgType><![CDATA[text]]></MsgType>" +
                            "<Content><![CDATA[【3】很多妈妈吃我们的月子餐第一周和第二周跟我们反映说，奶水少，不足，比较着急，在这种情况下需要告诉宝妈，奶水的增加是一个循序渐进的过程，宝宝的胃口也是逐渐增大的，所以在第一周第二周宝宝不需要吃太多奶水，我们第一、第二周主要帮助妈妈们先排恶露，慢慢地把脾胃修复了以后，在第二周会适量添加一些助奶的汤方，所以妈妈在这个时候如果有缺乳的情况，请不要太过着急。调整好心态。如果第三周第四周奶水还是少的话，我们会推荐气血双补，调气生乳的经方来帮您调养。]]></Content>" +
                            "</xml>";
                    this.print(response, answerMsg);
                } else if (content.equals("4") || content.equals("四")) {
                    String time = new Date().getTime() + "";
                    String answerMsg = "<xml>" +
                            "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" +
                            "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                            "<CreateTime>" + time + "</CreateTime>" +
                            "<MsgType><![CDATA[text]]></MsgType>" +
                            "<Content><![CDATA[【4】分娩后，发生奶结的原因多是由于内分泌因素导致奶水减少，或者婴儿吸吮的次数不够，致使乳腺无法正常分泌乳汁，部分妈妈因肝气不畅导致乳头短小、凹陷，因喂奶造成乳头受伤造成奶量减少、奶水不足的情况，还有些妈妈因肝气郁结，乳房胀大，乳汁无法正常排出，出现这样的情况我们在第一周或者第二周先调补胃气，再疏肝理气。从饮食上开郁散结预防奶结的发生，您在平常的时候可以采用按摩+热敷的方法，并注意保持心情舒畅，保持乳房清洁如果还出现这样的情况，我们在第三周或者第四周向您推荐蒲公英散结茶或丝瓜络散结茶为您调理。]]></Content>" +
                            "</xml>";
                    this.print(response, answerMsg);
                } else if (content.equals("5") || content.equals("五")) {
                    String time = new Date().getTime() + "";
                    String answerMsg = "<xml>" +
                            "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" +
                            "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                            "<CreateTime>" + time + "</CreateTime>" +
                            "<MsgType><![CDATA[text]]></MsgType>" +
                            "<Content><![CDATA[【5】产后因为元气大伤，容易外感六邪，即风、寒、暑、湿、燥、火皆可侵袭而火化，产后五脏六腑处在较大的复旧过程中，其功能失调易引起内生之火，七情舒缓适当可致火由内生。阴液内耗，虚火内生，所以很容易出现：心烦意乱、眼干、眼痒、而且暴躁易冲动，经常发脾气，舌苔黄腻、口苦口干，口腔生疮，口臭、牙痛、牙龈红肿、牙根发炎等症状，另外，大便还会干燥。" +
                            "针对产后妈妈上火，我们建议是，防止外感，保持情绪稳定，多喝水。我们餐册第一周和第二周以补气健脾恢复体力排出恶露为主要目标，从第三周开始，逐渐加大滋阴生津的力度，另外，若上火情况严重，可以增加增液茶饮的服用量，对改善上火情况，都是十分有帮助的。\n]]></Content>" +
                            "</xml>";
                    this.print(response, answerMsg);
                } else if (content.equals("6") || content.equals("六")) {
                    String time = new Date().getTime() + "";
                    String answerMsg = "<xml>" + "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" + "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                            "<CreateTime>" + time + "</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>" + "<Content><![CDATA[【6】妈妈过敏\n" +
                            "过敏体质是指某类人群的免疫系统存在缺陷，他们的免疫系统异于常人，比较常见的原因是遗传性因素和分娩导致内分泌改变所引起。常见的表现有荨麻疹、过敏性鼻炎、花粉症、过敏性哮喘等。\n" +
                            "如果是过敏体质的母亲，请在配制餐册前，与我们进行沟通，最好有医院检查的过敏源检测报告，我们在配制您的餐册时，会避免添加导致您过敏的食物。 \n" +
                            "宝宝过敏\n" +
                            "宝宝过敏体质与遗传有相当大的关系，父母双方过敏遗传机率高达80%以上。在出生后的最初六年都处于易感期，极容易造成宝宝对尘螨、花粉、牛奶等高蛋白质过敏。\n" +
                            "这段时间很多宝宝都会出现胃肠功能、免疫功能不完善，机体抵抗力较弱，消化功能紊乱，肠吸收不良容易出现皮肤湿疹、过敏性鼻炎、过敏性咳嗽哮喘以及反复呼吸道感染性疾病（支气管炎肺炎）等。\n" +
                            "我们给妈妈的建议是：如果妈妈是特禀体质，请积极进行脱敏治疗，防止遗传给宝宝，最好进行母乳喂养，多晒太阳，多呼吸新鲜空气，避免花粉，烟尘等污染环境，及时接种疫苗对防止宝宝过敏也是十分重要的。]]></Content>" + "</xml>";
                    this.print(response, answerMsg);
                } else if (content.equals("7") || content.equals("七")) {
                    String time = new Date().getTime() + "";
                    String answerMsg = "<xml>" + "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" + "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                            "<CreateTime>" + time + "</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>" + "<Content><![CDATA[【7】产后高血压\n" +
                            "您妊娠期时候有过妊高征，产后很容易成为原发性高血压。您以前是否有过肾脏疾病，如果肾脏疾病治疗不及时，产后高血压的发病可能性很大。您产后如果长期精神紧张，肝气不舒，也很容易由于精神刺激诱发高血压。\n" +
                            "我们给您的建议是：积极调理心态，避免情绪波动，保持充足睡眠。低盐饮食（少于6g），要遵循少食多餐的饮食原则，多吃新鲜蔬菜和水果，一定要避免食用辛辣等刺激性食物，多吃含钾、钙丰富而含钠低的食品，如土豆、茄子、海带、莴笋。\n" +
                            "【注意】请在配餐之前与我们沟通，我们的餐册是荤素搭配的，通过我们的餐册和合理用药，对您的高血压是会有改善作用的。\n" +
                            "这种属于特殊疾病，食用我们的月子餐不会出大的偏差，但是我们不能治疗这些疾病。\n" +
                            "高血糖\n" +
                            "由于产后女性胰岛素分泌水平急剧变化，产后妈妈的血糖也会出现变化，一般比较常见的原因是妊娠糖尿病没有及时纠正所引起的，母乳喂养也可能会影响血糖水平的变化。出现这种情况，您平常是否有私自加餐的情况，是否摄入高糖的饮料或者水果，是否进食过多？]]></Content>" + "</xml>";
                    this.print(response, answerMsg);
                } *//*else {
                    String time = new Date().getTime() + "";
                    String answerMsg = "<xml>" +
                            "<ToUserName><![CDATA[" + fromUsername + "]]></ToUserName>" +
                            "<FromUserName><![CDATA[" + toUsername + "]]></FromUserName>" +
                            "<CreateTime>" + time + "</CreateTime>" +
                            "<MsgType><![CDATA[text]]></MsgType>" +
                            "<Content><![CDATA[请您按照提示输入对应的内容。]]></Content>" +
                            "</xml>";
                    this.print(response, answerMsg);
                }*//*
            }*/
            if (msgType.equals("event") && event.equals("subscribe") && eventKey.startsWith("qrscene_")) {
                System.out.print("subscribe******************************" + content + msgType);
                WxUser userbyOpenid = userDao.getByOpenID(fromUsername);
                if (userbyOpenid == null) {
                    WxUser wxUser = new WxUser();
                    wxUser.setOpenid(fromUsername);
                    userDao.save(wxUser);
                }
                //this.print(response, fromUsername);
                //this.print(response, eventKey.substring(8));
            }
//            String time = new Date().getTime() + "";
//            String textTpl = "<xml>" +
//                    "<ToUserName><![CDATA[%1$s]]></ToUserName>" +
//                    "<FromUserName><![CDATA[%2$s]]></FromUserName>" +
//                    "<CreateTime>%3$s</CreateTime>" +
//                    "<MsgType><![CDATA[%4$s]]></MsgType>" +
//                    "<Content><![CDATA[%5$s]]></Content>" +
//                    "<FuncFlag>0</FuncFlag>" +
//                    "</xml>";
//            if (null != keyword && !keyword.equals("")) {
//                String msgType = "text";
//                String contentStr = "Welcome to wechat world!";
//                String resultStr = textTpl.format(textTpl, fromUsername, toUsername, time, msgType, contentStr);
//                this.print(response, resultStr);
//            } else {
//                this.print(response, "Input something...");
//            }
        } else {
            System.out.print("subscribe******************************nothing");
            this.print(response, "");
        }
    }

    //向请求端发送返回数据
    public void print(HttpServletResponse response, String content) throws IOException {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\Administrator\\Desktop\\Result.txt", true);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {

        }
        try {
            response.getWriter().print(content);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {

        }
    }

    //数组转字符串
    public String ArrayToString(String[] arr) {
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            bf.append(arr[i]);
        }
        return bf.toString();
    }

    //sha1加密
    public String SHA1Encode(String sourceString) {
        String resultString = null;
        try {
            resultString = new String(sourceString);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            resultString = byte2hexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }

    public final String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString().toUpperCase();
    }

    private Boolean checkSignature(HttpServletRequest request) {
        // 微信加密签名，用于验证签名
        String signature = request.getParameter("signature");
        // 时间
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");

        String[] str = {TOKEN, timestamp, nonce};
        Arrays.sort(str); // 字典序排
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String digest = new SHA1().getDigestOfString(bigStr.getBytes()).toLowerCase();

        // 确认请求来至微信
        return digest.equals(signature);
    }


    @RequestMapping(value = "/Wx/SetMenu")
    public void createMenu(HttpServletResponse response) throws IOException {
        Map map = new HashMap();
        List<Map> menus = new LinkedList<Map>();

        //第一个菜单
        Map firstBtn = new HashMap();
        firstBtn.put("type", "view");
        firstBtn.put("name", "历史消息");
        firstBtn.put("url", "http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MzI3NTQxNTAyNQ==#wechat_webview_type=1&wechat_redirect");
        menus.add(firstBtn);

        //第二个菜单
        Map secondBtn = new HashMap();
        secondBtn.put("name","展馆行");
        List<Map> secondSubBtn = new LinkedList<Map>();
        Map myBtn = new HashMap();
        myBtn.put("type", "view");
        myBtn.put("name", "活动信息");
        myBtn.put("url", "http://mp.weixin.qq.com/mp/homepage?__biz=MzI3NTQxNTAyNQ==&hid=2&sn=c69ca36088ff71eda320c81813eed403#wechat_redirect");
        secondSubBtn.add(myBtn);
        Map myTest = new HashMap();
/*
        WxNews wxNews = newsForSend("展馆行");
*/
        myTest.put("type", "view");
        myTest.put("name", "展馆信息");
        myTest.put("url", "http://mp.weixin.qq.com/s?__biz=MzI3NTQxNTAyNQ==&mid=100000487&idx=1&sn=68c3552af5f8b4921b9bcb9106a22ea8&chksm=6b0468795c73e16f4f8fb13c326684fd6d0b81aae900ba4177b8755dba112769f58eba0754db#rd");
        secondSubBtn.add(myTest);
        secondBtn.put("sub_button", secondSubBtn);
        menus.add(secondBtn);

        //第三个菜单
        Map thirdBtn = new HashMap();
        thirdBtn.put("type","view");
        thirdBtn.put("name","活动列表");
        String redirect_url="http://"+DOMAIN+"/Weixin/Wx/result";
        thirdBtn.put("url", redirect_url);
        menus.add(thirdBtn);
        /*secondBtn.put("type", "view");
        secondBtn.put("name", "活动一览表");
        String redirect_url="http://"+DOMAIN+"/Wx/result";
        secondBtn.put("url", redirect_url);
        menus.add(secondBtn);*/
        /*//第二个菜单的子菜单
        List<Map> secondSubBtn = new LinkedList<Map>();
        Map myBtn = new HashMap();
        myBtn.put("type", "view");
        myBtn.put("name", "订单信息");
        myBtn.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3A%2F%2Fwww.yuechanxin.com%2Fcxmm%2FWx%2Finfo&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        secondSubBtn.add(myBtn);
        Map myTest = new HashMap();
        myTest.put("type", "click");
        myTest.put("name", "疑问解答");
        myTest.put("key", "questionKey");
        secondSubBtn.add(myTest);
        secondBtn.put("sub_button", secondSubBtn);
        menus.add(secondBtn);

        //第三个菜单
        Map thirdBtn = new HashMap();
        thirdBtn.put("name", "禅心妈妈");

        List<Map> thirdSubBtn = new LinkedList<Map>();
        Map newsBtn = new HashMap();
        newsBtn.put("type", "view");
        newsBtn.put("name", "企业动态");
        newsBtn.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3A%2F%2Fwww.yuechanxin.com%2Fcxmm%2FWx%2Factivity&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        thirdSubBtn.add(newsBtn);
        Map newsBtn2 = new HashMap();
        newsBtn2.put("type", "view");
        newsBtn2.put("name", "禅心分享");
        newsBtn2.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3A%2F%2Fwww.yuechanxin.com%2Fcxmm%2FWx%2Fshare&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        thirdSubBtn.add(newsBtn2);
        Map activityBtn = new HashMap();
        activityBtn.put("type", "view");
        activityBtn.put("name", "我的推广");
        activityBtn.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3A%2F%2Fwww.yuechanxin.com%2Fcxmm%2FWx%2FApply&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        thirdSubBtn.add(activityBtn);
        Map joinUsBtn = new HashMap();
        joinUsBtn.put("type", "view");
        joinUsBtn.put("name", "合作伙伴");
        joinUsBtn.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=http%3A%2F%2Fwww.yuechanxin.com%2Fcxmm%2FWx%2FPartner&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        thirdSubBtn.add(joinUsBtn);
        thirdBtn.put("sub_button", thirdSubBtn);
        menus.add(thirdBtn);*/
        map.put("button", menus);

        String accessToken = getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s", accessToken);
        JSONObject jObject = (JSONObject) JSONObject.toJSON(map);
        String jsonString = jObject.toString();
        String s = postJSONString(url, jObject.toString());
        print(response, jsonString + "\r\n" + s);
    }


    public String payJSAPI(String nonce_str, String body, String out_trade_no, int total_fee, String spbill_create_ip, String openid) throws NoSuchAlgorithmException, IOException {
        StringBuilder urlBuilder = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        long currentTime = System.currentTimeMillis();
        currentTime += 15 * 60 * 1000;
        Date now = new Date(currentTime);
        Date date = new Date(currentTime);
        String signStr = sign(nonce_str, body, out_trade_no, total_fee, spbill_create_ip, openid, now, date);
        urlBuilder.append("appid=" + APP_ID);
        urlBuilder.append("&mch_id=").append(MCH_ID);
        //urlBuilder.append("&device_info=").append( );
        urlBuilder.append("&nonce_str=").append(nonce_str);
        urlBuilder.append("&sign=").append(signStr);
        urlBuilder.append("&body=").append(body);
        urlBuilder.append("&out_trade_no=").append(out_trade_no);
        urlBuilder.append("&total_fee=").append(total_fee);
        urlBuilder.append("&spbill_create_ip=").append(spbill_create_ip);
        urlBuilder.append("&time_start=").append(df.format(now));
        urlBuilder.append("&time_expire=").append(df.format(date));
        urlBuilder.append("&notify_url=").append("");
        urlBuilder.append("&trade_type=").append("JSAPI");
        urlBuilder.append("&openid=").append(openid);
        String param = urlBuilder.toString();
        return sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder?", param);
    }

    public String sign(String nonce_str, String body, String out_trade_no, int total_fee, String spbill_create_ip, String openid, Date now, Date date) throws NoSuchAlgorithmException {
        StringBuilder urlBuilder = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        urlBuilder.append("appid=" + APP_ID);
        urlBuilder.append("&body=").append(body);
        urlBuilder.append("&mch_id=").append(MCH_ID);
        //urlBuilder.append("&device_info=").append( );
        urlBuilder.append("&nonce_str=").append(nonce_str);
        urlBuilder.append("&notify_url=").append("");
        urlBuilder.append("&openid=").append(openid);
        urlBuilder.append("&out_trade_no=").append(out_trade_no);
        urlBuilder.append("&sign=").append(nonce_str);
        urlBuilder.append("&spbill_create_ip=").append(spbill_create_ip);
        urlBuilder.append("&time_expire=").append(df.format(date));
        urlBuilder.append("&time_start=").append(df.format(now));
        urlBuilder.append("&total_fee=").append(total_fee);
        urlBuilder.append("&trade_type=").append("JSAPI");
        urlBuilder.append("&key=").append(APP_SECRET);
        String signStr = urlBuilder.toString();
        signStr = getMd5(signStr).toUpperCase();
        return signStr;
    }

    public static String getMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for (byte x : bs) {
            if ((x & 0xff) >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }

    /**
     * 发送图文消息
     * @param touser
     * @param mpnews
     * @return
     * @throws IOException
     */
    public String customSend(String touser,Map<String,String> mpnews) throws IOException {
        String access_token = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+access_token;
/*
        String touser = "o8d06w-sgpEBE0ZxdbsyksZLBQ6I";
*/
        String msgtype = "mpnews";
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("touser",touser);
        param.put("msgtype",msgtype);
        param.put("mpnews",mpnews);
        String result = sendPost(url, JSONObject.toJSONString(param));
        return result;
    }

    /**
     * 发送外链图文
     * @param touser
     * @param wxNewsList
     * @return
     * @throws IOException
     */
    public String customSend(String touser,List<WxNews> wxNewsList) throws IOException {
        String access_token = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+access_token;
/*
        String touser = "o8d06w-sgpEBE0ZxdbsyksZLBQ6I";
*/
        String msgtype = "news";
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("touser",touser);
        param.put("msgtype",msgtype);
        Map<String,String>news = new HashMap<String, String>();
        List<WxArticles> articles = new ArrayList<WxArticles>();
        for(WxNews wxNews: wxNewsList){
            WxArticles wxArticles = new WxArticles();
            wxArticles.setTitle(wxNews.getTitle());
            wxArticles.setDescription(wxNews.getDigest());
            wxArticles.setPicurl(wxNews.getAuthor());
        }
        String result = sendPost(url, JSONObject.toJSONString(param));
        return result;
    }

    /**
     *
     * @param type  素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count 返回素材的数量，取值在1到20之间
     * @param gotCount 递归已获取
     * @return
     * @throws IOException
     */
    public List<WxMaterial> batchgetMaterial(String type, Integer offset, Integer count, Integer gotCount) throws IOException {
        String access_token = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?ACCESS_TOKEN="+access_token;
        Map<String,Object> param = new HashMap<String,Object>();
       /* String type= "news";
        Integer offset = 0;
        Integer count = 2;*/
        param.put("type",type);
        param.put("offset", offset);
        param.put("count", count);
        String result = sendPost(url, JSONObject.toJSONString(param));
        List<WxMaterial> wxMaterials = new ArrayList<WxMaterial>();
        JSONObject jsonObject = JSON.parseObject(result);
        if(jsonObject.get("errcode")==null){
            Integer total_count = jsonObject.getInteger("total_count");
            Integer item_count =jsonObject.getInteger("item_count");
            gotCount += item_count;
            System.out.print(total_count);
            System.out.print("\n"+item_count);
            String item = jsonObject.getString("item");
            List<WxMaterial> list = JSONArray.parseArray(item, WxMaterial.class);
            wxMaterials.addAll(list);
            if(gotCount<total_count){
                wxMaterials.addAll(batchgetMaterial("news", count,total_count-item_count,gotCount));
            }
        }
        return wxMaterials;
    }

    /**
     * 根据关键字获取media_id
     * @param keyword
     * @return
     * @throws IOException
     */
    public Map<String, String> MediaID(String keyword) throws IOException {
        List<WxMaterial> wxMaterials = batchgetMaterial("news", 0, 20, 0);
        Map<String,String>media_ids = new HashMap<String, String>();
        for(WxMaterial wxMaterial:wxMaterials){
            System.out.print(wxMaterial.getMedia_id()+" "+wxMaterial.getUpdate_time()+"\n");
            if(wxMaterial.isIn(keyword))
                media_ids.put("media_id", wxMaterial.getMedia_id());
        }
        return media_ids;
    }

    /**
     * 根据关键字获得单条推文
     * @param keyword
     * @return
     * @throws IOException
     */
    public WxNews newsForSend(String keyword) throws IOException {
        List<WxMaterial> wxMaterials = batchgetMaterial("news", 0, 20, 0);
        WxNews wxNews = null;
        for(WxMaterial wxMaterial:wxMaterials){
            System.out.print(wxMaterial.getMedia_id()+" "+wxMaterial.getUpdate_time()+"\n");
            wxNews = wxMaterial.newsForSend(keyword);
            if(wxNews!=null)
                break;
        }
        return wxNews;
    }

    public static void main(String[] args) throws IOException {
        WxController wxController = new WxController();
      /*  System.out.print(wxController.customSend("o8d06w-sgpEBE0ZxdbsyksZLBQ6I",wxController.MediaID("展馆行")));*/
        WxNews wxNews = wxController.newsForSend("展馆行");
        System.out.print(wxNews.getUrl());
    }
}
